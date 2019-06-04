package edu.udel.cis.taschd.gen;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.udel.cis.taschd.assign.Assignment;
import edu.udel.cis.taschd.ca.CourseAssistant;
import edu.udel.cis.taschd.ca.CourseAssistantPool;
import edu.udel.cis.taschd.course.Course;
import edu.udel.cis.taschd.course.CourseInstance;
import edu.udel.cis.taschd.course.CourseInstancePool;
import edu.udel.cis.taschd.course.Section;
import edu.udel.cis.taschd.skill.SkillSet;
import edu.udel.cis.taschd.time.WeeklySchedule;

/**
 * <p>
 * The {@link GenerateAssignment} represents a generator tool for a
 * {@link CourseAssistantPool} and a {@link CourseInstancePool}. A
 * {@link CourseAssistantPool} is a set of {@link CourseAssistant}s available
 * for a given term. A {@link CourseInstancePool} is a set of
 * {@link CourseInstance}s available for a given term. It provides the methods
 * to generate the assignment. It hides the details of the internal
 * representation of this generation process.
 * </p>
 * 
 * <p>
 * {@link GenerateAssignment} uses modules ca, course, skill, time and assign.
 * </p>
 *
 * @author nikhil
 */
public class GenerateAssignment {
	/**
	 * This is the result we need to return as the real {@link Assignment}.
	 */
	private Assignment assign;

	/**
	 * This is the {@link CourseAssistantPool} regarding as one of the input of the
	 * generator.
	 */
	private CourseAssistantPool cap;

	/**
	 * This is the {@link CourseInstancePool} regarding as one of the input of the
	 * generator.
	 */
	private CourseInstancePool cip;

	/**
	 * This is an {@link ArrayList} of the {@link CourseAssistant}s, containing all
	 * the {@link CourseAssistant}s with the high score which is computed by the
	 * {@link #divideCaPool(ArrayList, Section, SkillSet).
	 */
	private ArrayList<CourseAssistant> highScoreCa = new ArrayList<>();

	/**
	 * This is an {@link ArrayList} of the {@link CourseAssistant}s, containing all
	 * the {@link CourseAssistant}s with the low score which is computed by the
	 * {@link #divideCaPool(ArrayList, Section, SkillSet).
	 */
	private ArrayList<CourseAssistant> lowScoreCa = new ArrayList<>();

	/**
	 * Constructs a new {@link GenerateAssignment} with the given
	 * {@link CourseAssistantPool} and {@link CourseInstancePool}.
	 * 
	 * @param cap a non-{@code null} {@link CourseAssistantPool}.
	 * @param cip a non-{@code null} {@link CourseInstancePool}.
	 */
	public GenerateAssignment(CourseAssistantPool cap, CourseInstancePool cip) {
		this.cap = cap;
		this.cip = cip;
		this.assign = new Assignment();

	}

	/**
	 * This is a method for generating the {@link Assignment} for the sections based
	 * on several constraints, listed as scores of {@link CourseAssistant}s, course
	 * load, is the {@link Section}s consecutive or not, the {@link CourseAssistant}
	 * assigned to a lecture {@link Section} should also be assigned to the
	 * corresponding lab {@link Section} or inverse situation, the {@link Course}
	 * level and the {@link CourseAssistant} level. For more optimization conditions
	 * we are still working.
	 * 
	 * @param ci a non-{@code null} {@link ArrayList} of {@link CourseInstance}s.
	 * @param ca a non-{@code null} {@link ArrayList of {@link CourseAssistant}s.
	 */
	private void createTaAssignment(ArrayList<CourseInstance> ci, ArrayList<CourseAssistant> ca) {

		CourseAssistant possibleCa;
		SkillSet courseSkills;
		ArrayList<CourseAssistant> consideredCa = new ArrayList<>();

		for (int i = 0; i < ci.size(); i++) {

			courseSkills = ci.get(i).getCourse().getSkills(); // get a course skill set
			int index = 0;

			for (Section sec : ci.get(i).getSections()) {

				int numTaAssigned = 0;
				int numLaAssigned = 0;
				consideredCa.clear();

				if (!sec.isTaAssigned()) {

					Section previousSec = null;
					if (index != 0 && index < Iterables.size(ci.get(i).getSections()))
						previousSec = Iterables.get(ci.get(i).getSections(), index - 1);
					// to check if the section is consecutive to previous section then assign same
					// CA

					if (index != 0 && index < Iterables.size(ci.get(i).getSections())
							&& sec.getLocation().equalsIgnoreCase(previousSec.getLocation())
							&& sec.getInstructorName().equalsIgnoreCase(previousSec.getInstructorName())
							&& sec.getSchedule().wtpsScore(previousSec.getSchedule()) < 0.3 && sec.isMtac()
							&& previousSec.isTaAssigned()) {

						// Assign course assistant from previous section to consecutive section
						copyAssignment(sec, previousSec);
					}

					// Assign Lab section with same CA assigned to corresponding lecture section
					else if (sec.getSectionType().equalsIgnoreCase("L") && sec.getCorrespondingLecture() != null
							&& sec.getCorrespondingLecture().isTaAssigned()) {

						// Assign course assistant from corresponding lecture to lab section
						copyAssignment(sec, sec.getCorrespondingLecture());
					}

					else if (!sec.getSectionType().equalsIgnoreCase("L") && sec.getCorrespondingLab() != null
							&& sec.getCorrespondingLab().isTaAssigned()) {

						// Assign course assistant from corresponding lab to lecture section
						copyAssignment(sec, sec.getCorrespondingLab());
					}

					else { // Assign if it is not a consecutive section

						divideCaPool(ca, sec, courseSkills);

						// run till section is assigned TA or every ta from highScore and lowScore pool
						// is considered
						
						while (!(sec.isTaAssigned() || consideredCa.size() == highScoreCa.size() + lowScoreCa.size())) {

							if (numTaAssigned < sec.getNumOfTA()) {

								if (!highScoreCa.isEmpty() && consideredCa.size() != highScoreCa.size()) {

									possibleCa = highScoreCa.get((int) (Math.random() * highScoreCa.size()));

									// to check if course is 600+ level and selected student is not graduate
									if (!(ci.get(i).getCourse().getCourseCode().matches("^[6-9].*$")
											&& !possibleCa.getGraduateStudent())) {

										// to check is course load for student is less than threshold and the selected
										// student was not previously considered
										if (possibleCa.getCourseLoad() < 60 && !consideredCa.contains(possibleCa)
												&& consideredCa.size() != highScoreCa.size()) {

											sec.addAssignedCourseAssistant(possibleCa); // assign CA
											possibleCa.setCourseLoad(
													possibleCa.getCourseLoad() + sec.getCurrentEnrollment()); // increment
											// the
											// load
											numTaAssigned++; // increment number of Ta assigned

										}
										// if no ta can be assigned from highScoreTa pool then select from lowScoreTa
										// pool
									}

									consideredCa.add(possibleCa); // add to considered Ca list
								}

								else if (!lowScoreCa.isEmpty()
										&& consideredCa.size() != highScoreCa.size() + lowScoreCa.size()) {

									possibleCa = lowScoreCa.get((int) (Math.random() * lowScoreCa.size()));
									// to check if course is 600+ level and selected student is not graduate
									if (!(ci.get(i).getCourse().getCourseCode().matches("^[6-9].*$")
											&& !possibleCa.getGraduateStudent())) {

										if (possibleCa.getCourseLoad() < 60 && !consideredCa.contains(possibleCa)) {

											sec.addAssignedCourseAssistant(possibleCa); // assign CA
											possibleCa.setCourseLoad(
													possibleCa.getCourseLoad() + sec.getCurrentEnrollment()); // increment
											// the
											// load
											numTaAssigned++; // increment number of Ta assigned

										}
									}
									consideredCa.add(possibleCa); // add to considered Ca list

								}

							}

							if (numLaAssigned < sec.getNumOfLA()) {

								if (!lowScoreCa.isEmpty()
										&& consideredCa.size() != highScoreCa.size() + lowScoreCa.size()) {
									
									possibleCa = lowScoreCa.get((int) (Math.random() * lowScoreCa.size()));

									if (possibleCa.getCourseLoad() < 60 && !consideredCa.contains(possibleCa)
											&& !possibleCa.getGraduateStudent()) {

										sec.addAssignedCourseAssistant(possibleCa); // assign CA
										possibleCa
										.setCourseLoad(possibleCa.getCourseLoad() + sec.getCurrentEnrollment()); // increment
										// the load
										numLaAssigned++; // increment number of La assigned

									}

									consideredCa.add(possibleCa); // add to considered Ca list
								}
							}

							
							if (numTaAssigned == sec.getNumOfTA() && numLaAssigned == sec.getNumOfLA()) {

								sec.setTaAssigned(true);
							}

						}

					}
					index++;
				}
			}
		}

	}

	/**
	 * This method copies the {@link CourseAssistant} from the previous
	 * {@link Section} to current {@link Section}. This method is used when we find
	 * these {@link Section}s are consecutive and want to assign the
	 * {@link CourseAssistant} of previous {@link Section} to this {@link Section}.
	 * Just one thing need to be considered, the course load should not exceed the
	 * given value.
	 * 
	 * @param sec         the current {@link Section}.
	 * @param previousSec the previous {@link Section}.
	 */
	private void copyAssignment(Section sec, Section previousSec) {

		// Assign course assistant from previous section to consecutive section
		for (CourseAssistant c : previousSec.getAssignedCourseAssistants()) {

			sec.addAssignedCourseAssistant(c);

			// if consecutive sections are labs then don't add load
			if (!sec.getSectionType().equalsIgnoreCase("L"))
				c.setCourseLoad(c.getCourseLoad() + sec.getCurrentEnrollment());
		}
		sec.setTaAssigned(true);

	}

	/**
	 * This method divides the {@link CourseAssistantPool} based on the comparative
	 * score threshold, those which the scores are greater than 0.4 goes to the high
	 * score pool, those which the scores are less than 0.4 goes to the low score
	 * pool.
	 * 
	 * @param ca           a non-{@code null} {@link CourseAssistant}.
	 * @param sec          a non-{@code null} {@link Section}.
	 * @param courseSkills a non-{@code null} {@link SkillSet} of a {@link Course}.
	 */
	private void divideCaPool(ArrayList<CourseAssistant> ca, Section sec, SkillSet courseSkills) {

		double finalScore = 0;
		double skillScore = 0, wtpsScore = 0;
		WeeklySchedule sectionSchedule = sec.getSchedule(); // get the wtps
		this.highScoreCa.clear();
		this.lowScoreCa.clear();

		for (int j = 0; j < ca.size(); j++) {

			skillScore = courseSkills.skillScore(ca.get(j).getPossessedSkillset());
			wtpsScore = sectionSchedule.wtpsScore(ca.get(j).getWtps());

			if (!sec.isMtac())
				wtpsScore = 1.0;

			// System.out.println("CA"+j +" "+ skillScore+" " + wtpsScore);
			finalScore = skillScore * wtpsScore;
			// System.out.println("CA"+j +" "+ finalScore);
			// divide the course assistant into highScore and lowScore pool
			if (finalScore > 0.3) {
				this.highScoreCa.add(ca.get(j));

			} else {

				this.lowScoreCa.add(ca.get(j));

			}

		}

	}

	/**
	 * This method is responsible for returning the assignment based on the
	 * {@link #createTaAssignment(ArrayList, ArrayList)} method. For each
	 * {@link Section} in each {@link CourseInstance}, it will give the assignment.
	 * Finally, it returns a {@link HashMap} as the assignment.
	 * 
	 * @return a {@link HashMap} representing the final assignment.
	 */
	public Assignment assign() {

		ArrayList<CourseAssistant> temp1 = Lists.newArrayList(cap.getCourseAssistantSet());
		ArrayList<CourseInstance> temp2 = Lists.newArrayList(cip.getCourseInstanceSet());

		createTaAssignment(temp2, temp1);

		// HashMap<Section, Set<CourseAssistant>> assignment = new HashMap<>();

		for (CourseInstance ci : cip.getCourseInstanceSet()) {

			for (Section sec : ci.getSections()) {

				if (sec.isTaAssigned()) {

					for (CourseAssistant ca : sec.getAssignedCourseAssistants())
						assign.setCaForSection(sec, ca);
				}
			}
		}

		return assign;
	}

}

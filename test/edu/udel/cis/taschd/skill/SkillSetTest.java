package edu.udel.cis.taschd.skill;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.Test;
import edu.udel.cis.taschd.skill.SkillTest;

/**
 * 
 * Tests which corresponds to the class skill set of a Skill module
 *
 * @author nikhil
 * @author matthew
 */

public class SkillSetTest {
	

	
	
	@SuppressWarnings("unused")
	private static PrintStream out = System.out;

	/**
	 * Empty SkillSet.
	 */
	private SkillSet skillset1 = new SkillSet();

	/**
	 * Skill set with 2 skills: Java, Python
	 */

	private static ArrayList<Skill> c1 = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{add(new Skill("Java"));add(new Skill("Python"));}};
	
    
	private static SkillSet skillset2 = new SkillSet(c1);
	
	public static ArrayList<Skill> skillAL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = -5895423340747161305L;

	{add(SkillTest.java);add(SkillTest.python);}};
	public static SkillSet skillA = new SkillSet(skillAL);
	
	public static ArrayList<Skill> skillBL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = 5035178965512010013L;

	{add(SkillTest.Perl);add(SkillTest.fortran);add(SkillTest.java);add(SkillTest.ml);}};
	public static SkillSet skillB = new SkillSet(skillBL);
	
	public static ArrayList<Skill> skillCL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = -6163649863121702055L;

	{add(SkillTest.cpp);add(SkillTest.csharp);add(SkillTest.c);add(SkillTest.sql);}};
	public static SkillSet skillC = new SkillSet(skillCL);
	
	public static ArrayList<Skill> skillDL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = -5527591040336720871L;

	{add(SkillTest.mips);add(SkillTest.javascript);add(SkillTest.women);add(SkillTest.alloy);}};
	public static SkillSet skillD = new SkillSet(skillDL);
	
	public static ArrayList<Skill> skillEL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = -281073749414942399L;

	{add(SkillTest.html);add(SkillTest.css);add(SkillTest.php);}};
	public static SkillSet skillE = new SkillSet(skillEL);
	
	public static ArrayList<Skill> skillFL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = 3009216568270205267L;

	{add(SkillTest.sql);add(SkillTest.python);add(SkillTest.cpp);}};
	public static SkillSet skillF = new SkillSet(skillFL);

	public static ArrayList<Skill> skillGL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = 1946355543414487435L;

	{add(SkillTest.java);add(SkillTest.c);add(SkillTest.fortran);add(SkillTest.python);add(SkillTest.php);add(SkillTest.Perl);}};
	public static SkillSet skillG = new SkillSet(skillGL);
	
	public static ArrayList<Skill> skillHL = new ArrayList<Skill>(){/**
		 * 
		 */
		private static final long serialVersionUID = -3145934498854615955L;

	{add(SkillTest.java);add(SkillTest.matlab);}};
	public static SkillSet skillH = new SkillSet(skillHL);

	
	
	@Test
	public void test1() {

		ArrayList<Skill> c2 = new ArrayList<>();
		c2.add(new Skill("Java"));
		c2.add(new Skill("MPI"));
		c2.add(new Skill("SQL"));

		SkillSet obj1 = new SkillSet(c2);
		int count =  0;
		for (Skill skill : obj1.getSkills()) {
			assertEquals(c2.get(count), skill);
			count++;
		}

	}
	
	@Test
	public void test2() {
		Skill skill1 = new Skill("Java");
		Skill skill2 = new Skill("Python");
		skillset1.addSkill(skill1);
        skillset1.addSkill(skill2);

		assertEquals(true,skillset2.equals(skillset1));
	}
	
	
	@Test
	public void test3() {
		Skill skill1 = new Skill("Java");
		skillset1.addSkill(skill1);
      
		skillset1.addSkill(skill1);
		
		int count =  0;
		for (Skill skill : skillset1.getSkills()) {
			
			assertEquals(0,skill1.compareTo(skill));
			count++;
		}
		//size of the set
		assertEquals(1, count);
	}
	
	@Test
	public void test4() {
		Skill skill1 = new Skill("Java");
		Skill skill2 = new Skill("MPI");
		skillset1.addSkill(skill1);
        skillset1.addSkill(skill2);

		assertEquals(false,skillset2.equals(skillset1));
	}
	
	@Test
	public void test5() {
		Skill skill1 = new Skill("Java");
	
		skillset1.addSkill(skill1);
      

		assertEquals(false,skillset2.equals(skillset1));
	}
	
	@Test
	public void test6() {
		Skill skill1 = new Skill("Java");
		Skill skill2 = new Skill("Python");
		skillset1.addSkill(skill1);
		skillset1.addSkill(skill2);
		     
        String obj = "Java, Python";
        
        
        assertEquals(0,obj.compareTo(skillset1.toString()));
    
		
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void test7() {
		Skill skill1 = new Skill("Java");
		Skill skill2 = new Skill("Python");
		skillset1.addSkill(skill1);
		skillset1.addSkill(skill2);
		     
        String obj = "Java, Python";
        
        
        assertEquals(false,skillset1.equals(obj));
    
		
	}

}

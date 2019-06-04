package edu.udel.cis.taschd.time;

import java.time.DayOfWeek;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * {@link TimeInterval} represents a single {@link TimeInterval} within a week.
 * A {@link TimeInterval} begins at a certain minute in the week, and has a
 * duration, measured in minutes. This class is immutable.
 * <p>
 * 
 * <p>
 * For now, a {@link TimeInterval} will simply wrap a {@link String}, it uses
 * nothing.
 * </p>
 * 
 * @author nikhil
 * @author siegel
 */
public class TimeInterval implements Comparable<TimeInterval> {
	/**
	 * The minute within the week that this {@link TimeInterval} begins. This is
	 * an integer in the range [0,10080). Note that 10080=7*24*60 is the number
	 * of minutes in a week. 0 represents midnight at the very beginning of
	 * Monday. 10079 represents 11:59:00 PM on Sunday night. This follows the
	 * ISO-8601 standard, where 1 represents Monday, and 7 represents Sunday.
	 */
	private int start;

	/**
	 * The number of minutes in the {@link TimeInterval}. This is an integer in
	 * the range [0,1440]. Note that a {@link TimeInterval} can extend into the
	 * next day, or even into the next week.
	 */
	private int duration;

	/**
	 * Constructs a new {@link TimeInterval} with given start minute and
	 * duration.
	 * 
	 * @param start
	 *                     the minute of the week at which this
	 *                     {@link TimeInterval} begins, an integer in the range
	 *                     [0,10080). 0 represents Monday at midnight.
	 * @param duration
	 *                     the duration of this {@link TimeInterval}, measured
	 *                     in minutes, an integer in the range [0,1440].
	 */
	public TimeInterval(int start, int duration) {
		if (start < 0 || start >= 10080)
			throw new IllegalArgumentException(
					"start must be in range [0,10080) but is " + start);
		this.start = start;
		if (duration < 0 || duration > 1440)
			throw new IllegalArgumentException(
					"duration must be in range [0,1440] but is " + duration);
		this.duration = duration;
	}

	/**
	 * Constructs a new {@link TimeInterval} with data specified in more
	 * convenient human-oriented structure.
	 * 
	 * @param dayOfWeek
	 *                        the day of the week in which the
	 *                        {@link TimeInterval} begins.
	 * @param startHour
	 *                        the hour of the day in which the
	 *                        {@link TimeInterval} begins, an integer in the
	 *                        range [0,24).
	 * @param startMinute
	 *                        the minute of the hour in which the
	 *                        {@link TimeInterval} begins, an integer in the
	 *                        range [0,60).
	 * @param duration
	 *                        the duration of the {@link TimeInterval}, in
	 *                        minutes, an interval in the range [0,1440].
	 */
	public TimeInterval(DayOfWeek dayOfWeek, int startHour, int startMinute,
			int duration) {
		if (dayOfWeek == null)
			throw new IllegalArgumentException("dayOfWeek can not be null");
		if (startHour < 0 || startHour >= 24)
			throw new IllegalArgumentException(
					"start hour must be in range [0,23] but is " + startHour);
		if (startMinute < 0 || startMinute >= 60)
			throw new IllegalArgumentException(
					"start minute must be in range [0,59] but is "
							+ startMinute);
		this.start = (dayOfWeek.getValue() - 1) * 24 * 60 + startHour * 60
				+ startMinute;
		if (duration < 0 || duration > 1440)
			throw new IllegalArgumentException(
					"duration must be in range [0,1440] but is " + duration);
		this.duration = duration;
	}

	/**
	 * Constructs new {@link TimeInterval} instance based on the same kind of
	 * {@link String} returned by the {@link #toString()} method.
	 * 
	 * @param str
	 *                string representation of a {@link TimeInterval} as
	 *                returned by the {@link #toString()} method.  eg: Tue 11:00–12:15
	 */
	public TimeInterval(String str) {
		// TODO
		DayOfWeek startDay = null;
		DayOfWeek endDay = null;
		int end =0;
		int startHour = 0;
		int startMinute = 0;
		int endHour = 0;
		int endMinute = 0;
		int duration = 0;
		
		String pattern = "(\\A[aA-zZ]{3})+\\s+([0-9]{1,2})+[:]([0-9]{1,2})+[^aA-zA,0-9]+([0-9]{1,2})+[:]([0-9]{1,2})+"
				+ "\\s*[(]*([aA-zZ]{3})*[)]*";

	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(str);
	     
	      if (m.find( )) {
	    	
	    	  
	          startDay = longName(m.group(1));
	          startHour = Integer.parseInt(m.group(2));
	          startMinute = Integer.parseInt(m.group(3));
	          endHour = Integer.parseInt(m.group(4));
	          endMinute = Integer.parseInt(m.group(5));
	          if(m.group(6) != null)
	        	  endDay= longName(m.group(6));
	          else
	        	  endDay= startDay;
	          
	      }
		
	
		
		if (startDay == null)
			throw new IllegalArgumentException("startDay can not be null");
		if (endDay == null)
			throw new IllegalArgumentException("endDay can not be null");
		if (startHour < 0 || startHour >= 24)
			throw new IllegalArgumentException(
					"start hour must be in range [0,23] but is " + startHour);
		if (startMinute < 0 || startMinute >= 60)
			throw new IllegalArgumentException(
					"start minute must be in range [0,59] but is "
							+ startMinute);
		this.start = (startDay.getValue() - 1) * 24 * 60 + startHour * 60
				+ startMinute;
		
		if (endHour < 0 || endHour >= 24)
			throw new IllegalArgumentException(
					"end hour must be in range [0,23] but is " + endHour);
		if (endMinute < 0 || endMinute >= 60)
			throw new IllegalArgumentException(
					"end minute must be in range [0,59] but is "
							+ endMinute);
		
		end = (endDay.getValue() - 1) * 24 * 60 + endHour * 60
				+ endMinute;
		duration = end - this.start;
		
		if (duration < 0 || duration > 1440)
			throw new IllegalArgumentException(
					"duration must be in range [0,1440] but is " + duration);
		this.duration = duration;
	}

	/**
	 * This is a static method to return the abbreviate of the day of week. It
	 * only has seven conditions and cannot be modified.
	 * 
	 * @param day
	 *                a enum value imported from {@link java.time.DayOfWeek},
	 *                representing the seven days in one week.
	 * 
	 * @return switching according to the given parameter, it is a abbreviate of
	 *         the day of week.
	 */
	public static String shortName(DayOfWeek day) {
		switch (day) {
			case FRIDAY :
				return "Fri";
			case MONDAY :
				return "Mon";
			case SATURDAY :
				return "Sat";
			case SUNDAY :
				return "Sun";
			case THURSDAY :
				return "Thu";
			case TUESDAY :
				return "Tue";
			case WEDNESDAY :
				return "Wed";
			default :
				throw new RuntimeException("unreachable");
		}
	}	
	
	
	/**
	 * This is a static method to return the  day of week. It
	 * only has seven conditions and cannot be modified.
	 * 
	 * @param day
	 *                a String value of abbreviated day given as input,
	 *                representing the seven days in one week.
	 * 
	 * @return switching according to the given parameter, it is a long form representation of 
	 *         {@link java.time.DayOfWeek} of the day of week.
	 */
	
	public static DayOfWeek longName(String day) {
		switch (day) {
			case "Fri" :
				return DayOfWeek.FRIDAY;
			case "Mon" :
				return DayOfWeek.MONDAY;
			case "Sat" :
				return DayOfWeek.SATURDAY;
			case "Sun" :
				return DayOfWeek.SUNDAY;
			case "Thu" :
				return DayOfWeek.THURSDAY;
			case "Tue" :
				return DayOfWeek.TUESDAY;
			case "Wed" :
				return DayOfWeek.WEDNESDAY;
			default :
				throw new RuntimeException("unreachable");
		}
	}

	/**
	 * This method returns a formatted String, for now it is used to give the
	 * standard form of time in {@link #timeString()}, like if the minute is 9,
	 * it will convert to "09". If the minute is 11, it will remain "11".
	 * 
	 * @param x
	 *              it is the minute of hour.
	 * 
	 * @return the formatted String of minute as required above.
	 */
	private String make2Digits(int x) {
		if (x < 10)
			return "0" + x;
		else
			return "" + x;
	}

	/**
	 * A getter to get the start time, represented as the number of minutes
	 * since midnight of Monday.
	 * 
	 * @return the start time, in minutes since Monday 00:00:00.
	 */
	public int getStartTime() {
		return start;
	}

	/**
	 * A getter to get the day of the week of the start time of this
	 * {@link TimeInterval}.
	 * 
	 * @return the day of the week in which this {@link TimeInterval} starts.
	 */
	public DayOfWeek getStartDayOfWeek() {
		return DayOfWeek.of(1 + start / (24 * 60));
	}

	/**
	 * A getter to get the hour of the day in which this {@link TimeInterval}
	 * begins. This is an integer in the range [0,23]. 0 represents midnight, 1
	 * is 1:00 AM, etc.
	 * 
	 * @return the hour of the day in which this {@link TimeInterval} begins.
	 */
	public int getStartHourOfDay() {
		return (start % (24 * 60)) / 60;
	}

	/**
	 * A getter to get the minute of the hour in which this {@link TimeInterval}
	 * begins. This is an integer in range [0,59].
	 * 
	 * @return the minute of the hour of the start time.
	 */
	public int getStartMinuteOfHour() {
		return start % 60;
	}

	/**
	 * A getter to get the duration of this {@link TimeInterval}, in minutes.
	 * 
	 * @return the duration, in minutes.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * A getter to get the end time, represented as the number of minutes since
	 * midnight of Monday in the starting week. Note that the end time can
	 * extend into the next week.
	 * 
	 * @return the end time, in minutes since start Monday 00:00:00.
	 */
	public int getEndTime() {
		return start + duration;
	}

	/**
	 * A getter to get the day of the week of the end time of this
	 * {@link TimeInterval}.
	 * 
	 * @return the day of the week in which this {@link TimeInterval} end.
	 */
	public DayOfWeek getEndDayOfWeek() {
		return DayOfWeek.of(1 + ((start + duration) / (24 * 60)) % 7);
	}

	/**
	 * A getter to get the hour of the day in which this {@link TimeInterval}
	 * ends. This is an integer in the range [0,23]. 0 represents midnight, 1 is
	 * 1:00 AM, etc.
	 * 
	 * @return the hour of the day in which this {@link TimeInterval} ends.
	 */
	public int getEndHourOfDay() {
		return ((start + duration) % (24 * 60)) / 60;
	}

	/**
	 * A getter to get the minute of the hour in which this {@link TimeInterval}
	 * ends. This is an integer in range [0,59].
	 * 
	 * @return the minute of the hour of the end time.
	 */
	public int getEndMinuteOfHour() {
		return (start + duration) % 60;
	}

	/**
	 * Computes the number of minutes between the end of that
	 * {@link TimeInterval} and the beginning of this one. This assumes that the
	 * {@link TimeInterval}s are repeated every week, infinitely into the past
	 * and future, so that this difference is always defined.
	 * 
	 * @param that
	 *                 another (non-{@code null}) {@link TimeInterval}.
	 * 
	 * @return the number of minutes between the end of that
	 *         {@link TimeInterval} and the beginning of the next occurrence of
	 *         this {@link TimeInterval}; always a non-negative integer.
	 */
	public int minutesAfter(TimeInterval that) {
		int result = start - that.getEndTime();

		while (result < 0)
			result += 7 * 24 * 60; // number of minutes in the week
		return result;
	}

	/**
	 * Compares the start times of this {@link TimeInterval} with that one.
	 * Returns -1, 0, or 1 depending on whether this starts earlier, same time,
	 * or later than that
	 * 
	 * @param that
	 *                 another non-{@code null} {@link TimeInterval}.
	 * 
	 * @return -1 if this one starts at earlier time in the week; 0 if the
	 *         starts times are equal; +1 if this one starts at a later time in
	 *         the week.
	 */
	public int compareStart(TimeInterval that) {
		int diff = this.start - that.start;

		if (diff < 0)
			return -1;
		else if (diff == 0)
			return 0;
		else
			return 1;
	}

	/**
	 * Compares the end times of this {@link TimeInterval} with that one.
	 * Returns -1, 0, or 1 depending on whether this ends earlier, same time, or
	 * later than that.
	 * 
	 * @param that
	 *                 another non-{@code null} {@link TimeInterval}.
	 * 
	 * @return -1 if this one ends at earlier time in the week; 0 if the end
	 *         times are equal; +1 if this one ends at a later time in the week.
	 * 
	 */
	public int compareEnd(TimeInterval that) {
		int diff = this.getEndTime() - that.getEndTime();

		if (diff < 0)
			return -1;
		else if (diff == 0)
			return 0;
		else
			return 1;
	}

	/**
	 * Returns a human-readable representation of the {@link TimeInterval}
	 * without the day of the week, in the form "HH:MM–HH:MM".
	 * 
	 * @return string representation of {@link TimeInterval} without day of
	 *         week.
	 */
	public String timeString() {
		return getStartHourOfDay() + ":" + make2Digits(getStartMinuteOfHour())
				+ "–" + getEndHourOfDay() + ":"
				+ make2Digits(getEndMinuteOfHour());
	}

	/**
	 * This class is now immutable, so this method will be deleted. Instead,
	 * specify all values at construction. And use the enumeration
	 * {@link DayOfWeek}, not a string.
	 * 
	 * @param arg
	 *                string representation of day.
	 */
	@Deprecated
	public void setDayOfWeek(String arg) {
		throw new RuntimeException("deprecated");
	}

	/**
	 * Returns a human-readable representation of this {@link TimeInterval}, in
	 * the form "DayOfWeek hh:mm–hh:mm", with a possible appendage if the end
	 * time is the next day.
	 * 
	 * @return a String representation of the {@link TimeInterval}.
	 */
	public String toString() {
		DayOfWeek startDay = getStartDayOfWeek(), endDay = getEndDayOfWeek();
		String result = shortName(startDay) + " " + timeString();

		if (!startDay.equals(endDay))
			result += " (" + shortName(endDay) + ")";
		return result;
	}

	/**
	 * This method is a override for {@link java.lang.Object#equals(Object)}.
	 * 
	 * @return the boolean value, TRUE if these two objects are equal or in the
	 *         second if statement their start times and durations are the same,
	 *         FALSE if this object is not TimeInterval type or in the second if
	 *         statement their start times or durations are not the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof TimeInterval) {
			TimeInterval that = (TimeInterval) obj;

			return this.start == that.start && this.duration == that.duration;
		}
		return false;
	}

	/**
	 * This method is a override for {@link java.lang.Object#hashCode()}.
	 * 
	 * @return an int value representing the hash code defined by ourself.
	 */
	@Override
	public int hashCode() {
		return start * 1000000 + duration;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * The order is determined by first comparing start times. If start times
	 * are equal, the end times are used to break the tie.
	 * </p>
	 */
	@Override
	public int compareTo(TimeInterval that) {
		int c = compareStart(that);

		if (c != 0)
			return c;
		return compareEnd(that);
	}
	

	
}
/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.feeder.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Timestamp {

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd'T'HH:mm:ssX";
	private static final int millisPerDay = 1000 * 60 * 60 * 24;
	private short year = Short.MIN_VALUE;
	private byte month = Byte.MIN_VALUE;
	private byte day = Byte.MIN_VALUE;
	private byte hour = Byte.MIN_VALUE;
	private byte minute = Byte.MIN_VALUE;
	private byte seconds = Byte.MIN_VALUE;
	private byte timezone = Byte.MIN_VALUE;

	@Override
	public String toString() {
		// yyyy-mm-ddThh:mm:ss+hh:mm+zz:zz => 31 chars
		final StringBuffer ts = new StringBuffer(31);
		if (year != Short.MIN_VALUE) {
			ts.append(year);
			if (month != Byte.MIN_VALUE) {
				ts.append("-");
			}
		}
		if (month != Byte.MIN_VALUE) {
			ts.append(month<10?"0"+month:month);
			if (day != Byte.MIN_VALUE) {
				ts.append("-");
			}
		}
		if (day != Byte.MIN_VALUE) {
			ts.append(day<10?"0"+day:day);
		}
		if ( (year != Short.MIN_VALUE || month != Byte.MIN_VALUE || day != Byte.MIN_VALUE )
				&& (hour != Byte.MIN_VALUE || minute != Byte.MIN_VALUE || seconds != Byte.MIN_VALUE)) {
			ts.append("T");
		}
		if (hour != Byte.MIN_VALUE) {
			ts.append(hour<10?"0"+hour:hour);
			if (minute != Byte.MIN_VALUE) {
				ts.append(":");
			}
		}
		if (minute != Byte.MIN_VALUE) {
			ts.append( (minute<10?"0"+minute:minute)+":");
		} else if (hour != Byte.MIN_VALUE) {
			ts.append("00:");
		}
		if (seconds != Byte.MIN_VALUE ) {
			ts.append(seconds<10?"0"+seconds:seconds);
		} else if (minute != Byte.MIN_VALUE && hour != Byte.MIN_VALUE) {
			ts.append("00");
		}
		if (timezone != Byte.MIN_VALUE &&
				(hour != Byte.MIN_VALUE || minute != Byte.MIN_VALUE || seconds != Byte.MIN_VALUE)) {
			ts.append(convertTimeZone(timezone));
		}
		return ts.toString();
	}

	private String convertTimeZone(final int timeZone) {
		if (timeZone >= 0) {
			if (timeZone >= 10) {
				return "+" + timeZone + ":00";
			} else {
				return "+0" + timeZone + ":00";
			}
		} else {
			if (timeZone <= -10) {
				return timeZone + ":00";
			} else {
				return "-0" + Math.abs(timeZone) + ":00";
			}
		}
	}

	public Timestamp set(final long dateToSet) {
		final Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(dateToSet);
		year = (short) cal.get(Calendar.YEAR);
		month = (byte) (cal.get(Calendar.MONTH)+1);
		day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		hour = (byte) cal.get(Calendar.HOUR_OF_DAY);
		minute = (byte) cal.get(Calendar.MINUTE);
		seconds = (byte) cal.get(Calendar.SECOND);
		timezone = (byte) (cal.getTimeZone().getOffset(dateToSet)/3600000);
		return this;
	}

	/**
	 * @param fileName the filename that might contain additional information
	 * 			for the {@link Timestamp}
	 * @param regExToExtractFileInfo
	 * @param dateInfoPattern
	 * @throws ParseException in the case the filename could not be parsed using
	 * 			the Datafile attributes "regExDateInfoInFileName" and
	 * 			"DateInfoPattern".
	 * @throws PatternSyntaxException in the case of not being able to parse the
	 * 			value of the Datafile attribute "regExDateInfoInFileName".
	 * @throws IndexOutOfBoundsException in the case of no group is found using
	 * 			the value of the Datafile attribute "regExDateInfoInFileName".
	 */
	public Timestamp enrichByFilename(
			final String fileName,
			final String regExToExtractFileInfo,
			final String dateInfoPattern)
					throws ParseException {
		if (fileName == null || fileName.isEmpty() ||
				regExToExtractFileInfo == null || regExToExtractFileInfo.isEmpty() ||
				dateInfoPattern == null || dateInfoPattern.isEmpty()) {
			return this;
		}
		final Pattern pattern = Pattern.compile(regExToExtractFileInfo);
		final Matcher matcher = pattern.matcher(fileName);
		if (matcher.matches()) {
			final SimpleDateFormat sdf = new SimpleDateFormat(dateInfoPattern);
			final String dateInformation = matcher.group(1);
			final GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(sdf.parse(dateInformation));

			if (dateInfoPattern.indexOf("y") > -1) {
				setYear(Short.parseShort(Integer.toString(cal.get(GregorianCalendar.YEAR))));
			}
			if (dateInfoPattern.indexOf("M") > -1) {
				setMonth(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.MONTH)+1)));
			}
			if (dateInfoPattern.indexOf("d") > -1) {
				setDay(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.DAY_OF_MONTH))));
			}
			if (dateInfoPattern.indexOf("H") > -1) {
				setHour(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.HOUR_OF_DAY))));
			}
			if (dateInfoPattern.indexOf("m") > -1) {
				setMinute(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.MINUTE))));
			}
			if (dateInfoPattern.indexOf("s") > -1) {
				setSeconds(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.SECOND))));
			}
			if (dateInfoPattern.indexOf("z") > -1 || dateInfoPattern.indexOf("Z") > -1) {
				setTimezone(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.ZONE_OFFSET))));
			}
		}
		return this;
	}

	protected Date toDate() {
		try {
			return new SimpleDateFormat(DEFAULT_PATTERN).parse(toString());
		} catch (final ParseException e) {
			throw new RuntimeException("Could not execute toDate()",e);
		}
	}

	public boolean after(final Timestamp timeStamp) {
		if (timeStamp == null) {
			throw new IllegalArgumentException("parameter timeStamp is mandatory.");
		}
		return toDate().after(timeStamp.toDate());
	}

	public boolean before(final Timestamp timeStamp) {
		if (timeStamp == null) {
			throw new IllegalArgumentException("parameter timeStamp is mandatory.");
		}
		return toDate().before(timeStamp.toDate());
	}

	/**
	 * @param lastModified long
	 * @param lastModifiedDelta -1, if it should be ignored, else > 0.
	 */
	public Timestamp enrichByFileModificationDate(long lastModified,
			final int lastModifiedDelta) {
		final GregorianCalendar cal = new GregorianCalendar();
		if (lastModifiedDelta > 0) {
			lastModified = lastModified - (lastModifiedDelta * millisPerDay);
		}
		cal.setTime(new Date(lastModified));
		setYear(Short.parseShort(Integer.toString(cal.get(GregorianCalendar.YEAR))));
		setMonth(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.MONTH)+1)));
		setDay(Byte.parseByte(Integer.toString(cal.get(GregorianCalendar.DAY_OF_MONTH))));
		return this;
	}

	public void setYear(final short year) {
		this.year = year;
	}

	public void setMonth(final byte month) {
		this.month = month;
	}

	public void setDay(final byte day) {
		this.day = day;
	}

	public void setHour(final byte hour) {
		this.hour = hour;
	}

	public void setMinute(final byte minute) {
		this.minute = minute;
	}

	public void setSeconds(final byte seconds) {
		this.seconds = seconds;
	}

	public void setTimezone(final byte timezone) {
		this.timezone = timezone;
	}

	public short getYear() {
		return year;
	}

	public byte getMonth() {
		return month;
	}

	public byte getDay() {
		return day;
	}

	public byte getHour() {
		return hour;
	}

	public byte getMinute() {
		return minute;
	}

	public byte getSeconds() {
		return seconds;
	}

	public byte getTimezone() {
		return timezone;
	}
}

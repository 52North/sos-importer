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

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @author Raimund
 */
public class Timestamp {
	
	private short year;
	private byte month;
	private byte day;
	private byte hour;
	private byte minute;
	private byte seconds;
	private byte timezone;
	
	
	
	@Override
	public String toString() {
		return String.format("%s-%s-%sT%s:%s:%s%s]",
				year,
				(month<10?"0"+month:month),
				(day<10?"0"+day:day),
				(hour<10?"0"+hour:hour),
				(minute<10?"0"+minute:minute),
				(seconds<10?"0"+seconds:seconds),
				convertTimeZone(timezone));
	}
	
	private String convertTimeZone(int timeZone) {
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

	public void setYear(short year) {
		this.year = year;
	}

	public void setMonth(byte month) {
		this.month = month;
	}

	public void setDay(byte day) {
		this.day = day;
	}

	public void setHour(byte hour) {
		this.hour = hour;
	}

	public void setMinute(byte minute) {
		this.minute = minute;
	}

	public void setSeconds(byte seconds) {
		this.seconds = seconds;
	}

	public void setTimezone(byte timezone) {
		this.timezone = timezone;
	}

}

/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.model.dateAndTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.n52.sos.importer.model.Combination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateAndTime extends Combination {
	
	private static final Logger logger = LoggerFactory.getLogger(DateAndTime.class);
	
	private Year year;
	private Month month;
	private Day day;
	private Hour hour;
	private Minute minute;
	private Second second;
	private TimeZone timeZone;
	
	public Year getYear() {
		return year;
	}
	public void setYear(final Year year) {
		if (getGroup() != null) {
			if (year != null) {
				logger.info("Add " + year + " to " + this);
			} else {
				logger.info("Remove " + this.year + " from " + this);
			}
		}
		this.year = year;
	}
	public Month getMonth() {
		return month;
	}
	public void setMonth(final Month month) {
		if (getGroup() != null) {
			if (month != null) {
				logger.info("Add " + month + " to " + this);
			} else {
				logger.info("Remove " + this.month + " from " + this);
			}
		}
		this.month = month;
	}
	public Day getDay() {
		return day;
	}
	public void setDay(final Day day) {
		if (getGroup() != null) {
			if (day != null) {
				logger.info("Add " + day + " to " + this);
			} else {
				logger.info("Remove " + this.day + " from " + this);
			}
		}
		this.day = day;
	}
	public Hour getHour() {
		return hour;
	}
	public void setHour(final Hour hour) {
		if (getGroup() != null) {
			if (hour != null) {
				logger.info("Add " + hour + " to " + this);
			} else {
				logger.info("Remove " + this.hour + " from " + this);
			}
		}
		this.hour = hour;
	}
	public Minute getMinute() {
		return minute;
	}
	public void setMinute(final Minute minute) {
		if (getGroup() != null) {
			if (minute != null) {
				logger.info("Add " + minute + " to " + this);
			} else {
				logger.info("Remove " + this.minute + " from " + this);
			}
		}
		this.minute = minute;
	}
	public Second getSeconds() {
		return second;
	}
	public void setSecond(final Second second) {
		if (getGroup() != null) {
			if (second != null) {
				logger.info("Add " + second + " to " + this);
			} else {
				logger.info("Remove " + this.second + " from " + this);
			}
		}
		this.second = second;
	}
	public TimeZone getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(final TimeZone timeZone) {
		if (getGroup() != null) {
			if (timeZone != null) {
				logger.info("Add " + timeZone + " to " + this);
			} else {
				logger.info("Remove " + this.timeZone + " from " + this);
			}
		}
		this.timeZone = timeZone;
	}	
	
	@Override
	public Object parse(final String s) {
		Date dateTime = null;
		final String currentPattern = getPattern();
		final SimpleDateFormat formatter =
	           new SimpleDateFormat(currentPattern);      	
        try {
        	dateTime = formatter.parse(s);
		} catch (final ParseException e) {
			throw new NumberFormatException(e.getLocalizedMessage());
		}
		return dateTime;
	}
	
	@Override
	public String format(final Object o) {
        final Date date = (Date)o;		        
    	final SimpleDateFormat formatter =
	           new SimpleDateFormat(getPattern());      	
        final String dateString = formatter.format(date);

		return dateString;
	}
	@Override
	public String toString() {
		if (getGroup() == null) {
			return "Date and Time(" + year + ", " + month + ", "
					+ day + ", " + hour + ", " + minute + ", "
					+ second + ", " + timeZone + ")";
		} else {
			return "Date&Time group " + getGroup();
		}
	}
}

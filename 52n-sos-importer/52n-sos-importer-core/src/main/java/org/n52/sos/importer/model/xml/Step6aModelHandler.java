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
package org.n52.sos.importer.model.xml;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x02.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x02.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Called in the case of not having any date information in the file
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step6aModelHandler implements ModelHandler<Step6aModel> {

	private static final Logger logger = Logger.getLogger(Step6aModelHandler.class);
	
	@Override
	public void handleModel(Step6aModel stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		/*
		 *  we have a date time object and need to save it in a Metadata element
		 *  with key Key.TIME
		 *  
		 *  LOCAL FIELDS
		 */
		DateAndTime dAT;
		String timeStamp;
		Enum key = Key.TIME;
		//
		dAT = stepModel.getDateAndTime();
		timeStamp = getTimeStampFromDateAndTime(dAT);
		/*
		 * check if metadata with Key.TIME already exists
		 * if yes -> update
		 * else -> create new element
		 */
		AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
		if(addiMeta == null) {
			addiMeta = sosImportConf.addNewAdditionalMetadata();
		}
		// get metadata array and check for Key.TIME
		if(addOrUpdateMetadata(key, timeStamp, addiMeta)) {
			if (logger.isInfoEnabled()) {
				logger.info("Timestamp in additional metadata updated/added: "
						+ timeStamp);
			}
		}else {
			logger.fatal("Timestamp element could not be updated");
		}
	}

	private boolean addOrUpdateMetadata(Enum key, 
			String value,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("addOrUpdateMetadata()");
		}
		Metadata[] metaElems = addiMeta.getMetadataArray();
		Metadata meta = null;
		String addedOrUpdated = "Updated";
		// check if there is already a element with the given key
		for (Metadata metadata : metaElems) {
			if (metadata.getKey().equals(key) ) {
				meta = metadata;
				break;
			}
		}
		if(meta == null) {
			meta = addiMeta.addNewMetadata();
			meta.setKey(key);
			addedOrUpdated = "Added";
		}
		meta.setValue(value);
		if (logger.isDebugEnabled()) {
			logger.debug(addedOrUpdated + 
					" additional metadata. Key: " + 
					key + "; " +
					"Value: " + 
					value);
		}
		return (meta.getValue().equalsIgnoreCase(value));
	}

	/**
	 * Check for each date element is the given <code>DateAndTime</code> object
	 * and create final string for the time stamp using this rule:<br/>
	 * IF
	 * <ul><li><b>null</b> -> add default value to String</li>
	 * <li><b>else</b> -> add value to String</li></ul>
	 * Used format: <code>"yyyy-MM-dd'T'HH:mm:ssZ"</code><br/>
	 * The colon bug is fixed, so the time zone looks like "<code>+02:00</code>" for example.<br/>
	 * Default value is: "<code>1970-01-01T00:00:00:+00:00</code>"
	 * @param dAT
	 * @return a String or null, if dAT is null.
	 */
	private String getTimeStampFromDateAndTime(DateAndTime dAT) {
		if (logger.isTraceEnabled()) {
			logger.trace("getTimeStampFromDateAndTime()");
		}
		if(dAT == null) {
			return null;
		}
		/*
		 * 	LOCAL FIELDS
		 */
		GregorianCalendar cal;
		int year = 1970, 
				month = 0, 
				dayOfMonth = 1, 
				hourOfDay = 0, 
				minute = 0, 
				seconds = 0,
				timezone = 0;
		String timeStamp, timeZoneString, sign;
		SimpleDateFormat format;
		TimeZone tz;
		/*
		 * 	YEAR
		 */
		if(dAT.getYear() != null) {
			if(dAT.getYear().getValue() != Integer.MIN_VALUE) {
				year = dAT.getYear().getValue();
			}
		}
		/*
		 * 	MONTH
		 */
		if(dAT.getMonth() != null) {
			if(dAT.getMonth().getValue() != Integer.MIN_VALUE) {
				month = dAT.getMonth().getValue();
			}
		}
		/*
		 * DAY OF MONTH
		 */
		if(dAT.getDay() != null) {
			if(dAT.getDay().getValue() != Integer.MIN_VALUE) {
				dayOfMonth = dAT.getDay().getValue();
			}
		}
		/*
		 * HOUR OF DAY
		 */
		if(dAT.getHour() != null) {
			if(dAT.getHour().getValue() != Integer.MIN_VALUE) {
				hourOfDay = dAT.getHour().getValue();
			}
		}
		/*
		 * 	MINUTE
		 */
		if(dAT.getMinute() != null) {
			if(dAT.getMinute().getValue() != Integer.MIN_VALUE) {
				minute = dAT.getMinute().getValue();
			}
		}
		/*
		 * SECONDS
		 */
		if(dAT.getSeconds() != null) {
			if(dAT.getSeconds().getValue() != Integer.MIN_VALUE) {
				seconds = dAT.getSeconds().getValue();
			}
		}
		/*
		 * 	TIMEZONE
		 */
		if(dAT.getTimeZone() != null) {
			if(dAT.getTimeZone().getValue() != Integer.MIN_VALUE) {
				timezone = dAT.getTimeZone().getValue();
			}
		}
		// Get right timezone
		timeZoneString = "GMT";
		sign = "+";
		if(timezone <= 0) {
			sign = "";
		}
		timeZoneString = timeZoneString + sign + timezone;
		if (logger.isDebugEnabled()) {
			logger.debug("timeZoneString: " + timeZoneString);
		}
		tz = TimeZone.getTimeZone(timeZoneString);
		if (logger.isDebugEnabled()) {
			logger.debug("TimeZone: " + tz);
		}
		cal = new GregorianCalendar(tz);
		cal.set(GregorianCalendar.YEAR, year);
		cal.set(GregorianCalendar.MONTH, month);
		cal.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth);
		cal.set(GregorianCalendar.HOUR_OF_DAY, hourOfDay);
		cal.set(GregorianCalendar.MINUTE, minute);
		cal.set(GregorianCalendar.SECOND, seconds);
		format = new SimpleDateFormat(Constants.DATE_FORMAT_STRING);
		format.setTimeZone(tz);
		timeStamp = format.format(cal.getTime());
		/*
		 * 	FIX colon bug
		 */
		timeStamp = timeStamp.substring(0, timeStamp.length()-2) + ":" + timeStamp.substring(timeStamp.length()-2);
		return timeStamp;
	}


}

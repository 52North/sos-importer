/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.model.xml;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6aModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Called in the case of not having any date information in the file
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Step6aModelHandler implements ModelHandler<Step6aModel> {

    private static final Logger logger = LoggerFactory.getLogger(Step6aModelHandler.class);

    /** {@inheritDoc} */
    @Override
    public void handleModel(final Step6aModel stepModel,
            final SosImportConfiguration sosImportConf) {
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
        final Enum key = Key.TIME;
        //
        dAT = stepModel.getDateAndTime();
        timeStamp = getTimeStampFromDateAndTime(dAT);
        /*
         * check if metadata with Key.TIME already exists
         * if yes -> update
         * else -> create new element
         */
        AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
        if (addiMeta == null) {
            addiMeta = sosImportConf.addNewAdditionalMetadata();
        }
        // get metadata array and check for Key.TIME
        if (addOrUpdateMetadata(key, timeStamp, addiMeta)) {
            if (logger.isInfoEnabled()) {
                logger.info("Timestamp in additional metadata updated/added: "
                        + timeStamp);
            }
        } else {
            logger.error("Timestamp element could not be updated");
        }
    }

    private boolean addOrUpdateMetadata(final Enum key,
            final String value,
            final AdditionalMetadata addiMeta) {
        if (logger.isTraceEnabled()) {
            logger.trace("addOrUpdateMetadata()");
        }
        final Metadata[] metaElems = addiMeta.getMetadataArray();
        Metadata meta = null;
        String addedOrUpdated = "Updated";
        // check if there is already a element with the given key
        for (final Metadata metadata : metaElems) {
            if (metadata.getKey().equals(key)) {
                meta = metadata;
                break;
            }
        }
        if (meta == null) {
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
        return meta.getValue().equalsIgnoreCase(value);
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
     * @return a String or null, if dAT is null.
     */
    private String getTimeStampFromDateAndTime(final DateAndTime dAT) {
        if (logger.isTraceEnabled()) {
            logger.trace("getTimeStampFromDateAndTime()");
        }
        if (dAT == null) {
            return null;
        }
        /*
         *  LOCAL FIELDS
         */
        GregorianCalendar cal;
        int year = 1970;
        int month = 0;
        int dayOfMonth = 1;
        int hourOfDay = 0;
        int minute = 0;
        int seconds = 0;
        int timezone = 0;
        String timeStamp;
        String timeZoneString;
        String sign;
        SimpleDateFormat format;
        TimeZone tz;
        /*
         *  YEAR
         */
        if (dAT.getYear() != null) {
            if (dAT.getYear().getValue() != Integer.MIN_VALUE) {
                year = dAT.getYear().getValue();
            }
        }
        /*
         *  MONTH
         */
        if (dAT.getMonth() != null) {
            if (dAT.getMonth().getValue() != Integer.MIN_VALUE) {
                month = dAT.getMonth().getValue();
            }
        }
        /*
         * DAY OF MONTH
         */
        if (dAT.getDay() != null) {
            if (dAT.getDay().getValue() != Integer.MIN_VALUE) {
                dayOfMonth = dAT.getDay().getValue();
            }
        }
        /*
         * HOUR OF DAY
         */
        if (dAT.getHour() != null) {
            if (dAT.getHour().getValue() != Integer.MIN_VALUE) {
                hourOfDay = dAT.getHour().getValue();
            }
        }
        /*
         *  MINUTE
         */
        if (dAT.getMinute() != null) {
            if (dAT.getMinute().getValue() != Integer.MIN_VALUE) {
                minute = dAT.getMinute().getValue();
            }
        }
        /*
         * SECONDS
         */
        if (dAT.getSeconds() != null) {
            if (dAT.getSeconds().getValue() != Integer.MIN_VALUE) {
                seconds = dAT.getSeconds().getValue();
            }
        }
        /*
         *  TIMEZONE
         */
        if (dAT.getTimeZone() != null) {
            if (dAT.getTimeZone().getValue() != Integer.MIN_VALUE) {
                timezone = dAT.getTimeZone().getValue();
            }
        }
        // Get right timezone
        timeZoneString = "GMT";
        sign = "+";
        if (timezone <= 0) {
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
         *  FIX colon bug
         */
        timeStamp = timeStamp.substring(0, timeStamp.length() - 2) + ":" + timeStamp.substring(timeStamp.length() - 2);
        return timeStamp;
    }


}

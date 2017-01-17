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
package org.n52.sos.importer.feeder.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TimeZone;

import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Preconditions:
 * <ul><li>Time is in UTC</li>
 * <li>Date is encoded with MM/dd/yy</li>
 * <li>Time is encoded with HH:mm:ss</li>
 * <li>Each import run requires a NEW file</li></ul>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class NSAMParser implements CsvParser {

	/*
	 * This values is derived from "mm/dd/yy"
	 */
    private static final int dateStringLength = 8;

	private static final int dataOffset = 21;

    private static final int metaDataOffset = 3;

    private static final Logger LOG = LoggerFactory.getLogger(NSAMParser.class);

    private static final int startTimeBeginIndex = 11;

    private static final int startTimeEndIndex = 19;

    private static final int metaDatabeginIndex = 23;

    private static final int metaDataLength = 8;

    private static final int metaDataTimebeginIndex = 11;

    private static final int metaDataTimeLength = metaDataLength;

    private static final String metadataSplitter = ",,,,,";

    private static String timeSeriesSplitter = ",,,";


    final Stack<String[]> lines = new Stack<>();


    /** {@inheritDoc} */
    @Override
    public String[] readNext() throws IOException {
        if (lines.empty()) {
            return null;
        }
        return lines.pop();
    }

    /** {@inheritDoc} */
    @Override
    public void init(final BufferedReader br,
            final Configuration configuration) throws IOException {
        // 1 read file metadata
        skipLines(br,metaDataOffset);
        // 1.1 start date => # of timeseries (columns)
		final String startDateLine = br.readLine();
        final String[] startDates = getStartDates(startDateLine);
        // 1.2 start time pro zeitreihe einlesen
        String startTimeLine = br.readLine();
        final String startTime = startTimeLine.substring(metaDataTimebeginIndex,metaDataTimebeginIndex+metaDataTimeLength);
        // 2 read data
        skipLines(br,dataOffset);
        // 2.1 split each data line into time series
        String line;
        final List<List<String[]>> timeSeriesBuffer = new LinkedList<>();
        for (@SuppressWarnings("unused") final String startDate : startDates) {
            timeSeriesBuffer.add(new LinkedList<String[]>());
        }
        final String timeSeriesElementSplitter = new String(new char[] {configuration.getCsvSeparator()});
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        final TimeZone timeZone = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(timeZone);
        boolean firstLine = true;
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            if (firstLine) {
            	firstLine = false;
            	if (hasMoreThanOneTimeseries(startDates)) {
            		createTimeSeriesSplitter(line);
            	} else {
            		LOG.debug("Only one time series in dataset found.");
            	}
            }
            int i = 0;
            for (final String timeSeriesElem : line.split(timeSeriesSplitter)) {
            	if (timeSeriesElem != null && !timeSeriesElem.isEmpty()) {
            		final String[] timeSeriesElemTokens = timeSeriesElem.split(timeSeriesElementSplitter);
            		if (timeSeriesElemTokens != null && timeSeriesElemTokens.length > 0 && timeSeriesElemTokens[0].indexOf("Comment for Sample") == -1) {
            			try {
            				final Timestamp timestamp = getTimestamp(startDates[i],
            						startTime, sdf, timeZone, timeSeriesElemTokens[0]);
            				timeSeriesBuffer.get(i).add(new String[] {timestamp.toString(), timeSeriesElemTokens[1]});
            				i++;
            			} catch (NumberFormatException | ParseException e) {
            				LOG.error("Exception thrown: {}", e.getMessage(), e);
            			}
            		}
            	}
            }
        }
        // 3 create line Stack<String[]> from timeSeriesBuffer
        createStack(timeSeriesBuffer);
    }

	private boolean hasMoreThanOneTimeseries(final String[] startDates) {
		return startDates.length > 1;
	}

    private void createTimeSeriesSplitter(String line) {
    	LOG.debug("Identify time series splitter from first line of data: '{}'", line);
    	boolean startFound = false;
    	int splitterCount = 1;
    	for (final String elem : line.split(",")) {
    			if (elem.isEmpty()) {
    				startFound = true;
    				splitterCount++;
    			}
    	        if (startFound && !elem.isEmpty()){
    	        	break;
    	        }
    	}
    	if (splitterCount > 1) {
    		timeSeriesSplitter = "";
    		while (splitterCount > 0) {
    			timeSeriesSplitter += ",";
    			splitterCount--;
    		}
    		LOG.debug("Created timeseries splitter: {}", timeSeriesSplitter);
    	} else {
    		LOG.debug("Splitter count == 1 => only one timeseries in dataset.");
    	}
	}

	private Timestamp getTimestamp(final String startDate,
            final String startTime,
            final SimpleDateFormat sdf,
            final TimeZone timeZone,
            final String secondsElapsed) throws ParseException {
        final Timestamp timestamp = new Timestamp();
        final String timeString = startDate + " " + startTime;
        final Date parsedDate = sdf.parse(timeString);
        final GregorianCalendar cal = new GregorianCalendar(timeZone);
        cal.setTime(parsedDate);
        final long newMillis = cal.getTimeInMillis() + Integer.parseInt(secondsElapsed) * 1000;
        cal.setTimeInMillis(newMillis);
        timestamp.setYear((short) cal.get(GregorianCalendar.YEAR));
        timestamp.setMonth((byte) (cal.get(GregorianCalendar.MONTH)+1));
        timestamp.setDay((byte) cal.get(GregorianCalendar.DAY_OF_MONTH));
        timestamp.setHour((byte) cal.get(GregorianCalendar.HOUR_OF_DAY));
        timestamp.setMinute((byte) cal.get(GregorianCalendar.MINUTE));
        timestamp.setSeconds((byte) cal.get(GregorianCalendar.SECOND));
        timestamp.setTimezone((byte)0);
        return timestamp;
    }

    private String[] getStartDates(final String startDateLine) {
    	LOG.debug("Parsing startDateLine: '{}'", startDateLine);
        String[] startDates = null;
        if (startDateLine.length() > metaDatabeginIndex) {
        		startDates = startDateLine.substring(metaDatabeginIndex).split(metadataSplitter);
        		final ArrayList<String> startDatesTmp = new ArrayList<>(startDates.length+1);
        		LOG.debug("StartDate first split: {}", Arrays.toString(startDates));
        		startDatesTmp.add(startDateLine.substring(startTimeBeginIndex,startTimeEndIndex));
        		for (String startDate : startDates) {
        			if (!startDate.isEmpty()) {
        				// Case: Leftover ","
        				if (startDate.indexOf(",") != -1) {
        					startDate = startDate.replaceAll(",", "");
        				}
        				// Case: Wrong date format like 12/31/2015 and not 12/31/15
        				if (startDate.length() == dateStringLength+2) {
        					startDate = startDate.substring(0,startDate.lastIndexOf("/")+1).concat(startDate.substring(startDate.lastIndexOf("/")+3));
        				}
        				if (startDate.length()==dateStringLength){
        					startDatesTmp.add(startDate);
        				}
        			}
        		}
        		startDates = startDatesTmp.toArray(new String[startDatesTmp.size()]);
    	} else {
    		startDates = new String[1];
    		startDates[0] = startDateLine.substring(startTimeBeginIndex,startTimeEndIndex);
    	}
        LOG.debug("Start dates found: {}",Arrays.toString(startDates));
        return startDates;
    }

    private void createStack(final List<List<String[]>> timeSeriesBuffer) {
        Collections.reverse(timeSeriesBuffer);
        for (final List<String[]> timeSeries : timeSeriesBuffer) {
            Collections.reverse(timeSeries);
            for (final String[] strings : timeSeries) {
                lines.push(strings);
            }
        }
    }

    private void skipLines(final BufferedReader br,
            int i) throws IOException {
        while(i-->0) {
            br.readLine();
        }
    }

    /*
     * (non-Javadoc)
     * @see org.n52.sos.importer.feeder.csv.CsvParser#getSkipLimit()
     *
     * Skip limit is 1 because the "lines" are artificial in this parser and num of lines == num of observations.
     */
	/** {@inheritDoc} */
	@Override
	public int getSkipLimit() {
		return 1;
	}

}

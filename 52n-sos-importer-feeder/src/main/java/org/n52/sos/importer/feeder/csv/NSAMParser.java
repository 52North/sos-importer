/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.importer.feeder.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 */
public class NSAMParser implements CsvParser {

    private static final int dataOffset = 21;

    private static final int metaDataOffset = 3;

    private static final Logger LOG = LoggerFactory.getLogger(NSAMParser.class);

    private static final int startTimeBeginIndex = 11;

    private static final int startTimeEndIndex = 19;

    private static final int metaDatabeginIndex = 23;

    private static final String metadataSplitter = ",,,,,";

    private static final String timeSeriesSplitter = ",,,";

    private static final int metaDataLength = 8;

    final Stack<String[]> lines = new Stack<>();

    @Override
    public String[] readNext() throws IOException {
        if (lines.empty()) {
            return null;
        }
        return lines.pop();
    }

    @Override
    public void init(final BufferedReader br,
            final Configuration configuration) throws IOException {
        // 1 read file metadata
        skipLines(br,metaDataOffset);
        // 1.1 start date => # of timeseries (columns)
        final String startDateLine = br.readLine();
        final String[] startDates = getStartDates(startDateLine);
        // 1.2 start time pro zeitreihe einlesen
        final String startTime = br.readLine().substring(metaDatabeginIndex,metaDatabeginIndex+metaDataLength);
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
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            int i = 0;
            for (final String timeSeriesElem : line.split(timeSeriesSplitter)) {
                final String[] timeSeriesElemTokens = timeSeriesElem.split(timeSeriesElementSplitter);
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
        // 3 create line Stack<String[]> from timeSeriesBuffer
        createStack(timeSeriesBuffer);
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
        String[] startDates = startDateLine.substring(metaDatabeginIndex).split(metadataSplitter);
        final ArrayList<String> startDatesTmp = new ArrayList<>(startDates.length+1);
        startDatesTmp.add(startDateLine.substring(startTimeBeginIndex,startTimeEndIndex));
        for (final String startDate : startDates) {
            startDatesTmp.add(startDate);
        }
        startDates = startDatesTmp.toArray(new String[startDatesTmp.size()]);
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

}

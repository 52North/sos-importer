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
package org.n52.sos.importer.feeder.model;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.opengis.swe.x20.DataArrayDocument;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataArrayType.ElementType;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.DataRecordType.Field;
import net.opengis.swe.x20.QuantityType;
import net.opengis.swe.x20.TextEncodingType;
import net.opengis.swe.x20.TimeType;

import org.apache.xmlbeans.XmlString;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.SweArrayObservationParameters;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.oxf.xml.NcNameResolver;
import org.n52.oxf.xml.XMLConstants;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;

public class TimeSeries {

	public static final String SENSOR_ID_NOT_SET = "SENSOR_ID_NOT_SET";

	public static final ObservedProperty OBSERVED_PROPERTY_NOT_SET = new ObservedProperty("OBSERVED_PROPERTY_NOT_SET", "OBSERVED_PROPERTY_NOT_SET");

	public static final String UOM_CODE_NOT_SET = "UOM_CODE_NOT_SET";

	public static final String MV_TYPE_NOT_SET = "MV_TYPE_NOT_SET";

	public static final String SENSOR_NAME_NOT_SET = "SENSOR_NAME_NOT_SET";

	private final LinkedList<InsertObservation> timeseries = new LinkedList<>();

	private final String tokenSeparator = ";";

	private final String blockSeparator = "@";

	public boolean addObservation(final InsertObservation insertObservation) {
		return timeseries.add(insertObservation);
	}

	public String getSensorURI() {
		if (timeseries.isEmpty()) {
			return SENSOR_ID_NOT_SET;
		}
		final String sensorURI = timeseries.getFirst().getSensorURI();
		if (sensorURI == null || sensorURI.isEmpty()) {
			return SENSOR_ID_NOT_SET;
		}
		return sensorURI;
	}

	public Object getSensorName() {
		if (timeseries.isEmpty()) {
			return SENSOR_NAME_NOT_SET;
		}
		final String sensorName = timeseries.getFirst().getSensorName();
		if (sensorName == null || sensorName.isEmpty()) {
			return SENSOR_NAME_NOT_SET;
		}
		return sensorName;
	}

	public InsertObservation getFirst() {
		if (timeseries.isEmpty()) {
			return null;
		}
		return timeseries.getFirst();
	}

	public ObservedProperty getObservedProperty() {
		if (timeseries.isEmpty()) {
			return OBSERVED_PROPERTY_NOT_SET;
		}
		final ObservedProperty obsProp = timeseries.getFirst().getObservedProperty();
		if (obsProp == null) {
			return OBSERVED_PROPERTY_NOT_SET;
		}
		return obsProp;
	}

	public String getUnitOfMeasurementCode() {
		if (timeseries.isEmpty()) {
			return UOM_CODE_NOT_SET;
		}
		final String uomCode = timeseries.getFirst().getUnitOfMeasurementCode();
		if (uomCode == null || uomCode.isEmpty()) {
			return UOM_CODE_NOT_SET;
		}
		return uomCode;
	}

	public String getMeasuredValueType() {
		if (timeseries.isEmpty()) {
			return MV_TYPE_NOT_SET;
		}
		final String mVType = timeseries.getFirst().getMeasuredValueType();
		if (mVType == null || mVType.isEmpty()) {
			return MV_TYPE_NOT_SET;
		}
		return mVType;
	}

	public InsertObservationParameters getSweArrayObservation(final String sosVersion) {
		final SweArrayObservationParameters obsParameter = new SweArrayObservationParameters();
		// add extension
		obsParameter.addExtension("<swe:Boolean xmlns:swe=\"http://www.opengis.net/swe/2.0\" definition=\"SplitDataArrayIntoObservations\"><swe:value>true</swe:value></swe:Boolean>");
			// OM_Observation

    		// procedure
		obsParameter.addProcedure(getSensorURI());
    		// obsProp
		obsParameter.addObservedProperty(getObservedProperty().getUri());
    		// feature
		addFeature(obsParameter);
			// result
    	addResult(obsParameter);
		if (sosVersion.equalsIgnoreCase("2.0.0")) {
			obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + getFirst().getEpsgCode());
			// phentime
			obsParameter.addPhenomenonTime(getPhenomenonTime());
			// temporal bbox for result time
			obsParameter.addResultTime(getResultTime());
			return new org.n52.oxf.sos.request.v200.InsertObservationParameters(obsParameter, Collections.singletonList(getFirst().getOffering().getUri()));
		}

		obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + getFirst().getEpsgCode());
		obsParameter.addSamplingTime(getPhenomenonTime());
		return new org.n52.oxf.sos.request.v100.InsertObservationParameters(obsParameter);
	}

	private void addResult(final SweArrayObservationParameters obsParameter) {
		final DataArrayDocument xbDataArrayDoc = DataArrayDocument.Factory.newInstance();
		final DataArrayType xbDataArray = xbDataArrayDoc.addNewDataArray1();
		// count
		xbDataArray.addNewElementCount().addNewCount().setValue(BigInteger.valueOf(timeseries.size()));
		// element type
		final DataRecordType xbDataRecord = DataRecordType.Factory.newInstance();
		// phentime
		final Field xbPhenTime = xbDataRecord.addNewField();
		xbPhenTime.setName("phenomenonTime");
		final TimeType xbTimeWithUom = TimeType.Factory.newInstance();
		xbTimeWithUom.setDefinition("http://www.opengis.net/def/property/OGC/0/PhenomenonTime");
		xbTimeWithUom.addNewUom().setHref("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");
		xbPhenTime.addNewAbstractDataComponent().set(xbTimeWithUom);
		xbPhenTime
			.getAbstractDataComponent()
			.substitute(XMLConstants.QN_SWE_2_0_TIME, TimeType.type);
		// obsProp
		final Field xbObsProperty = xbDataRecord.addNewField();
		xbObsProperty.setName(NcNameResolver.fixNcName(getObservedProperty().getName()));
		final QuantityType xbQuantityWithUom = QuantityType.Factory.newInstance();
		xbQuantityWithUom.setDefinition(getObservedProperty().getUri());
		xbQuantityWithUom.addNewUom().setCode(getUnitOfMeasurementCode());
		xbObsProperty.addNewAbstractDataComponent().set(xbQuantityWithUom);
		xbObsProperty
			.getAbstractDataComponent()
			.substitute(XMLConstants.QN_SWE_2_0_QUANTITY, QuantityType.type);

		final ElementType xbElementType = xbDataArray.addNewElementType();
		xbElementType.setName("definition");
		xbElementType.addNewAbstractDataComponent().set(xbDataRecord);
		xbElementType
			.getAbstractDataComponent()
			.substitute(XMLConstants.QN_SWE_2_0_DATA_RECORD, DataRecordType.type);

		// encoding
		final TextEncodingType textEncoding = TextEncodingType.Factory.newInstance();
		// token
		textEncoding.setTokenSeparator(tokenSeparator);
		// block seperator
		textEncoding.setBlockSeparator(blockSeparator);
		xbDataArray.addNewEncoding().addNewAbstractEncoding().set(textEncoding);
		xbDataArray
			.getEncoding().getAbstractEncoding()
			.substitute(XMLConstants.QN_SWE_2_0_TEXT_ENCODING, TextEncodingType.type);

		// values
		xbDataArray.addNewValues().set(createValuesString());
		obsParameter.addObservationValue(xbDataArrayDoc.xmlText());
	}

	private XmlString createValuesString() {
		// values <-- linebreak every 100 lines?
		final StringBuilder sb = new StringBuilder();
		int counter = 0;
		for (final InsertObservation io : timeseries) {
			sb.append(io.getTimeStamp().toString());
			sb.append(tokenSeparator);
			sb.append(io.getResultValue());
			sb.append(blockSeparator);
			if (counter > 0 && counter++ % 100 == 0) {
				sb.append("\n");
			}
		}
		sb.trimToSize();
		String valueString = sb.toString();
		valueString = valueString.substring(0, valueString.lastIndexOf(blockSeparator));
		final XmlString xbValueString = XmlString.Factory.newInstance();
        xbValueString.setStringValue(valueString);
        return xbValueString;
	}

	private void addFeature(final ObservationParameters obsParameter) {
		final InsertObservation io = getFirst();
		obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
		obsParameter.addNewFoiName(io.getFeatureOfInterestName());
		obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
		// position
		boolean eastingFirst = false;
		if (Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode()) == null) {
			Configuration.EPSG_EASTING_FIRST_MAP.get("default");
		} else {
			eastingFirst = Configuration.EPSG_EASTING_FIRST_MAP.get(io.getEpsgCode());
		}
		String pos = eastingFirst?
				String.format("%s %s",
				io.getLongitudeValue(),
				io.getLatitudeValue()) :
					String.format("%s %s",
							io.getLatitudeValue(),
							io.getLongitudeValue());
		if (io.isSetAltitudeValue()) {
			pos = String.format("%s %s", pos, io.getAltitudeValue());
		}
		obsParameter.addFoiPosition(pos);
	}

	private String getResultTime() {
		Timestamp resultTime = null;
		for (final InsertObservation io : timeseries) {
			if (resultTime == null || resultTime.before(io.getTimeStamp())) {
				resultTime = io.getTimeStamp();
			}
		}
		if (resultTime == null || resultTime.toString().isEmpty()) {
			return "Could not get result time date of timeseries";
		}
		return resultTime.toString();
	}

	private String getPhenomenonTime() {
		Timestamp start = null;
		Timestamp end = null;
		for (final InsertObservation io : timeseries) {
			if (start == null || start.after(io.getTimeStamp())) {
				start = io.getTimeStamp();
			}
			if (end == null || end.before(io.getTimeStamp())) {
				end = io.getTimeStamp();
			}
		}
		if (start == null || start.toString().isEmpty() || end == null || end.toString().isEmpty()) {
			return "Could not get start and/or end date of timeseries";
		}
		return new StringBuffer(start.toString()).append("/").append(end.toString()).toString();
	}

	@Override
	public String toString() {
		return String.format("TimeSeries [sensor=%s, observedProperty=%s, feature=%s]",
				getSensorURI(),
				getObservedProperty(),
				timeseries.getFirst().getFeatureOfInterestURI());
	}

	/**
	 * @return <code>true</code>, if this time series contains no {@link InsertObservation} objects.
	 */
	public boolean isEmpty() {
		return timeseries.isEmpty();
	}

	public List<? extends InsertObservation> getInsertObservations() {
		return Collections.unmodifiableList(timeseries);
	}

}

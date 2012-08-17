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

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.Resource;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.AltDocument.Alt;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x02.GeneratedResourceType;
import org.x52North.sensorweb.sos.importer.x02.GeneratedSpatialResourceType;
import org.x52North.sensorweb.sos.importer.x02.LatDocument.Lat;
import org.x52North.sensorweb.sos.importer.x02.LongDocument.Long;
import org.x52North.sensorweb.sos.importer.x02.ManualResourceType;
import org.x52North.sensorweb.sos.importer.x02.ObservedPropertyType;
import org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x02.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x02.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x02.RelatedUnitOfMeasurementDocument.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x02.SensorType;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.SpatialResourceType;
import org.x52North.sensorweb.sos.importer.x02.URIDocument.URI;
import org.x52North.sensorweb.sos.importer.x02.UnitOfMeasurementType;

/**
 * Called in the case of having missing foi, observed property, unit of 
 * measurement, or sensor for any measured value column.<br />
 * This handler deals with two cases: <ul>
 * 	<li>Manual Resources</li>
 *  <li>Generated Resources</li></ul>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step6bModelHandler implements ModelHandler<Step6bModel> {

	private static final Logger logger = Logger.getLogger(Step6bModelHandler.class);
	
	@Override
	public void handleModel(Step6bModel stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		/*
		 *	LOCAL FIELDS
		 */
		MeasuredValue mV;
		Resource res;
		int mVColumnID = -1;
		AdditionalMetadata addiMeta;
		Column mVColumn;
		/*
		 * Get column reference from measured value object
		 * Get Resource
		 * Check Type
		 * AddOrUpdate Element in additional metadata and in column meta data
		 */
		mV = stepModel.getMeasuredValue();
		res = stepModel.getResource();
		if (logger.isDebugEnabled()) {
			logger.debug("Measured value: \"" + 
					mV + "\"; Resource: \"" +
					res + "\"");
		}
		if (mV != null && mV.getTableElement() != null) {
			mVColumnID = Helper.getColumnIdFromTableElement(mV.getTableElement());
			if (logger.isDebugEnabled()) {
				logger.debug("Column ID of measured value: " + mVColumnID);
			}
		}
		addiMeta = sosImportConf.getAdditionalMetadata();
		if(addiMeta == null) {
			addiMeta = sosImportConf.addNewAdditionalMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("added new AddtionalMetadata element");
			}
		}
		mVColumn = Helper.getColumnById(mVColumnID, sosImportConf);
		// add resource to model
		if(addRelatedResource(res,mVColumn,addiMeta)) {
			if (logger.isInfoEnabled()) {
				logger.info("Related resource updated/added: "
						+ res + "[" + res.getXMLId() + "]");
			}
		}
	}

	/**
	 * Check and add/update additional metadata element and ColumnMetadata 
	 * measure value column having id <code>mVColumnID</code>.
	 * @param res the related <code>Resource</code>
	 * @param mVColumnID
	 * @return
	 */
	private boolean addRelatedResource(Resource res, Column mVColumn, AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\taddRelatedResource()");
		}
		/*
		 * 	ADD FEATURE_OF_INTEREST
		 */
		if (res instanceof org.n52.sos.importer.model.resources.FeatureOfInterest) {
			org.n52.sos.importer.model.resources.FeatureOfInterest foi = 
					(org.n52.sos.importer.model.resources.FeatureOfInterest) res;
			return addRelatedFOI(foi,mVColumn,addiMeta);
		}
		/*
		 * 	ADD OBSERVED_PROPERTY
		 */
		else if (res instanceof org.n52.sos.importer.model.resources.ObservedProperty) {
			org.n52.sos.importer.model.resources.ObservedProperty obsProp = 
					(org.n52.sos.importer.model.resources.ObservedProperty) res;
			return addRelatedObservedProperty(obsProp,mVColumn,addiMeta);
		}
		/*
		 * 	ADD SENSOR
		 */
		else if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
			org.n52.sos.importer.model.resources.Sensor sensor = 
					(org.n52.sos.importer.model.resources.Sensor) res;
			return addRelatedSensor(sensor,mVColumn,addiMeta);
		}
		/*
		 * 	ADD UNIT_OF_MEASUREMENT
		 */
		else if (res instanceof org.n52.sos.importer.model.resources.UnitOfMeasurement) {
			org.n52.sos.importer.model.resources.UnitOfMeasurement uOM = 
					(org.n52.sos.importer.model.resources.UnitOfMeasurement) res;
			return addRelatedUOM(uOM,mVColumn,addiMeta);
		}
		return false;
	}

	private boolean addRelatedFOI(FeatureOfInterest foi, 
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\taddRelatedFOI()");
		}
		//
		FeatureOfInterestType foiXB = null;
		FeatureOfInterestType[] foisXB = addiMeta.getFeatureOfInterestArray();
		RelatedFOI[] relatedFOIs;
		boolean addNew;
		org.n52.sos.importer.model.position.Position pos;
		Position posXB = null;
		//
		if(foisXB != null && foisXB.length > 0) {
						
			findFOI : 
			for (FeatureOfInterestType aFOI : foisXB) {
				if ( aFOI.getResource().getID().equalsIgnoreCase(foi.getXMLId()) ) {
					foiXB = aFOI;
					break findFOI;
				}
			}
		
		}
		if(foiXB == null) {
			foiXB = addiMeta.addNewFeatureOfInterest();
		}
		if (foi.isGenerated()) {
			/*
			 * GENERATED
			 */
			GeneratedSpatialResourceType foiGRT = null;
			if (foiXB.getResource() instanceof GeneratedResourceType) {
				foiGRT = (GeneratedSpatialResourceType) foiXB.getResource();
			}
			if (foiGRT == null) {
				foiGRT = (GeneratedSpatialResourceType) foiXB.addNewResource().
						substitute(Constants.QNAME_GENERATED_SPATIAL_RESOURCE,
								GeneratedSpatialResourceType.type);
			}
			foiGRT.setID(foi.getXMLId());
			URI uri = foiGRT.getURI();
			if (uri == null) {
				uri = foiGRT.addNewURI();
			}
			uri.setUseAsPrefix(foi.isUseNameAfterPrefixAsURI());
			if (foi.isUseNameAfterPrefixAsURI()) {
				uri.setStringValue(foi.getUriPrefix());
			}
			foiGRT.setConcatString(foi.getConcatString());
			org.n52.sos.importer.model.table.Column[] relCols = (org.n52.sos.importer.model.table.Column[]) foi.getRelatedCols();
			int[] numbers = new int[relCols.length];
			for (int i = 0; i < relCols.length; i++) {
				org.n52.sos.importer.model.table.Column c = relCols[i];
				numbers[i] = c.getNumber();
			}
			foiGRT.setNumberArray(numbers);
			// add position to FOI
			pos = foi.getPosition();
			posXB = foiGRT.getPosition();
			if(posXB == null) {
				posXB = foiGRT.addNewPosition();
			}
			fillXBPosition(posXB,pos);
		} else {
			/*
			 * MANUAL
			 */
			SpatialResourceType foiSRT = null;
			if (foiXB.getResource() instanceof SpatialResourceType) {
				foiSRT = (SpatialResourceType) foiXB.getResource(); 
			}
			if (foiSRT == null) {
				foiSRT = (SpatialResourceType) foiXB.addNewResource().
						substitute(Constants.QNAME_MANUAL_SPATIAL_RESOURCE,
								SpatialResourceType.type);
			}
			foiSRT.setName(foi.getName());
			foiSRT.setID(foi.getXMLId());
			URI uri = foiSRT.getURI();
			if (uri == null) {
				uri = foiSRT.addNewURI();
			}
			uri.setStringValue(foi.getURIString());
			// add position to FOI
			pos = foi.getPosition();
			posXB = foiSRT.getPosition();
			if(posXB == null) {
				posXB = foiSRT.addNewPosition();
			}
			fillXBPosition(posXB,pos);
		}
		/*
		 * the FOI is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedFOIs = mVColumn.getRelatedFOIArray();
		addNew = !isFoiInArray(relatedFOIs,foi.getXMLId());
		if(addNew) {
			mVColumn.addNewRelatedFOI().setIdRef(foi.getXMLId());
			if (logger.isDebugEnabled()) {
				logger.debug("Added new related FOI element");
			}
		}
		relatedFOIs = mVColumn.getRelatedFOIArray();
		return isFoiInArray(relatedFOIs, foi.getXMLId());
	}

	private void fillXBPosition(Position posXB,
			org.n52.sos.importer.model.position.Position pos) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\taddOrUpdatePosition()");
		}
		if (pos == null || posXB == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("One position is null: skip filling: pos? " + pos
						+ "; posXB? " + posXB);
			}
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("BEFORE: pos: " + pos + "; posXB: " + posXB);
		}
		if (pos.getGroup() != null && !pos.getGroup().equalsIgnoreCase("")) {
			/*
			 * The position is contained in the file, so just add the link to 
			 * the group and finish
			 */
			String groupId = pos.getGroup();
			// check for old artifacts like alt/lat/long/epsg
			if (posXB.isSetAlt()) 		{ posXB.unsetAlt(); } 
			if (posXB.isSetEPSGCode()) 	{ posXB.unsetEPSGCode(); }
			if (posXB.isSetLat())		{ posXB.unsetLat(); }
			if (posXB.isSetLong())		{ posXB.unsetLong(); }
			//
			posXB.setGroup(groupId);
		} else {
			// the position is defined manually, so add the elements
			// clean group before if set
			if (posXB.isSetGroup()) { posXB.unsetGroup(); }
			/*
			 * 	EPSG_CODE
			 */
			posXB.setEPSGCode(pos.getEPSGCode().getValue());
			/*
			 * 	ALTITUDE
			 */
			Alt alt = posXB.getAlt();
			if (alt == null) {
				alt = posXB.addNewAlt();
			}
			alt.setFloatValue( new Double(pos.getHeight().getValue()).floatValue() );
			/*
			 * 	LATITUDE
			 */
			Lat lat = posXB.getLat();
			if (lat == null) {
				lat = posXB.addNewLat();
			}
			lat.setFloatValue( new Double(pos.getLatitude().getValue()).floatValue() );
			/*
			 *	LONGITUDE
			 */
			Long lon = posXB.getLong();
			if (lon == null) {
				lon = posXB.addNewLong();
			}
			lon.setFloatValue( new Double(pos.getLongitude().getValue()).floatValue() );
		}
		if (logger.isDebugEnabled()) {
			logger.debug("AFTER: posXB: " + posXB);
		}
		
	}

	private boolean addRelatedObservedProperty(
			org.n52.sos.importer.model.resources.ObservedProperty obsProp,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\taddRelatedObservedProperty()");
		}
		//
		ObservedPropertyType obsPropXB = null;
		ObservedPropertyType[] obsPropsXB = addiMeta.getObservedPropertyArray();
		RelatedObservedProperty[] relatedObsProps;
		boolean addNew;
		//
		if(obsPropsXB != null && obsPropsXB.length > 0) {
						
			findObservedProperty : 
			for (ObservedPropertyType obsPropy : obsPropsXB) {
				if (obsPropy.getResource().getID().equalsIgnoreCase(obsProp.getXMLId())) {
					obsPropXB = obsPropy;
					break findObservedProperty;
				}
			}
		
		}
		if(obsPropXB == null) {
			obsPropXB = addiMeta.addNewObservedProperty();
		}
		if (obsProp.isGenerated()) {
			/*
			 * GENERATED
			 */
			GeneratedResourceType obsPropGRT = null;
			if (obsPropXB.getResource() instanceof GeneratedResourceType) {
				obsPropGRT = (GeneratedResourceType) obsPropXB.getResource();
			}
			if (obsPropGRT == null) {
				obsPropGRT = (GeneratedResourceType) obsPropXB.addNewResource().
						substitute(Constants.QNAME_GENERATED_RESOURCE,
								GeneratedResourceType.type);
			}
			obsPropGRT.setID(obsProp.getXMLId());
			URI uri = obsPropGRT.getURI();
			if (uri == null) {
				uri = obsPropGRT.addNewURI();
			}
			uri.setUseAsPrefix(obsProp.isUseNameAfterPrefixAsURI());
			if (obsProp.isUseNameAfterPrefixAsURI()) {
				uri.setStringValue(obsProp.getUriPrefix());
			}
			obsPropGRT.setConcatString(obsProp.getConcatString());
			org.n52.sos.importer.model.table.Column[] relCols = 
					(org.n52.sos.importer.model.table.Column[])
					obsProp.getRelatedCols();
			int[] numbers = new int[relCols.length];
			for (int i = 0; i < relCols.length; i++) {
				org.n52.sos.importer.model.table.Column c = relCols[i];
				numbers[i] = c.getNumber();
			}
			obsPropGRT.setNumberArray(numbers);
		} else {
			/*
			 * MANUAL
			 */
			ManualResourceType obsPropMRT = null;
			if (obsPropXB.getResource() instanceof ManualResourceType) {
				obsPropMRT = (ManualResourceType) obsPropXB.getResource();
			}
			if (obsPropMRT == null) {
				obsPropMRT = (ManualResourceType) obsPropXB.addNewResource().
						substitute(Constants.QNAME_MANUAL_RESOURCE,
								ManualResourceType.type);
			}
			obsPropMRT.setName(obsProp.getName());
			obsPropMRT.setID(obsProp.getXMLId());
			URI uri = obsPropMRT.getURI();
			if (uri == null) {
				uri = obsPropMRT.addNewURI();
			}
			uri.setStringValue(obsProp.getURIString());
		}
		/*
		 * the ObservedProperty is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedObsProps = mVColumn.getRelatedObservedPropertyArray();
		addNew = !isObsPropInArray(relatedObsProps,obsProp.getXMLId());
		if(addNew) {
			mVColumn.addNewRelatedObservedProperty().setIdRef(obsProp.getXMLId());
		}
		relatedObsProps = mVColumn.getRelatedObservedPropertyArray();
		return isObsPropInArray(relatedObsProps, obsProp.getXMLId());
	}

	private boolean addRelatedSensor(
			org.n52.sos.importer.model.resources.Sensor sensor,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedSensor()");
		}
		//
		SensorType sensorXB = null;
		SensorType[] sensorsXB = addiMeta.getSensorArray();
		RelatedSensor[] relatedSensors;
		boolean addNew;
		//
		if(sensorsXB != null && sensorsXB.length > 0) {
						
			findSensor : 
			for (SensorType aSensor : sensorsXB) {
				if (aSensor.getResource().getID().equalsIgnoreCase(sensor.getXMLId())) {
					sensorXB = aSensor;
					break findSensor;
				}
			}
		
		}
		if(sensorXB == null) {
			sensorXB = addiMeta.addNewSensor();
		}
		if (sensor.isGenerated()) {
			/*
			 * GENERATED
			 */
			GeneratedResourceType sensorGRT = null;
			if (sensorXB.getResource() instanceof GeneratedResourceType) {
				sensorGRT = (GeneratedResourceType) sensorXB.getResource();
			}
			if (sensorGRT == null) {
				sensorGRT = (GeneratedResourceType) sensorXB.addNewResource().
						substitute(Constants.QNAME_GENERATED_RESOURCE,
								GeneratedResourceType.type);
			}
			sensorGRT.setID(sensor.getXMLId());
			URI uri = sensorGRT.getURI();
			if (uri == null) {
				uri = sensorGRT.addNewURI();
			}
			uri.setUseAsPrefix(sensor.isUseNameAfterPrefixAsURI());
			if (sensor.isUseNameAfterPrefixAsURI()) {
				uri.setStringValue(sensor.getUriPrefix());
			}
			sensorGRT.setConcatString(sensor.getConcatString());
			org.n52.sos.importer.model.table.Column[] relCols = 
					(org.n52.sos.importer.model.table.Column[])
					sensor.getRelatedCols();
			int[] numbers = new int[relCols.length];
			for (int i = 0; i < relCols.length; i++) {
				org.n52.sos.importer.model.table.Column c = relCols[i];
				numbers[i] = c.getNumber();
			}
			sensorGRT.setNumberArray(numbers);
			sensorXB.setResource(sensorGRT);
		} else {
			/*
			 * MANUAL
			 */
			ManualResourceType sensorRT = null; 
			if (sensorXB.getResource() instanceof ManualResourceType) {
				sensorRT = (ManualResourceType) sensorXB.getResource();
			}
			if (sensorRT == null) {
				sensorRT = (ManualResourceType) sensorXB.addNewResource().
						substitute(Constants.QNAME_MANUAL_RESOURCE,
								ManualResourceType.type);
			}
			sensorRT.setName(sensor.getName());
			URI uri = sensorRT.getURI();
			if (uri == null) {
				uri = sensorRT.addNewURI();
			}
			uri.setStringValue(sensor.getURIString());
			sensorRT.setID(sensor.getXMLId());
		}
		/*
		 * the Sensor is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedSensors = mVColumn.getRelatedSensorArray();
		addNew = !Helper.isSensorInArray(relatedSensors,sensor.getXMLId());
		if(addNew) {
			mVColumn.addNewRelatedSensor().setIdRef(sensor.getXMLId());
		}
		relatedSensors = mVColumn.getRelatedSensorArray();
		return Helper.isSensorInArray(relatedSensors, sensor.getXMLId());
	}
	
	private boolean addRelatedUOM(
			org.n52.sos.importer.model.resources.UnitOfMeasurement uOM,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedUOM()");
		}
		//
		UnitOfMeasurementType uOMXB = null;
		UnitOfMeasurementType[] uOMsXB = addiMeta.getUnitOfMeasurementArray();
		RelatedUnitOfMeasurement[] relatedUOMs;
		boolean addNew;
		//
		if(uOMsXB != null && uOMsXB.length > 0) {
						
			findUOM : 
			for (UnitOfMeasurementType uom : uOMsXB) {
				if (uom.getResource().getID().equalsIgnoreCase(uOM.getXMLId())) {
					uOMXB = uom;
					break findUOM;
				}
			}
		
		}
		if(uOMXB == null) {
			uOMXB = addiMeta.addNewUnitOfMeasurement();
		}
		if (uOM.isGenerated()) {
			/*
			 * GENERATED
			 */
			GeneratedResourceType uOMGRT = null;
			if (uOMXB.getResource() instanceof GeneratedResourceType) {
				uOMGRT = (GeneratedResourceType) uOMXB.getResource();
			}
			if (uOMGRT == null) {
				uOMGRT = (GeneratedResourceType) uOMXB.addNewResource().
						substitute(Constants.QNAME_GENERATED_RESOURCE, 
								GeneratedResourceType.type);
			}
			uOMGRT.setID(uOM.getXMLId());
			URI uri = uOMGRT.getURI();
			if (uri == null) {
				uri = uOMGRT.addNewURI();
			}
			uri.setUseAsPrefix(uOM.isUseNameAfterPrefixAsURI());
			if (uOM.isUseNameAfterPrefixAsURI()) {
				uri.setStringValue(uOM.getUriPrefix());
			}
			uOMGRT.setConcatString(uOM.getConcatString());
			org.n52.sos.importer.model.table.Column[] relCols = 
					(org.n52.sos.importer.model.table.Column[]) 
														uOM.getRelatedCols();
			int[] numbers = new int[relCols.length];
			for (int i = 0; i < relCols.length; i++) {
				org.n52.sos.importer.model.table.Column c = relCols[i];
				numbers[i] = c.getNumber();
			}
			uOMGRT.setNumberArray(numbers);
		} else {
			/*
			 * MANUAL
			 */
			ManualResourceType uOMMRT = null;
			if (uOMXB.getResource() instanceof ManualResourceType) {
				uOMMRT = (ManualResourceType) uOMXB.getResource();
			}
			if (uOMMRT == null) {
				uOMMRT = (ManualResourceType) uOMXB.addNewResource().
						substitute(Constants.QNAME_MANUAL_RESOURCE,
								ManualResourceType.type);
			}
			uOMMRT.setName(uOM.getName());
			uOMMRT.setID(uOM.getXMLId());
			URI uri = uOMMRT.getURI();
			if (uri == null) {
				uri = uOMMRT.addNewURI();
			}
			uri.setStringValue(uOM.getURIString());
		}
		/*
		 * the UOM is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedUOMs = mVColumn.getRelatedUnitOfMeasurementArray();
		addNew = !isUOMInArray(relatedUOMs,uOM.getXMLId());
		if(addNew) {
			mVColumn.addNewRelatedUnitOfMeasurement().setIdRef(uOM.getXMLId());
		}
		relatedUOMs = mVColumn.getRelatedUnitOfMeasurementArray();
		return isUOMInArray(relatedUOMs, uOM.getURIString());
	}
	
	private boolean isFoiInArray(RelatedFOI[] relatedFOIs, String foiXmlId) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\tisFoiInArray()");
		}
		for (RelatedFOI relatedFoiFromArray : relatedFOIs) {
			if (relatedFoiFromArray.isSetIdRef()  && 
					relatedFoiFromArray.getIdRef().equalsIgnoreCase(foiXmlId) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isObsPropInArray(RelatedObservedProperty[] relatedObsProps,
			String obsPropXmlId) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisObsPropInArray()");
		}
		for (RelatedObservedProperty relatedObsPropFromArray : relatedObsProps) {
			if (relatedObsPropFromArray.isSetIdRef() && 
					relatedObsPropFromArray.getIdRef().equalsIgnoreCase(obsPropXmlId) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isUOMInArray(RelatedUnitOfMeasurement[] relatedUOMs,
			String uomUriXmlId) {
		if (logger.isTraceEnabled()) {
			logger.trace("isUOMInArray()");
		}
		for (RelatedUnitOfMeasurement relatedUOMFromArray : relatedUOMs) {
			if (relatedUOMFromArray.isSetIdRef() && 
					relatedUOMFromArray.getIdRef().equalsIgnoreCase(uomUriXmlId) ) {
				return true;
			}
		}
		return false;
	}

	
}

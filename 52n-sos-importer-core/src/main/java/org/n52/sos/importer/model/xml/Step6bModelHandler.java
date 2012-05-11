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
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.Resource;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.AltDocument.Alt;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x02.FeatureOfInterestDocument.FeatureOfInterest;
import org.x52North.sensorweb.sos.importer.x02.LatDocument.Lat;
import org.x52North.sensorweb.sos.importer.x02.LongDocument.Long;
import org.x52North.sensorweb.sos.importer.x02.ObservedPropertyDocument.ObservedProperty;
import org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x02.SensorDocument.Sensor;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.UnitOfMeasurementDocument.UnitOfMeasurement;

/**
 * Called in the case of having missing foi, observed property, unit of 
 * measurement, or sensor for any measured value column.
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
			logger.debug("Found measured value \"" + 
					mV + "\" and a resource \"" +
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
		}
		mVColumn = Helper.getColumnById(mVColumnID, sosImportConf);
		if(addRelatedResource(res,mVColumn,addiMeta)) {
			if (logger.isInfoEnabled()) {
				logger.info("Related resource updated/added: "
						+ res);
			}
		}
	}

	private boolean addRelatedFOI(
			org.n52.sos.importer.model.resources.FeatureOfInterest foi, 
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedFOI()");
		}
		//
		FeatureOfInterest foiXB = null;
		FeatureOfInterest[] foisXB = addiMeta.getFeatureOfInterestArray();
		RelatedFOI[] relatedFOIs;
		boolean addNew;
		org.n52.sos.importer.model.position.Position pos;
		Position posXB = null;
		//
		if(foisXB != null && foisXB.length > 0) {
						
			findFOI : 
			for (FeatureOfInterest aFOI : foisXB) {
				if ( aFOI.getURI().equalsIgnoreCase(foi.getURIString()) ) {
					foiXB = aFOI;
					break findFOI;
				}
			}
		
		}
		if(foiXB == null) {
			foiXB = addiMeta.addNewFeatureOfInterest();
		}
		foiXB.setName(foi.getName());
		foiXB.setURI(foi.getURIString());
		// add position to FOI
		pos = foi.getPosition();
		posXB = foiXB.getPosition();
		if(posXB == null) {
			foiXB.addNewPosition();
		}
		fillXBPosition(posXB,pos);
		/*
		 * the FOI is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedFOIs = mVColumn.getRelatedFOIArray();
		addNew = !isFoiInArray(relatedFOIs,foi.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedFOI().setURI(foi.getURIString());
			if (logger.isDebugEnabled()) {
				logger.debug("Added new related FOI element");
			}
		}
		relatedFOIs = mVColumn.getRelatedFOIArray();
		return isFoiInArray(relatedFOIs, foi.getURIString());
	}

	private boolean addRelatedObservedProperty(
			org.n52.sos.importer.model.resources.ObservedProperty obsProp,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedObservedProperty()");
		}
		//
		ObservedProperty obsPropXB = null;
		ObservedProperty[] obsPropsXB = addiMeta.getObservedPropertyArray();
		RelatedObservedProperty[] relatedObsProps;
		boolean addNew;
		//
		if(obsPropsXB != null && obsPropsXB.length > 0) {
						
			findObservedProperty : 
			for (ObservedProperty obsPropy : obsPropsXB) {
				if (obsPropy.getURI().equalsIgnoreCase(obsProp.getURIString())) {
					obsPropXB = obsPropy;
					break findObservedProperty;
				}
			}
		
		}
		if(obsPropXB == null) {
			obsPropXB = addiMeta.addNewObservedProperty();
		}
		obsPropXB.setName(obsProp.getName());
		obsPropXB.setURI(obsProp.getURIString());
		/*
		 * the ObservedProperty is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedObsProps = mVColumn.getRelatedObservedPropertyArray();
		addNew = !isObsPropInArray(relatedObsProps,obsProp.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedObservedProperty().setURI(obsProp.getURIString());
		}
		relatedObsProps = mVColumn.getRelatedObservedPropertyArray();
		return isObsPropInArray(relatedObsProps, obsProp.getURIString());
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
	
	private boolean addRelatedSensor(
			org.n52.sos.importer.model.resources.Sensor sensor,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedSensor()");
		}
		//
		Sensor sensorXB = null;
		Sensor[] sensorsXB = addiMeta.getSensorArray();
		RelatedSensor[] relatedSensors;
		boolean addNew;
		//
		if(sensorsXB != null && sensorsXB.length > 0) {
						
			findSensor : 
			for (Sensor aSensor : sensorsXB) {
				if (aSensor.getURI().equalsIgnoreCase(sensor.getURIString())) {
					sensorXB = aSensor;
					break findSensor;
				}
			}
		
		}
		if(sensorXB == null) {
			sensorXB = addiMeta.addNewSensor();
		}
		sensorXB.setName(sensor.getName());
		sensorXB.setURI(sensor.getURIString());
		/*
		 * the Sensor is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedSensors = mVColumn.getRelatedSensorArray();
		addNew = !isSensorInArray(relatedSensors,sensor.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedSensor().setURI(sensor.getURIString());
		}
		relatedSensors = mVColumn.getRelatedSensorArray();
		return isSensorInArray(relatedSensors, sensor.getURIString());
	}
	
	private boolean addRelatedUOM(
			org.n52.sos.importer.model.resources.UnitOfMeasurement uOM,
			Column mVColumn,
			AdditionalMetadata addiMeta) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\taddRelatedUOM()");
		}
		//
		UnitOfMeasurement uOMXB = null;
		UnitOfMeasurement[] uOMsXB = addiMeta.getUnitOfMeasurementArray();
		RelatedUnitOfMeasurement[] relatedUOMs;
		boolean addNew;
		//
		if(uOMsXB != null && uOMsXB.length > 0) {
						
			findUOM : 
			for (UnitOfMeasurement uom : uOMsXB) {
				if (uom.getURI().equalsIgnoreCase(uOM.getURIString())) {
					uOMXB = uom;
					break findUOM;
				}
			}
		
		}
		if(uOMXB == null) {
			uOMXB = addiMeta.addNewUnitOfMeasurement();
		}
		uOMXB.setName(uOM.getName());
		uOMXB.setURI(uOM.getURIString());
		uOMXB.setCode(uOM.getName());
		/*
		 * the UOM is in the model.
		 * Next is to link measure value column to this entity by its URI
		 */
		relatedUOMs = mVColumn.getRelatedUnitOfMeasurementArray();
		addNew = !isUOMInArray(relatedUOMs,uOM.getURIString());
		if(addNew) {
			mVColumn.addNewRelatedUnitOfMeasurement().setURI(uOM.getURIString());
		}
		relatedUOMs = mVColumn.getRelatedUnitOfMeasurementArray();
		return isUOMInArray(relatedUOMs, uOM.getURIString());
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
		/*
		 * 	EPSG_CODE
		 */
		posXB.setEPSGCode(pos.getEPSGCode().getValue());
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.epsg: " + posXB.getEPSGCode() + 
					" - pos.epsg: " + pos.getEPSGCode().getValue());
		}
		/*
		 * 	ALTITUDE
		 */
		Alt alt = posXB.getAlt();
		if (alt == null) {
			alt = posXB.addNewAlt();
		}
		alt.setFloatValue( new Double(pos.getHeight().getValue()).floatValue() );
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.alt: " + posXB.getAlt() + 
					" - pos.alt: " + pos.getHeight().getValue());
		}
		/*
		 * 	LATITUDE
		 */
		Lat lat = posXB.getLat();
		if (lat == null) {
			lat = posXB.addNewLat();
		}
		lat.setFloatValue( new Double(pos.getLatitude().getValue()).floatValue() );
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.lat: " + posXB.getLat() + 
					" - pos.lat: " + pos.getLatitude().getValue());
		}
		/*
		 *	LONGITUDE
		 */
		Long lon = posXB.getLong();
		if (lon == null) {
			lon = posXB.addNewLong();
		}
		lon.setFloatValue( new Double(pos.getLongitude().getValue()).floatValue() );
		if (logger.isDebugEnabled()) {
			logger.debug("posXB.lon: " + posXB.getLong() + 
					" - pos.lon: " + pos.getLongitude().getValue());
		}
	}

	private boolean isFoiInArray(RelatedFOI[] relatedFOIs, String foiURI) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisFoiInArray()");
		}
		for (RelatedFOI relatedFoiFromArray : relatedFOIs) {
			if (relatedFoiFromArray.isSetURI() && 
					relatedFoiFromArray.getURI().equalsIgnoreCase(foiURI) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isObsPropInArray(RelatedObservedProperty[] relatedObsProps,
			String obsPropURI) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisObsPropInArray()");
		}
		for (RelatedObservedProperty relatedObsPropFromArray : relatedObsProps) {
			if (relatedObsPropFromArray.isSetURI() && 
					relatedObsPropFromArray.getURI().equalsIgnoreCase(obsPropURI) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSensorInArray(RelatedSensor[] relatedSensors,
			String sensorURIString) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\t\tisSensorInArray()");
		}
		for (RelatedSensor relatedSensorFromArray : relatedSensors) {
			if (relatedSensorFromArray.isSetURI() && 
					relatedSensorFromArray.getURI().equalsIgnoreCase(sensorURIString) ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isUOMInArray(RelatedUnitOfMeasurement[] relatedUOMs,
			String uomUriString) {
		if (logger.isTraceEnabled()) {
			logger.trace("isUOMInArray()");
		}
		for (RelatedUnitOfMeasurement relatedUOMFromArray : relatedUOMs) {
			if (relatedUOMFromArray.isSetURI() && 
					relatedUOMFromArray.getURI().equalsIgnoreCase(uomUriString) ) {
				return true;
			}
		}
		return false;
	}

	
}

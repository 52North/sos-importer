package org.n52.sos.importer.controller;

import java.util.List;

import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;

public class MeasuredValueController {

	private MeasuredValue measuredValue;

	public MeasuredValueController() {
	}
	
	public MeasuredValueController(MeasuredValue measuredValue) {
		this.measuredValue = measuredValue;
	}
	
	public Step6bModel getMissingResourceForMeasuredValue() {
		List<MeasuredValue> measuredValues = ModelStore.getInstance().getMeasuredValues();
		
		for (MeasuredValue mv: measuredValues) {
			if (mv.getFeatureOfInterest() == null) 
				return new Step6bModel(mv, new FeatureOfInterest());
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getObservedProperty() == null) {
				return new Step6bModel(mv, new ObservedProperty());
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getUnitOfMeasurement() == null) {
				return new Step6bModel(mv, new UnitOfMeasurement());
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getSensor() == null) {
				return new Step6bModel(mv, new Sensor());
			}
		}
		return null;
	}
	
	
	public void setMeasuredValue(MeasuredValue measuredValue) {
		this.measuredValue = measuredValue;
	}

	public MeasuredValue getMeasuredValue() {
		return measuredValue;
	}
	 
}

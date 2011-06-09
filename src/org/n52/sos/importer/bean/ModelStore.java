package org.n52.sos.importer.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.Step1Model;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;
import org.n52.sos.importer.model.table.ColumnModel;
import org.n52.sos.importer.view.dateAndTime.MissingComponentPanel;

public class ModelStore {
	
	private static ModelStore instance = null;
	
	private Step1Model step1Model;
	
	private Step2Model step2Model;
	
	private List<MeasuredValue> measuredValues;
	
	private List<Resource> resourcesWithoutMeasuredValue;
	
	private ListIterator<DateAndTimeModel> dateAndTimeModels;
	
	private ModelStore() {
		measuredValues = new ArrayList<MeasuredValue>();
		resourcesWithoutMeasuredValue = new ArrayList<Resource>();
	}
	
	public static ModelStore getInstance() {
		if (instance == null)
			instance = new ModelStore();
		return instance;
	}
	
	public void addMeasuredValue(MeasuredValue mv) {
		measuredValues.add(mv);
	}
	
	public List<MeasuredValue> getMeasuredValues() {
		return measuredValues;
	}
	
	public MeasuredValue getMeasuredValueAtColumn(int column) {
		for (MeasuredValue mv: measuredValues) {
			if (((ColumnModel)mv.getTableElement()).getNumber() == column)
				return mv;
		}
		return null;
	}
	
	public void addResourcesWithoutMeasuredValue(List<Resource> resources) {
		resourcesWithoutMeasuredValue.addAll(resources);
	}
	
	public Resource pollResourceWithoutMeasuredValue() {
		if (resourcesWithoutMeasuredValue.isEmpty()) return null;
		Resource r = resourcesWithoutMeasuredValue.remove(0);
		return r;
	}
	
	public DateAndTimeModel getNextUnassignedDateAndTime() {
		if (dateAndTimeModels.hasNext())
			return dateAndTimeModels.next();
		else {
			//reset
			while (dateAndTimeModels.hasPrevious()) {
				dateAndTimeModels.previous();
			}
			return null;
		}
	}
	
	public DateAndTimeModel getNextDateAndTimeModelWithMissingValues() {
		DateAndTimeModel dtm;
		DateAndTimeController dtc = new DateAndTimeController();
		List<MissingComponentPanel> missingComponentPanels;
		
		while (dateAndTimeModels.hasNext()) {
			dtm = dateAndTimeModels.next();
			dtc.setModel(dtm);
			missingComponentPanels = dtc.getMissingComponentPanels();
			if (missingComponentPanels.size() > 0)
				return dtm;
		}
		return null;
	}
	
	public Resource getMissingResourceForMeasuredValues() {
		for (MeasuredValue mv: measuredValues) {
			if (mv.getFeatureOfInterest() == null) 
				return new FeatureOfInterest();
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getObservedProperty() == null) {
				return new ObservedProperty();
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getUnitOfMeasurement() == null) {
				return new UnitOfMeasurement();
			}
		}
		for (MeasuredValue mv: measuredValues) {
			if (mv.getSensorName() == null) {
				return new SensorName();
			}
		}
		return null;
	}
	
	public int getTableOrientation() {
		return TableController.COLUMNS;
	}

	public void setStep1Model(Step1Model step1Model) {
		this.step1Model = step1Model;
	}

	public Step1Model getStep1Model() {
		return step1Model;
	}

	public void setStep2Model(Step2Model step2Model) {
		this.step2Model = step2Model;
	}

	public Step2Model getStep2Model() {
		return step2Model;
	}

	public void setDateAndTimeModelIterator(ListIterator<DateAndTimeModel> dateAndTimeModelIterator) {
		this.dateAndTimeModels = dateAndTimeModelIterator;
	}

	public ListIterator<DateAndTimeModel> getDateAndTimeModelIterator() {
		return dateAndTimeModels;
	}
}

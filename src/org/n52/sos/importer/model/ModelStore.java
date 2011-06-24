package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.view.dateAndTime.MissingDateAndTimePanel;

public class ModelStore {
	
	private static ModelStore instance = null;
	
	private Step1Model step1Model;
	
	private Step2Model step2Model;
	
	private List<MeasuredValue> measuredValues;
	
	private List<Resource> resourcesWithoutMeasuredValue;
	
	private ListIterator<DateAndTime> dateAndTimeModels;
	
	private List<FeatureOfInterest> featureOfInterests;
	
	private ModelStore() {
		measuredValues = new ArrayList<MeasuredValue>();
		featureOfInterests = new ArrayList<FeatureOfInterest>();
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
			if (((Column)mv.getTableElement()).getNumber() == column)
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
	
	public DateAndTime getNextUnassignedDateAndTime() {
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
	
	public DateAndTime getNextDateAndTimeModelWithMissingValues() {
		DateAndTime dtm;
		DateAndTimeController dtc = new DateAndTimeController();
		List<MissingDateAndTimePanel> missingComponentPanels;
		
		while (dateAndTimeModels.hasNext()) {
			dtm = dateAndTimeModels.next();
			dtc.setDateAndTime(dtm);
			missingComponentPanels = dtc.getMissingComponentPanels();
			if (missingComponentPanels.size() > 0)
				return dtm;
		}
		return null;
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

	public void setDateAndTimeModelIterator(ListIterator<DateAndTime> dateAndTimeModelIterator) {
		this.dateAndTimeModels = dateAndTimeModelIterator;
	}

	public ListIterator<DateAndTime> getDateAndTimeModelIterator() {
		return dateAndTimeModels;
	}

	public void addFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		featureOfInterests.add(featureOfInterest);
	}

	public List<FeatureOfInterest> getFeatureOfInterests() {
		return featureOfInterests;
	}
}

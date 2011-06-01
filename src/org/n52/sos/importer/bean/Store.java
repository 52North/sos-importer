package org.n52.sos.importer.bean;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.controller.TableController;

public class Store {
	
	private static Store instance = null;
	
	public List<MeasuredValue> measuredValues;
	
	public List<Resource> resourcesWithoutMeasuredValue;
	
	private Store() {
		measuredValues = new ArrayList<MeasuredValue>();
		resourcesWithoutMeasuredValue = new ArrayList<Resource>();
	}
	
	public static Store getInstance() {
		if (instance == null)
			instance = new Store();
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
			if (mv.getColumnNumber() == column)
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
	
	public int getTableOrientation() {
		return TableController.COLUMNS;
	}
}

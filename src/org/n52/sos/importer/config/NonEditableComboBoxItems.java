package org.n52.sos.importer.config;

public class NonEditableComboBoxItems {
	
	private static NonEditableComboBoxItems instance = null;
	
	private final String[] decimalSeparatorValues = { ".", "," };
	
	private final String[] thousandsSeparatorValues = { ",", ".", "'", " " };
	
	private final Object[] latLonUnits = {"°", "° '", "° ' ''", "meters"};
	
	private final Object[] heightUnits = {"meters", "feet"};
	
	private NonEditableComboBoxItems() {
	}

	public static NonEditableComboBoxItems getInstance() {
		if (instance == null)
			instance = new NonEditableComboBoxItems();
		return instance;
	}

	public Object[] getHeightUnits() {
		return heightUnits;
	}

	public Object[] getLatLonUnits() {
		return latLonUnits;
	}

	public String[] getDecimalSeparatorValues() {
		return decimalSeparatorValues;
	}

	public String[] getThousandsSeparatorValues() {
		return thousandsSeparatorValues;
	}
}

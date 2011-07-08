package org.n52.sos.importer.model.position;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public class PositionComponentModel {

	private static final Logger logger = Logger.getLogger(PositionComponentModel.class);
	
	private TableElement tableElement;
	
	private double value = -1;
	
	private String unit = null;

	public PositionComponentModel(TableElement tableElement) {
		this.tableElement = tableElement;
	}
	
	public PositionComponentModel(double value, String unit) {
		this.value = value;
		this.unit = unit;
	}

	public void setValue(double value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public double getValue() {
		return value;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	public void mark(Color color) {
		if (tableElement != null)
			tableElement.mark(color);
	}
	
	public double getParsedValue(Cell measuredValuePosition) {
		if (tableElement == null)
			return getValue();
		else 
			return parse(tableElement.getValueFor(measuredValuePosition));
	}
	
	public String getParsedUnit() {
		if (unit == null || unit.equals(""))
			return "n/a";
		else if (unit.equals("m") || unit.equals("meters")) 
			return "m";
		else if (unit.equals("ft") || unit.equals("feet"))
			return "ft";
		else if (unit.equals("degree") || unit.equals("°"))
			return "degree";
		return "n/a";
	}
	
	public double parse(String s) {
		//TODO
		return Double.parseDouble(s);
	}
}

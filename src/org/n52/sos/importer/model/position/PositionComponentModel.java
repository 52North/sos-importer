package org.n52.sos.importer.model.position;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public class PositionComponentModel {

	private static final Logger logger = Logger.getLogger(PositionComponentModel.class);
	
	private TableElement tableElement;
	
	private String value = null;
	
	private String unit = null;

	public PositionComponentModel(TableElement tableElement) {
		this.tableElement = tableElement;
	}
	
	public PositionComponentModel(String value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public String getValue() {
		return value;
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

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}
	
	public String getParsedValue(Cell measuredValuePosition) {
		if (tableElement == null)
			return getValue();
		else 
			return parse(tableElement.getValueFor(measuredValuePosition));
	}
	
	public String parse(String s) {
		//TODO
		return s;
	}
}

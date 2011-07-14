package org.n52.sos.importer.model.position;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public class EPSGCode {

	private static final Logger logger = Logger.getLogger(EPSGCode.class);
	
	private TableElement tableElement;
	
	private int value = -1;

	public EPSGCode(TableElement tableElement) {
		this.tableElement = tableElement;
	}
	
	public EPSGCode(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	public int getParsedValue(Cell measuredValuePosition) {
		if (tableElement == null)
			return getValue();
		else 
			return parse(tableElement.getValueFor(measuredValuePosition));
	}
	
	public int parse(String s) {
		//TODO
		return Integer.valueOf(s);
	}
	
	public void mark(Color color) {
		if (tableElement != null)
			tableElement.mark(color);
	}
}

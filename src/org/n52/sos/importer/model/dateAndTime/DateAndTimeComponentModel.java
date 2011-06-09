package org.n52.sos.importer.model.dateAndTime;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.table.TableElement;

public class DateAndTimeComponentModel {

	private static final Logger logger = Logger.getLogger(DateAndTimeComponentModel.class);
	
	private TableElement tableElement;
	
	private int value = -1;

	public DateAndTimeComponentModel(TableElement tableElement) {
		this.tableElement = tableElement;
	}
	
	public DateAndTimeComponentModel(int value) {
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
	
	public void mark(Color color) {
		if (tableElement != null)
			tableElement.mark(color);
	}
}

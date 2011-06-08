package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.model.table.TableElement;

public class MonthModel extends DateAndTimeComponentModel {
	
	public MonthModel(TableElement tableElement) {
		super(tableElement);
	}
	
	public MonthModel(int value) {
		super(value);
	}
}

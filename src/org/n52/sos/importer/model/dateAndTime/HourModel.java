package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.model.table.TableElement;

public class HourModel extends DateAndTimeComponentModel {
	
	public HourModel(TableElement tableElement) {
		super(tableElement);
	}
	
	public HourModel(int value) {
		super(value);
	}
}

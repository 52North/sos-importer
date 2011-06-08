package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.model.table.TableElement;

public class TimeZoneModel extends DateAndTimeComponentModel {

	public TimeZoneModel(TableElement tableElement) {
		super(tableElement);
	}
	
	public TimeZoneModel(int value) {
		super(value);
	}	
}

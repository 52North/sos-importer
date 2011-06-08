package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.model.table.TableElement;

public class YearModel extends DateAndTimeComponentModel {

	public YearModel(TableElement tableElement) {
		super(tableElement);
	}
	
	public YearModel(int value) {
		super(value);
	}
}

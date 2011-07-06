package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.table.TableElement;

public class Height extends PositionComponentModel {

	public Height(TableElement tableElement) {
		super(tableElement);
	}
	
	public Height(double value, String unit) {
		super(value, unit);
	}

}

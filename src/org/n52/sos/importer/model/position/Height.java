package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.table.TableElement;

public class Height extends PositionComponentModel {

	public Height(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Height(double value, String unit) {
		super(value, unit);
	}

	@Override
	public String toString() {
		return "Height" + super.toString();
	}
}

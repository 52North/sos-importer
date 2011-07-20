package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.table.TableElement;

public class Latitude extends PositionComponentModel {

	public Latitude(TableElement tableElement) {
		super(tableElement);
	}
	
	public Latitude(double value, String unit) {
		super(value, unit);
	}
	
	@Override
	public String toString() {
		return "Latitude" + super.toString();
	}
}

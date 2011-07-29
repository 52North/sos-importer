package org.n52.sos.importer.model.measuredValue;

import org.n52.sos.importer.interfaces.Parseable;

public class Text extends MeasuredValue implements Parseable {
	
	@Override
	public String toString() {
		return "Text" + super.toString();
	}

	@Override
	public Object parse(String s) {
		return s.trim();
	}
}

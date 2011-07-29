package org.n52.sos.importer.model.measuredValue;

import org.n52.sos.importer.interfaces.Parseable;

public class Count extends MeasuredValue implements Parseable {

	@Override
	public String toString() {
		return "Count" + super.toString();
	}
	
	@Override
	public Object parse(String s) {
		s = s.trim();
		int i = Integer.parseInt(s);
		if (i < 0) throw new NumberFormatException();
		return i;
	}
}

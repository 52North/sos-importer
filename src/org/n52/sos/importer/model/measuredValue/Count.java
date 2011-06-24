package org.n52.sos.importer.model.measuredValue;

import org.n52.sos.importer.Parseable;

public class Count extends MeasuredValue implements Parseable {

	@Override
	public String toString() {
		return "Count";
	}
	
	@Override
	public Object parse(String s) {
		s = s.trim();
		int i = Integer.parseInt(s);
		if (i < 0) throw new NumberFormatException();
		return i;
	}
}

package org.n52.sos.importer.model.measuredValue;


public class Boolean extends MeasuredValue {

	@Override
	public String toString() {
		return "Boolean" + super.toString();
	}
	
	@Override
	public Object parse(String s) {
		s = s.trim();
		if (s.equals("0") || s.equalsIgnoreCase("false")) return false;
		else if (s.equals("1") || s.equalsIgnoreCase("true")) return true;
		else throw new NumberFormatException();
	}
}

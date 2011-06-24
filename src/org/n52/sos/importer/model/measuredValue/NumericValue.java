package org.n52.sos.importer.model.measuredValue;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.n52.sos.importer.Formatable;
import org.n52.sos.importer.Parseable;

public class NumericValue extends MeasuredValue implements Formatable, Parseable {

	private String decimalSeparator;
	
	private String thousandsSeparator;
	
	@Override
	public String toString() {
		return "Numeric Value";
	}

	public void setDecimalSeparator(String selectedDecimalSeparator) {
		this.decimalSeparator = selectedDecimalSeparator;
	}

	public String getDecimalSeparator() {
		return decimalSeparator;
	}

	public void setThousandsSeparator(String selectedThousandsSeparator) {
		this.thousandsSeparator = selectedThousandsSeparator;
	}

	public String getThousandsSeparator() {
		return thousandsSeparator;
	}
	
	@Override
	public Object parse(String s) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(decimalSeparator.charAt(0));
		symbols.setGroupingSeparator(thousandsSeparator.charAt(0));
		
		Number n;
		try {
			DecimalFormat formatter = new DecimalFormat();
			formatter.setDecimalFormatSymbols(symbols);
			n = formatter.parse(s);
        } catch (ParseException e) {
	        throw new NumberFormatException();
		}					
		
		return n.doubleValue();
	}

	@Override
	public String format(Object o) {
		double number = (Double)o;
		
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(decimalSeparator.charAt(0));
		symbols.setGroupingSeparator(thousandsSeparator.charAt(0));
		
		DecimalFormat formatter = new DecimalFormat();
		formatter.setDecimalFormatSymbols(symbols);
		String n = formatter.format(number);
		return n;
	}
}

package org.n52.sos.importer.model.position;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.position.MissingHeightPanel;

public class Height extends PositionComponent {

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
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingHeightPanel((Position)c);
	}
	
	public static Height parse(String s) {
		double value = 0;
		String unit = "m";
		
		String number;
		if (s.contains("km")) {
			unit = "km";
			number = s.replace("km", "");
		} else if (s.contains("mi")) {
			unit = "mi";
			number = s.replace("mi", "");
		} else if (s.contains("m")) {
			unit = "m";
			number = s.replace("m", "");
		} else if (s.contains("ft")) {
			unit = "ft";
			number = s.replace("ft", "");
		} else
			number = s;
		
		NumericValue nv = new NumericValue();
		if (number.contains(","))
			nv.setDecimalSeparator(",");
		else if (number.contains("."))
			nv.setDecimalSeparator(".");
		else 
			nv.setDecimalSeparator(" ");
		nv.setThousandsSeparator(" ");
		
		value = nv.parse(number);			
		
		return new Height(value, unit);
	}

	public Height forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Height(getValue(), getParsedUnit());
		else {
			String heightString = getTableElement().getValueFor(featureOfInterestPosition);
			return Height.parse(heightString);
		}
	}
}

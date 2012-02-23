package org.n52.sos.importer.model.position;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.position.MissingLatitudePanel;

public class Latitude extends PositionComponent {

	public Latitude(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Latitude(double value, String unit) {
		super(value, unit);
	}
	
	@Override
	public String toString() {
		return "Latitude" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingLatitudePanel((Position)c);
	}
	
	/**
	 * tries to convert a given String into a valid Latitude object
	 */
	public static Latitude parse(String s) {
		double value = 0;
		String unit = "";
		
		String number;
		//TODO handle inputs like degrees/minutes/seconds, n.Br.
		if (s.contains("°")) {
			unit = "°";
			String[] part = s.split("°");
			number = part[0];
		} else if (s.contains("m")) {
			unit = "m";
			number = s.replace("m", "");
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

		if (unit.equals(""))
			if (value <= 90)
				unit = "°";
			else 
				unit = "m";
		
		return new Latitude(value, unit);
	}
	
	@Override
	public Latitude forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Latitude(getValue(), getParsedUnit());
		else {
			String latitudeString = getTableElement().getValueFor(featureOfInterestPosition);
			return Latitude.parse(latitudeString);
		}
	}
}

package org.n52.sos.importer.model.position;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.position.MissingLongitudePanel;

public class Longitude extends PositionComponent {

	public Longitude(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Longitude(double value, String unit) {
		super(value, unit);
	}
	
	@Override
	public String toString() {
		return "Longitude" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingLongitudePanel((Position)c);
	}
	
	/**
	 * tries to convert a given String into a valid Longitude object
	 */
	public static Longitude parse(String s) {
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
		nv.setThousandsSeparator(" ");
		
		value = nv.parse(number);			

		if (unit.equals(""))
			if (value <= 180)
				unit = "°";
			else 
				unit = "m";
		
		return new Longitude(value, unit);
	}
	
	@Override
	public Longitude forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Longitude(getValue(), getParsedUnit());
		else {
			String longitudeString = getTableElement().getValueFor(featureOfInterestPosition);
			return Longitude.parse(longitudeString);
		}
	}
}

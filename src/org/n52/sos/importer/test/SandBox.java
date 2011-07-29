package org.n52.sos.importer.test;

import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.position.Position;

public class SandBox {
public static void main(String[] args) {
	NumericValue nv = new NumericValue();
		nv.setDecimalSeparator(",");
	nv.setThousandsSeparator(" ");
	System.out.println(nv.parse("1"));
	
		Position p = new Position();
		p.setPattern("LAT n.Br. LON ö.L. ALT EPSG");
		Position p1 = (Position) p.parse("-72 n.Br. 7.3° ö.L. 50m 4236");
	}
}

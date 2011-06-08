package org.n52.sos.importer.model.table;

import java.awt.Color;
import java.awt.Point;

import org.n52.sos.importer.controller.TableController;

public class CellModel extends TableElement {
	
	private Point coordinates = new Point(-1, -1);

	public Point getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public void mark(Color color) {
		TableController.getInstance().colorCell(color, coordinates);
	}
}

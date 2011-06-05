package org.n52.sos.importer.bean;

import java.awt.Point;

public class TableConnection {

	private int columnNumber = -1;
	
	private int rowNumber = -1;

	private Point cellCoordinates = null;
	
	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public void setCellCoordinates(Point cellCoordinates) {
		this.cellCoordinates = cellCoordinates;
	}

	public Point getCellCoordinates() {
		return cellCoordinates;
	}
}

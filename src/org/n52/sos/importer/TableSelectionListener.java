package org.n52.sos.importer;

public interface TableSelectionListener {

	public void columnSelectionChanged(int oldColumn, int newColumn);

	public void rowSelectionChanged(int oldRow, int newRow);

}

package org.n52.sos.importer.model.resources;

import java.net.URI;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public abstract class Resource {
	
	private TableElement tableElement;
	
	private String name;
	
	private URI uri;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setURI(URI uri) {
		this.uri = uri;
	}

	public URI getURI() {
		return uri;
	}
	
	public String getURIString() {
		if (uri == null)
			return name;
		else 
			return uri.getPath();
	}
	
	public abstract void assign(MeasuredValue mv);
	
	public abstract boolean isAssigned(MeasuredValue mv);
	
	public abstract void unassign(MeasuredValue mv);

	public void setTableElement(TableElement tableElement) {
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	public String print(Cell measuredValuePosition) {
		if (tableElement == null)
			return this + "[name=" + name + ",URI=" + uri + "]";
		else 
			return this + "[name=" + tableElement.getValueFor(measuredValuePosition) + "]";
	}
	
	public abstract DefaultComboBoxModel getNames();
	
	public abstract DefaultComboBoxModel getURIs();
}

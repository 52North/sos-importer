package org.n52.sos.importer.bean;

import java.net.URI;

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
	
	public abstract void assign(MeasuredValue measuredValue);
	
	public abstract boolean isAssigned(MeasuredValue mv);
	
	public abstract void unassignFromMeasuredValues();

	public void setTableElement(TableElement tableElement) {
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
}

package org.n52.sos.importer.model.resources;

import java.net.URI;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.TableElement;

public abstract class Resource {
	
	private static final Logger logger = Logger.getLogger(Resource.class);
	
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
			return uri.toString();
	}
	
	public void setTableElement(TableElement tableElement) {
		logger.info("In " + tableElement + " are " + this + "s");
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	public abstract void assign(MeasuredValue mv);
	
	public abstract boolean isAssigned(MeasuredValue mv);
	
	public abstract void unassign(MeasuredValue mv);
	
	public abstract DefaultComboBoxModel getNames();
	
	public abstract DefaultComboBoxModel getURIs();
	
	public abstract List<Resource> getList();
	
	public abstract Resource getNextResourceType();
	
	@Override 
	public String toString() {
		if (getTableElement() == null)
			return " \"" + getName() + "\"";
		else 
			return " " + getTableElement();
	}
}

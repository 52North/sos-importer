package org.n52.sos.importer.model.resources;

import java.net.URI;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.TableElement;

/**
 * In this project, a resource has a URI and a name. This can be
 * a feature of interest, observed property, unit of measurement or
 * a sensor.
 * @author Raimund
 */
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
	
	public String getNameString() {
		if (name == null || name.equals(""))
			return uri.toString();
		else 
			return name;
	}
	
	public String getURIString() {
		if (uri == null || uri.toString().equals(""))
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
	
	/**
	 * indicates if the measured value is assigned to a resource of this type
	 * @param mv
	 * @return
	 */
	public abstract boolean isAssigned(MeasuredValue mv);
	
	/**
	 * indicates if the measured value is assigned to this resource
	 * @param mv
	 * @return
	 */
	public abstract boolean isAssignedTo(MeasuredValue mv);
	
	public abstract void unassign(MeasuredValue mv);
	
	public abstract DefaultComboBoxModel getNames();
	
	public abstract DefaultComboBoxModel getURIs();
	
	public abstract List<Resource> getList();
	
	public abstract Resource getNextResourceType();
	
	@Override 
	public String toString() {
		if (getTableElement() != null)
			return " " + getTableElement();
		if (getName() != null)
			return " \"" + getName() + "\"";
		return "";	
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tableElement == null) {
			if (other.tableElement != null)
				return false;
		} else if (!tableElement.equals(other.tableElement))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}
}

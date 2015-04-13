/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.model.resources;

import java.net.URI;
import java.util.List;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In this project, a resource has a URI and a name. This can be
 * a feature of interest, observed property, unit of measurement or
 * a sensor.<br />
 * Since version 2, a resource can be generated by other TableElements 
 * including a potential prefix and string used between the TableElements values.
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version 2
 */
public abstract class Resource extends Component {
	
	private static final Logger logger = LoggerFactory.getLogger(Resource.class);

	private TableElement tableElement;
	private String name;
	private URI uri;
	private String uriPrefix;
	private TableElement[] relatedCols;
	private String concatString;
	private boolean useNameAfterPrefixAsURI;
	private boolean generated;
	
	public void setName(final String name) { this.name = name; }
	public String getName() { return name; }
	public void setURI(final URI uri) { this.uri = uri; }
	public URI getURI() { return uri; }
	
	/**
	 * XML prefix for resources.
	 * @return should be xml:ID valid
	 */
	public abstract String XML_PREFIX();
	
	/**
	 * @return A String representing this type
	 */
	public abstract String getTypeName();
	
	/**
	 * Returns the name or alternatively the URI, when
	 * the name is null
	 * @return a String representation of this resource's name
	 */
	public String getNameString() {
		if (getName() == null || getName().equals("")) {
			return uri.toString();
		} else {
			return getName();
		}
	}
	
	/**
	 * Returns the URI or alternatively the name, when
	 * the URI is null
	 * @return a String representation of this resource's URI
	 */
	public String getURIString() {
		if (uri == null || uri.toString().equals("")) {
			return name;
		} else {
			return uri.toString();
		}
	}
	
	public void setTableElement(final TableElement tableElement) {
		logger.info("In " + tableElement + " are " + this + "s");
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	/**
	 * assign this resource to a measured value column or row
	 */
	public abstract void assign(MeasuredValue mv);
	
	/**
	 * indicates if the measured value is assigned to a resource of this type
	 */
	public abstract boolean isAssigned(MeasuredValue mv);
	
	/**
	 * indicates if the measured value is assigned to this resource
	 */
	public abstract boolean isAssignedTo(MeasuredValue mv);
	
	/**
	 * unassign this resource from a measured value column or row
	 */
	public abstract void unassign(MeasuredValue mv);
	
	/**
	 * get names of the resource stored in the properties file
	 */
	public abstract DefaultComboBoxModel getNames();
	
	/**
	 * get URIs of the resource stored in the properties file
	 */
	public abstract DefaultComboBoxModel getURIs();
	
	/**
	 * get all resources of this type in the model store
	 */
	public abstract List<Resource> getList();
	
	/**
	 * needed for iterating within one step
	 */
	public abstract Resource getNextResourceType();
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(final Combination c) {
		//not used since all resources have the same panel
		return null; 
	}
	
	/**
	 * returns the corresponding resource for a measured value cell
	 */
	public abstract Resource forThis(Cell measuredValuePosition);
	
	/**
	 * @return {@link Resource#XML_PREFIX} + {@link Resource#hashcode()}
	 */
	public String getXMLId() {
		return XML_PREFIX() + hashCode();
	}
	
	@Override 
	public String toString() {
		if (getTableElement() != null) {
			return " " + getTableElement();
		}
		if (getName() != null) {
			return " \"" + getName() + "\"";
		}
		return "";	
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((tableElement == null) ? 0 : tableElement.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Resource)) {
			return false;
		}
		final Resource other = (Resource) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (tableElement == null) {
			if (other.tableElement != null) {
				return false;
			}
		} else if (!tableElement.equals(other.tableElement)) {
			return false;
		}
		if (uri == null) {
			if (other.uri != null) {
				return false;
			}
		} else if (!uri.equals(other.uri)) {
			return false;
		}
		return true;
	}
	/**
	 * @return the uriPrefix
	 */
	public String getUriPrefix() {
		return uriPrefix;
	}
	/**
	 * @param uriPrefix the uriPrefix to set
	 */
	public void setUriPrefix(final String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}
	/**
	 * @return the relatedCols
	 */
	public TableElement[] getRelatedCols() {
		return relatedCols;
	}
	/**
	 * @param relatedCols the relatedCols to set
	 */
	public void setRelatedCols(final TableElement[] relatedCols) {
		this.relatedCols = relatedCols;
	}
	/**
	 * @return the concatString
	 */
	public String getConcatString() {
		return concatString;
	}
	/**
	 * @param concatString the concatString to set
	 */
	public void setConcatString(final String concatString) {
		this.concatString = concatString;
	}
	/**
	 * @return the useNameAfterPrefixAsURI
	 */
	public boolean isUseNameAfterPrefixAsURI() {
		return useNameAfterPrefixAsURI;
	}
	/**
	 * @param useNameAfterPrefixAsURI the useNameAfterPrefixAsURI to set
	 */
	public void setUseNameAfterPrefixAsURI(final boolean useNameAfterPrefixAsURI) {
		this.useNameAfterPrefixAsURI = useNameAfterPrefixAsURI;
	}
	/**
	 * If <code>true</code> this Resource is generated from other elements
	 * contained in the data file.
	 * @return the generated
	 */
	public boolean isGenerated() {
		return generated;
	}
	/**
	 * @param generated the generated to set
	 */
	public void setGenerated(final boolean generated) {
		this.generated = generated;
	}
	
	
}

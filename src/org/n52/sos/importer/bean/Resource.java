package org.n52.sos.importer.bean;

import java.net.URI;

public abstract class Resource extends TableConnection {
	
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
}

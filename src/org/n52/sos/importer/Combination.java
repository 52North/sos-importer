package org.n52.sos.importer;

public abstract class Combination implements Formatable, Parseable {

	/** for parsing */
	private String pattern;
	
	/** for merging */
	private String group;
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public String getGroup() {
		return group;
	}
}

package org.n52.sos.importer.interfaces;

/**
 * represents composed objects (e.g. position) which consist
 * of individual parts (e.g. latitude and longitude)
 * @author Raimund
 *
 */
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

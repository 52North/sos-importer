package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.bean.TableConnection;

public class DateAndTimeComponentModel extends TableConnection {

	private int value = -1;

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}

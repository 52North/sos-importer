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
package org.n52.sos.importer.view;

import javax.swing.JPanel;

import org.n52.sos.importer.combobox.EditableComboBoxItems;
import org.n52.sos.importer.combobox.EditableJComboBoxPanel;
import org.n52.sos.importer.view.utils.ToolTips;

/**
 * chooses the URL of the Sensor Observation Service
 * @author Raimund
 *
 */
public class Step7Panel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final EditableJComboBoxPanel sosComboBox;
	
	public Step7Panel() {
		sosComboBox = new EditableJComboBoxPanel(
				EditableComboBoxItems.getInstance().getSosURLs(), "SOS-URL", ToolTips.get("SOS"));
		this.add(sosComboBox);
	}
	
	public String getSOSURL() {
		return (String) sosComboBox.getSelectedItem();
	}
}



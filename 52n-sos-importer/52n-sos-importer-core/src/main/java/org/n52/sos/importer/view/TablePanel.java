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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * contains the table which is used by step panels 3, 4 and 5
 * @author Raimund
 *
 */
public class TablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static TablePanel instance = null;
	
	private final JTable table;
	
	private TablePanel() { 
		super();
		setBorder(new TitledBorder(null, Lang.l().dataPreview(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		/*gridBagLayout.columnWidths = new int[]{438, 0};
		gridBagLayout.rowHeights = new int[]{273, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};*/
		setLayout(gridBagLayout);
		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(Constants.DIALOG_WIDTH - 100, Constants.DIALOG_HEIGHT-450));
		JScrollPane scrollPane = new JScrollPane(table);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
		if (Constants.GUI_DEBUG) {
			setBorder(Constants.DEBUG_BORDER);
			scrollPane.setBorder(Constants.DEBUG_BORDER);
		}
	}
	
	public static TablePanel getInstance() {
		if (instance == null) 
			instance = new TablePanel();
		return instance;
	}

	public JTable getTable() {
		return table;
	}
}

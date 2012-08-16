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

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.view.i18n.Lang;
import org.n52.sos.importer.view.position.MissingPositionPanel;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step6cPanel extends JPanel {

	private static final long serialVersionUID = 1L;

		private MissingPositionPanel mpp;
		
		public Step6cPanel(String description,
				String featureOfInterestName,
				Step6cModel s6cM) {
			String questionStatement = String.format("%s %s ", description, Lang.l().featureOfInterest());
			setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
			JLabel questionStartLabel = new JLabel(questionStatement);
			JLabel foiName = new JLabel(featureOfInterestName);
			foiName.setFont(Constants.DEFAULT_LABEL_FONT_BOLD);
			
			JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
			descriptionPanel.add(questionStartLabel);
			descriptionPanel.add(foiName);
			descriptionPanel.add(new JLabel("?"));
			add(descriptionPanel, BorderLayout.NORTH);
			
			mpp = new MissingPositionPanel(s6cM);
			add(mpp);
			if (Constants.GUI_DEBUG) {
				descriptionPanel.setBorder(Constants.DEBUG_BORDER);
				mpp.setBorder(Constants.DEBUG_BORDER);
			}
		}

		public boolean isFinished() {
			return mpp.isFinished();
		}

		public void saveSettings() {
			mpp.saveSettings();
		}

		public void loadSettings() {
			mpp.loadSettings();
		}
		
}

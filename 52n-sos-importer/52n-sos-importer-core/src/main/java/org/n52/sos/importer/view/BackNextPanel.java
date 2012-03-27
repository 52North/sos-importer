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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.n52.sos.importer.controller.BackNextController;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * panel for back and next (and finish) button at the bottom of the main frame
 * @author Raimund
 *
 */
public class BackNextPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static BackNextPanel instance = null;

	private final JButton back = new JButton(Lang.l().backButtonLabel());
	private final JButton next = new JButton(Lang.l().nextButtonLabel());
	private final JButton finish = new JButton(Lang.l().finishButtonLabel());
	
	private BackNextPanel() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.add(back);
		this.add(next);
		this.add(finish);
		this.finish.setVisible(false);
		
		this.back.addActionListener(new BackButtonClicked());
		this.next.addActionListener(new NextButtonClicked());
		this.finish.addActionListener(new FinishButtonClicked());
	}

	public static BackNextPanel getInstance() {
		if (instance == null)
			instance = new BackNextPanel();
		return instance;
	}
	
	public void setBackButtonVisible(boolean flag) {
		back.setVisible(flag);
	}
	
	public void changeNextToFinish() {
		next.setVisible(false);
		finish.setVisible(true);
	}
	
	public void changeFinishToNext() {
		finish.setVisible(false);
		next.setVisible(true);
	}
	
	public void setFinishButtonEnabled(boolean aFlag) {
		finish.setEnabled(aFlag);
	}
	
	public void setNextButtonEnabled(boolean isNextEnabled) {
		next.setEnabled(isNextEnabled);
	}
	
	private class BackButtonClicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			BackNextController.getInstance().backButtonPressed();
		}
	}
	
	private class NextButtonClicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			BackNextController.getInstance().nextButtonClicked();
		}
	}
	
	private class FinishButtonClicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			BackNextController.getInstance().finishButtonClicked();
		}
	}
}

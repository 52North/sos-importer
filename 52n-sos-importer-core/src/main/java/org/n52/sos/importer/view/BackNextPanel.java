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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
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

	private JButton back;
	private JButton next;
	private JButton finish;
	
	private BackNextPanel() {
		super();
		if(Constants.GUI_DEBUG) {
			setBorder(Constants.DEBUG_BORDER);
		}
		GridLayout layout = new GridLayout(1,3);
		setLayout(layout);
		/*
		 * 	BACK button on the left
		 */
		back = new JButton(Lang.l().backButtonLabel());
		back.addActionListener(new BackButtonClicked());
		JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		backPanel.add(this.back);
		add(backPanel);
		/*
		 * EMPTY panel in the middle
		 */
		JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		add(emptyPanel);
		/*
		 * NEXT,FINISH on the right
		 */
		JPanel nextFinishPanel  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		// finish
		finish = new JButton(Lang.l().finishButtonLabel());
		finish.setVisible(false);
		finish.addActionListener(new FinishButtonClicked());
		nextFinishPanel.add(this.finish);
		// next
		next = new JButton(Lang.l().nextButtonLabel());
		next.addActionListener(new NextButtonClicked());
		nextFinishPanel.add(this.next);
		add(nextFinishPanel);
	}

	public static BackNextPanel getInstance() {
		if (instance == null) {
			instance = new BackNextPanel();
		}
		// workaround for language switch
		instance.back.setText(Lang.l().backButtonLabel());
		instance.finish.setText(Lang.l().finishButtonLabel());
		instance.next.setText(Lang.l().nextButtonLabel());
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
			BackNextController.getInstance().backButtonClicked();
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

package org.n52.sos.importer.view;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.n52.sos.importer.controller.BackNextController;

public class BackNextPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static BackNextPanel instance = null;

	private final JButton back = new JButton("Back");
	private final JButton next = new JButton("Next");
	private final JButton finish = new JButton("Finish");
	
	private BackNextPanel() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(back);
		this.add(next);
		this.add(finish);
		finish.setVisible(false);
		
		back.addActionListener(new BackButtonClicked());
		next.addActionListener(new NextButtonClicked());
		finish.addActionListener(new FinishButtonClicked());
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

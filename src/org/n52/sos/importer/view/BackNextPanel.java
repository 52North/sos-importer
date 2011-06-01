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
	
	private BackNextPanel() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(back);
		this.add(next);
		
		back.addActionListener(new BackButtonClicked());
		next.addActionListener(new NextButtonClicked());
	}

	public static BackNextPanel getInstance() {
		if (instance == null)
			instance = new BackNextPanel();
		return instance;
	}
	
	public void setBackButtonEnabled(boolean flag) {
		back.setEnabled(flag);
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
}

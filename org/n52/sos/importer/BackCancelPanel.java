package org.n52.sos.importer;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BackCancelPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JButton back = new JButton("Back");
	private final JButton next = new JButton("Next");
	private StepPanel stepPanel;
	
	public BackCancelPanel() {
		back.addActionListener(new BackButtonClicked());
		next.addActionListener(new NextButtonClicked());
		
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
		this.add(back);
		this.add(next);
	}
	
	public void setStepPanel(StepPanel stepPanel) {
		this.stepPanel = stepPanel;
	}
	
	private class BackButtonClicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			stepPanel.back();
		}
	}
	
	private class NextButtonClicked implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			stepPanel.next();
		}
	}
}

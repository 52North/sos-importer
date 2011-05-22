package org.n52.sos.importer;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public abstract class ButtonGroupPanel extends SelectionPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected ButtonGroup group = new ButtonGroup();
	
	public ButtonGroupPanel(MainFrame mainFrame, JPanel containerPanel) {
		super(mainFrame, containerPanel);
        this.setLayout(new GridLayout(0, 1));
	}
	
	protected void addRadioButton(String name) {
		JRadioButton radioButton = new JRadioButton(name);
		if (group.getButtonCount() == 0) 
			radioButton.setSelected(true);
		group.add(radioButton);
		this.add(radioButton);
		radioButton.addActionListener(new RemoveChildPanel());
	}
	
	protected void addRadioButton(String name, SelectionPanel panel) {
		JRadioButton radioButton = new JRadioButton(name);
		group.add(radioButton);
		this.add(radioButton);
		radioButton.addActionListener(new AddChildPanel(name, panel));
		childSelectionPanels.put(name, panel);
	}

	@Override
	public void setSelection(String s) {
		ButtonModel m = null;
		Enumeration<AbstractButton> e = group.getElements();
		while(e.hasMoreElements()) {
			JRadioButton b = (JRadioButton) e.nextElement();
			if (s.equals(b.getText())) {
				m = b.getModel();
				break;
			}
		}	
		group.setSelected(m, true);	
	}
	
	@Override
	public void setDefaultSelection() {
		ButtonModel firstButton = group.getElements().nextElement().getModel();
		group.setSelected(firstButton, true);
	}

	@Override
	public String getSelection() {
		Enumeration<AbstractButton> e = group.getElements();
		while(e.hasMoreElements()) {
			JRadioButton b = (JRadioButton) e.nextElement();
			if (b.isSelected()) {
				return b.getText();
			}
		}
		return null;
	}
	
	private class AddChildPanel implements ActionListener {
		SelectionPanel panel;
		String name;
		
		public AddChildPanel(String name, SelectionPanel panel) {
			this.name = name;
			this.panel = panel;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (getChildSelectionPanel() != null) 
				getChildSelectionPanel().removeFromContainerPanel();
			panel.getContainerPanel().add(panel);
			setChildSelectionPanel(name);
			getMainFrame().pack();
			selectionChanged();
		}	
	}
	
	private class RemoveChildPanel implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			selectionChanged();
			if (getChildSelectionPanel() != null) {
				getChildSelectionPanel().removeFromContainerPanel();
				getMainFrame().pack();
			}
		}		
	}
}

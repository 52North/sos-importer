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

import org.n52.sos.importer.view.MainFrame;

public abstract class ButtonGroupPanel extends SelectionPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected ButtonGroup group = new ButtonGroup();
	
	public ButtonGroupPanel(MainFrame mainFrame, JPanel containerPanel) {
		super(mainFrame, containerPanel);
        this.setLayout(new GridLayout(0, 1));
	}
	
	protected void addRadioButton(String name) {
		JRadioButton radioButton = new JRadioButton(name);
		radioButton.addActionListener(new RemoveChildPanel());
		if (group.getButtonCount() == 0) 
			radioButton.setSelected(true);
		group.add(radioButton);
		this.add(radioButton);
	}
	
	protected void addRadioButton(String name, SelectionPanel childPanel) {
		JRadioButton radioButton = new JRadioButton(name);
		addChildPanel(name, childPanel);
		radioButton.addActionListener(new AddChildPanel(childPanel));
		if (group.getButtonCount() == 0) {
			radioButton.setSelected(true);
			setSelectedChildPanel(childPanel);
		}
		group.add(radioButton);
		this.add(radioButton);
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
		JRadioButton firstButton = (JRadioButton) group.getElements().nextElement();
		group.setSelected(firstButton.getModel(), true);
		setSelectedChildPanel(firstButton.getText());
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
		SelectionPanel childPanel;
		
		public AddChildPanel(SelectionPanel childPanel) {
			this.childPanel = childPanel;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (getSelectedChildPanel() != null) 
				getSelectedChildPanel().removeFromContainerPanel();
			
			setSelectedChildPanel(childPanel);
			childPanel.addToContainerPanel();		
			
			getMainFrame().pack();
			selectionChanged();
		}	
	}
	
	private class RemoveChildPanel implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (getSelectedChildPanel() != null) {
				getSelectedChildPanel().removeFromContainerPanel();
				getMainFrame().pack();
				
				SelectionPanel childPanel = null;
				setSelectedChildPanel(childPanel);
			}
			selectionChanged();
		}		
	}
}

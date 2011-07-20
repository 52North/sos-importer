package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.n52.sos.importer.Parseable;
import org.n52.sos.importer.controller.TableController;

public class ParsingTestPanel extends SelectionPanel {

	private static final long serialVersionUID = 1L;
	
	private final ParseTestLabel parseTestLabel;
	
	public ParsingTestPanel(JPanel containerPanel, Parseable parser) {
		super(containerPanel);
		parseTestLabel = new ParseTestLabel(parser);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(parseTestLabel);
	}

	@Override
	protected void setSelection(String s) {				
	}

	@Override
	protected String getSelection() {
		return "0";
	}

	@Override
	public void setDefaultSelection() {	
	}
	
	@Override
	protected void reinit() {
		parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
	}		
}

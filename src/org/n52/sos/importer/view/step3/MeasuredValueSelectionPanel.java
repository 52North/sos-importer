package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.table.TableElement;

public class MeasuredValueSelectionPanel extends SelectionPanel {

	private static final long serialVersionUID = 1L;
	
	private final ParseTestLabel parseTestLabel;
	
	private MeasuredValue measuredValue;
	
	public MeasuredValueSelectionPanel(JPanel containerPanel, MeasuredValue measuredValue) {
		super(containerPanel);
		this.measuredValue = measuredValue;
		parseTestLabel = new ParseTestLabel(measuredValue);
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
	
	protected void reinit() {
		parseTestLabel.parseValues(TableController.getInstance().getMarkedValues());
	}		
	
	@Override
	public void assign(TableElement tableElement) {
		measuredValue.setTableElement(tableElement);
		ModelStore.getInstance().add(measuredValue);
	}
	
	@Override
	public void unassign(TableElement tableElement) {
		MeasuredValue measuredValueToRemove = null;
		for (MeasuredValue mv: ModelStore.getInstance().getMeasuredValues())
			if (tableElement.equals(mv.getTableElement())) {
				measuredValueToRemove = mv;
				break;
			}
				
		ModelStore.getInstance().remove(measuredValueToRemove);
	}
}

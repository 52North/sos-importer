package org.n52.sos.importer.view.step3;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.n52.sos.importer.model.resources.OmParameter;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.i18n.Lang;

public class OmParameterSelectionPanel extends SelectionPanel {
    
    private JLabel parameterNameLabel;
    private JTextField parameterNameTextField;
    private OmParameter omParameter;

    public OmParameterSelectionPanel(JPanel containerPanel, OmParameter omParameter) {
        super(containerPanel);
        this.omParameter = omParameter;
        parameterNameLabel = new JLabel(Lang.l().step3OmParameterNameLabel() + ":");
        parameterNameTextField = new JTextField(20);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(parameterNameLabel);
        add(parameterNameTextField);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void setSelection(String parameterName) {
        parameterNameTextField.setText(parameterName);
    }
    
    @Override
    protected String getSelection() {
        return parameterNameTextField.getText();
    }
    
    @Override
    public void setDefaultSelection() {
        parameterNameTextField.setText("");
    }
    
    @Override
    public void assign(TableElement tableElement) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void unAssign(TableElement tableElement) {
        // TODO Auto-generated method stub
        
    }
    
}

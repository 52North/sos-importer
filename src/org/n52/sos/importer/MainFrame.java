package org.n52.sos.importer;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final StepDescriptionPanel stepDescriptionPanel = new StepDescriptionPanel();
	private final JPanel stepPanel = new JPanel();
	private final BackCancelPanel backCancelPanel = new BackCancelPanel();
	
	private final Step1Panel step1Panel = new Step1Panel(this);
	private final Step2Panel step2Panel = new Step2Panel(this);
	private final TablePanel tablePanel = new TablePanel();
	private final Step3Panel step3Panel = new Step3Panel(this);
	
	public MainFrame() {
		this.setTitle("CSV to SOS");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().add(stepDescriptionPanel);
		this.getContentPane().add(stepPanel);
		this.getContentPane().add(backCancelPanel);
		
		this.setStepPanel(step1Panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void setStepPanel(StepPanel newStepPanel) {		
		stepPanel.removeAll();
		stepPanel.add(newStepPanel);
	    stepDescriptionPanel.setText(newStepPanel.getDescription());
		backCancelPanel.setStepPanel(newStepPanel);
		this.pack();
	}

	public Step1Panel getStep1Panel() {
		return step1Panel;
	}
	
	public Step2Panel getStep2Panel() {
		return step2Panel;
	}
	
	public Step3Panel getStep3Panel() {
		return step3Panel;
	}
	
	public TablePanel getTablePanel() {
		return tablePanel;
	}
	
	public BackCancelPanel getBackCancelPanel() {
		return backCancelPanel;
	}
	
	private void exitDialog() {
		int n = JOptionPane.showConfirmDialog(
			    this, "Do you really want to exit?\n"
			    + "Your work progress will be lost.\n",
			    "Exit", JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);
		if (n == JOptionPane.YES_OPTION) System.exit(0);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new MainFrame();
            }
        });
	}
}

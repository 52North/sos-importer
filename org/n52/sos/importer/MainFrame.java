package org.n52.sos.importer;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private StepPanel stepPanel;
	private final StepDescriptionPanel stepDescriptionPanel = new StepDescriptionPanel();
	private final BackCancelPanel backCancelPanel = new BackCancelPanel();
	
	public MainFrame() {
		this.setTitle("CSV to SOS");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setStepPanel(new Step1(this));
		redraw();


		this.setVisible(true);
	}
	
	private void redraw() {
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().add(stepDescriptionPanel);
		this.getContentPane().add(stepPanel);
		this.getContentPane().add(backCancelPanel);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void setStepPanel(StepPanel stepPanel) {
		this.stepPanel = stepPanel; 
		stepDescriptionPanel.setText(stepPanel.getDescription());
		backCancelPanel.setStepPanel(stepPanel);
		redraw();
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

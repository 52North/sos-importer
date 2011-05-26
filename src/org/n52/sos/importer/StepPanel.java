package org.n52.sos.importer;
import javax.swing.JPanel;


public abstract class StepPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final MainFrame mainFrame;

	public StepPanel(MainFrame mainFrame) {
		super();
		this.mainFrame = mainFrame;
	}
	
	protected abstract void loadSettings();
	
	protected abstract String getDescription();
	
	protected abstract void back();
	
	protected abstract void next();

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}

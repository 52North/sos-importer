package org.n52.sos.importer.view;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.n52.sos.importer.combobox.ComboBoxItems;
import org.n52.sos.importer.controller.MainController;

/**
 * the actual frame of the application which can swap the 
 * different step panels
 * @author Raimund
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final MainController mainController;
	
	private final JPanel stepContainerPanel = new JPanel();
	private final DescriptionPanel descriptionPanel = DescriptionPanel.getInstance();
	private final BackNextPanel backNextPanel = BackNextPanel.getInstance();
	
	public MainFrame(MainController mainController) {
		super();
		this.mainController = mainController;
		this.setTitle("SOS Importer");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowChanged());
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		this.getContentPane().add(descriptionPanel);
		this.getContentPane().add(stepContainerPanel);
		this.getContentPane().add(backNextPanel);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	public void setStepPanel(JPanel stepPanel) {		
		stepContainerPanel.removeAll();
		stepContainerPanel.add(stepPanel);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void showExitDialog() {
		int n = JOptionPane.showConfirmDialog(
			    this, "Do you really want to exit?\n",
			    "Exit", JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);

		if (n == JOptionPane.YES_OPTION) {
			ComboBoxItems.getInstance().save();
			System.exit(0);
		}
	}
	
	private class WindowChanged implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {		
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			mainController.exit();		
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}		
	}
}

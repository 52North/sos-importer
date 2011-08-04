package org.n52.sos.importer.tooltips;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ToolTipManager;

/**
 * loads tooltips from the properties file
 * @author Raimund
 *
 */
public class ToolTips {
	private static final String BUNDLE_NAME = "org.n52.sos.importer.tooltips.tooltips"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private ToolTips() {
	}

	public static String get(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static void loadSettings() {
		ToolTipManager.sharedInstance().setInitialDelay(500);
		ToolTipManager.sharedInstance().setDismissDelay(50000);
		ToolTipManager.sharedInstance().setReshowDelay(500);
	}
}

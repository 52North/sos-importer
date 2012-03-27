package org.n52.sos.importer.view.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;

public abstract class Lang {
	
	private static Lang instance = null;
	
	private static Locale currentLocale = Locale.ENGLISH;// <-- default language
	
	private static HashMap<Locale,Lang> availableLocales = new HashMap<Locale, Lang>();

	private static final Logger logger = Logger.getLogger(Lang.class);
	
	public static Lang l() {
		if(instance == null) {
			instance = availableLocales.get(currentLocale);
		}
		return instance;
	}
	
	static {
		Lang.availableLocales.put(Locale.ENGLISH, new En());
		En.setCurrentLocale(Locale.ENGLISH);
		Lang.availableLocales.put(Locale.GERMAN, new De());
	}
	
	public abstract Locale getLocale();
	public abstract String backButtonLabel();
	public abstract String errorDialogTitle();
	public abstract String exitDialogQuestion(); 
	public abstract String exitDialogTitle();
	public abstract String featureOfInterest();
	public abstract String file();
	public abstract String finishButtonLabel();
	public String frameTitle(){	return "SOS Importer 0.2 RC1"; }
	/**
	 * @param file
	 * @param path
	 * @return - file: "<code>file</code>" (path: "<code>path</code>")
	 */
	public String frameTitleExtension(String file, String path) {
		return " - " + this.file() + ":\"" + file + "\" (" + this.path() + ": \"" + path + "\")";
	}
	public abstract String infoDialogTitle();
	public abstract String measuredValue();
	public abstract String nextButtonLabel();
	public abstract String numValuePanelDecimalSeparator();
	public abstract String numValuePanelThousandsSeparator();
	public abstract String observation();
	public abstract String observedProperty();
	public abstract String path();
	public abstract String position();
	public abstract String sensor();
	/**
	 * @return Sensor Observation Service
	 */
	public String sos() {
		return "Sensor Observation Service";
	}
	public abstract String spaceString();
	public abstract String step();
	public abstract String step1BrowseButton();
	public abstract String step1Description();
	public abstract String step1File();
	public abstract String step1SelectLanguage();
	public abstract String step2ColumnSeparator();
	public abstract String step2CommentIndicator();
	public abstract String step2Description();
	public abstract String step2FirstLineWithData();
	public abstract String step2ParseHeader();
	public abstract String step2TextQualifier();
	public abstract String step3aDescription();
	public abstract String step3bDescription();
	public abstract String step3ColTypeDateTime();
	public abstract String step3ColTypeDoNotExport();
	public abstract String step3ColTypeMeasuredValue();
	public abstract String step3ColTypeUndefined();
	public abstract String step3DateAndTimeCombination();
	public abstract String step3DateAndTimeUnixTime();
	public abstract String step3MeasuredValBoolean();
	public abstract String step3MeasuredValCount();
	public abstract String step3MeasuredValNumericValue();
	public abstract String step3MeasuredValText();
	public abstract String step3PositionCombination();
	public abstract String step4aDescription();
	public abstract String step4aInfoDateAndTime();
	public abstract String step4aInfoMeasuredValue();
	public abstract String step4aModelDescription();
	public abstract String step4bDescription();
	public abstract String step4bInfoNotMeasuredValue();
	/**
	 * @param resource resource.className already set for this <code>En.measuredValue()</code>.
	 * @return
	 */
	public String step4bInfoResoureAlreadySet(Resource resource) {
		String res = "RESOURCE NOT FOUND!";
		if (resource instanceof FeatureOfInterest) {
			res = this.featureOfInterest();
		} else if (resource instanceof ObservedProperty) {
			res = this.observedProperty();
		} else if (resource instanceof Sensor) {
			res = this.sensor();
		} else if (resource instanceof UnitOfMeasurement) {
			res = this.unitOfMeasurement();
		}
		return res + this.step4bInfoResourceAlreadSetText() + this.measuredValue();
	}
	protected abstract String step4bInfoResourceAlreadSetText();
	public abstract String step4bModelDescription();
	public abstract String step5aDescription();
	public abstract String step5aModelDescription();
	public abstract String step5cDescription();
	public abstract String step5cModelDescription();
	public abstract String step6aDescription();
	public abstract String step6bDescription();
	public abstract String step6bModelDescription();
	public abstract String step6bSpecialDescription();
	public abstract String step6bSpecialModelDescription();
	public abstract String step6cDescription();
	public abstract String step6cModelDescription();
	public abstract String step7Description();
	public abstract String step7SOSconnectionFailed(String strURL,int responseCode);
	public abstract String step7SOSConnectionFailedException(String strURL,String message);
	public abstract String step8Description();
	public abstract String step8ErrorLable(int i);
	public abstract String step8InsertObservationLabel(int i);
	public abstract String step8LogFileLabel();
	public abstract String step8RegisterSensorLabel(int i);
	public abstract String step8SuccessLabel(int i);
	public abstract String unitOfMeasurement();
	public abstract String warningDialogTitle();
	/**
	 * @return the currentLocale
	 */
	public static Locale getCurrentLocale() {
		return currentLocale;
	}
	/**
	 * @param newLocale the currentLocale to set
	 */
	public static void setCurrentLocale(Locale newLocale) {
		if (logger.isTraceEnabled()) {
			logger.trace("setCurrentLocale(" + newLocale + ")");
		}
		// when the locale is changed, reset is and change instance object
		if(!Lang.currentLocale.equals(newLocale)) {
			Lang.currentLocale = newLocale;
			Lang.instance = availableLocales.get(newLocale);
		}
	}
	
	public static Locale[] getAvailableLocales() {
		Set <Locale> locales = availableLocales.keySet();
		return locales.toArray(new Locale[locales.size()]);
	}
}
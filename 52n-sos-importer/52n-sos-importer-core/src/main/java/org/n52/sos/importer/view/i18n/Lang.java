package org.n52.sos.importer.view.i18n;

import java.io.File;
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
	
	private static HashMap<Locale,Lang> availableLocales = new HashMap<Locale, Lang>();
	
	private static Locale currentLocale = Locale.ENGLISH;// <-- default language
	
	private static Lang instance = null;

	private static final Logger logger = Logger.getLogger(Lang.class);
	
	static {
		Lang.availableLocales.put(Locale.ENGLISH, new En());
		En.setCurrentLocale(Locale.ENGLISH);
		Lang.availableLocales.put(Locale.GERMAN, new De());
	}
	
	public static Locale[] getAvailableLocales() {
		Set <Locale> locales = availableLocales.keySet();
		return locales.toArray(new Locale[locales.size()]);
	}
	
	/**
	 * @return the currentLocale
	 */
	public static Locale getCurrentLocale() {
		return currentLocale;
	}
	public static Lang l() {
		if(instance == null) {
			instance = availableLocales.get(currentLocale);
		}
		return instance;
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
	/**
	 * @return Altitude / Height
	 */
	public abstract String altitude(); 
	/**
	 * @return and
	 */
	public abstract String and();
	public abstract String backButtonLabel();
	public abstract String day();
	/**
	 * @return EPSG-Code
	 */
	public abstract String epsgCode();
	/**
	 * @return The EPSG-Code has to be a natural number.
	 */
	public abstract String epsgCodeWarningDialogNaturalNumber();
	/**
	 * @return The EPSG-Code has to be in the range of 0 and 32767.
	 */
	public abstract String epsgCodeWarningDialogOutOfRange();
	/**
	 * @return Error
	 */
	public abstract String error();
	/**
	 * @return Error
	 */
	public abstract String errorDialogTitle();
	/**
	 * @return Example
	 */
	public abstract String example();
	public abstract String exitDialogQuestion();
	public abstract String exitDialogTitle();
	public abstract String featureOfInterest();
	public abstract String file();
	public abstract String finishButtonLabel();
	/**
	 * @return Format
	 */
	public abstract String format();
	public String frameTitle(){	return "SOS Importer 0.2 RC1"; }
	/**
	 * @param file
	 * @param path
	 * @return - file: "<code>file</code>" (path: "<code>path</code>")
	 */
	public String frameTitleExtension(String file, String path) {
		return " - " + this.file() + ":\"" + file + "\" (" + this.path() + ": \"" + path + "\")";
	}
	public abstract Locale getLocale();
	/**
	 * @return Group
	 */
	public abstract String group();
	/**
	 * @return The height has to be a decimal number.
	 */
	public abstract String heightWarningDialogDecimalNumber();
	/**
	 * @return Hours
	 */
	public abstract String hours();
	public abstract String infoDialogTitle();
	/**
	 * @return The latitude/northing can only be a decimal number so far.
	 */
	public abstract String latitudeDialogDecimalValue();
	/**
	 * @return Latitude / Northing
	 */
	public abstract String latitudeNorthing();
	/**
	 * @return The longitude/easting can only be a decimal number so far.
	 */
	public abstract String longitudeDialogDecimalValue();
	/**
	 * @return Longitude / Easting
	 */
	public abstract String longitudeEasting();
	public abstract String measuredValue();
	/**
	 * @return Minutes
	 */
	public abstract String minutes();
	/**
	 * @return Month
	 */
	public abstract String month();
	public abstract String nextButtonLabel();
	public abstract String numValuePanelDecimalSeparator();
	public abstract String numValuePanelThousandsSeparator();
	public abstract String observation();
	public abstract String observedProperty();
	public abstract String path();
	public abstract String position();
	/**
	 * @return Reference System
	 */
	public abstract String referenceSystem();
	/**
	 * @return Seconds
	 */
	public abstract String seconds();
	public abstract String sensor();
	/**
	 * @return Sensor Observation Service
	 */
	public String sos() {
		return "Sensor Observation Service";
	}
	/**
	 * @return SOS-URL
	 */
	public abstract String sosURL();
	public abstract String spaceString();
	public abstract String step();
	public abstract String step1BrowseButton();
	public abstract String step1Description();
	public abstract String step1File();
	public abstract String step1SelectLanguage();
	public abstract String step2ColumnSeparator();
	public abstract String step2CommentIndicator();
	/**
	 * @return CSV-Data-Preview
	 */
	public abstract String step2DataPreviewLabel();
	public abstract String step2Description();
	public abstract String step2FirstLineWithData();
	public abstract String step2ParseHeader();
	public abstract String step2TextQualifier();
	public abstract String step3aDescription();
	/**
	 * @return You have to specify at least one measured value column.
	 */
	public abstract String step3aMeasureValueColMissingDialogMessage();
	/**
	 * @return Measured value column missing
	 */
	public abstract String step3aMeasureValueColMissingDialogTitle();
	/**
	 * @return 1 value not parseable.
	 */
	public abstract String step3aParseTest1Failed();
	/**
	 * @return All values parseable.
	 */
	public abstract String step3aParseTestAllOk();
	/**
	 * @param n
	 * @return <code>n</code> values not parseable.
	 */
	public abstract String step3aParseTestNFailed(int n);
	/**
	 * @return The type for this column is "undefined" please select one. Chose "Do Not Export" for skipping it.
	 */
	public abstract String step3aSelectedColTypeUndefinedMsg();
	/**
	 * @return Column Type is "undefined"
	 */
	public abstract String step3aSelectedColTypeUndefinedTitle();
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
	protected abstract String step4bInfoResourceAlreadSetText();
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
	/**
	 * @return The selected config file folder <code>folder</code> is not
	 * 			accessible for the application. 
	 */
	public abstract String step7ConfigDirNotDirOrWriteable(String folder);
	/**
	 * @return Choose configuration file export folder
	 */
	public abstract String step7ConfigFileButton();
	/**
	 * @return Select Config file folder
	 */
	public abstract String step7ConfigFileDialogTitel();
	/**
	 * @return Configuration file folder and name
	 */
	public abstract String step7ConfigFileLabel();
	public abstract String step7Description();
	/**
	 * @return Directly import data during next step
	 */
	public abstract String step7DirectImport();
	/**
	 * @return Save configuration to XML file
	 */
	public abstract String step7SaveConfig();
	public abstract String step7SOSconnectionFailed(String strURL,int responseCode);
	public abstract String step7SOSConnectionFailedException(String strURL,String message);
	/**
	 * @return Open Configuration File
	 */
	public abstract String step8ConfigFileButton();
	public abstract String step8Description();
	public abstract String step8ErrorLable(int i);
	public abstract String step8InsertObservationLabel(int i);
	public abstract String step8LogFileButton();
	public abstract String step8RegisterSensorLabel(int i);
	/**
	 * @param f The file which was used to save the XML model
	 * @param excpetionText A short String describing the exception cause 
	 * 						during saving process
	 * @return The configuration could not be saved to file
	 * 						<br />"<code>f</code>".
	 * 						<br />An Exception occurred:
	 * 						<br /><code>exceptionText</code>.
	 * 						<br />Please check the log file for more details.
	 */
	public abstract String step8SaveModelFailed(File f, String exceptionText);
	public abstract String step8SuccessLabel(int i);

	/**
	 * @return Time
	 */
	public abstract String time();

	/**
	 * @return UTC offset
	 */
	public abstract String timeZone();

	/**
	 * @return Unit
	 */
	public abstract String unit();

	public abstract String unitOfMeasurement();

	public abstract String warningDialogTitle();

	/**
	 * @return Year
	 */
	public abstract String year();
}

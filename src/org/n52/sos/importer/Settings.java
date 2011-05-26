package org.n52.sos.importer;

public class Settings {

	private static String csvFilePath;
	
	private static String columnSeparator;
	
	private static String commentIndicator;
	
	private static String textQualifier;

	private static String thousandsSeparator;
	
	private static String decimalSeparator;
	
	public static void setCSVFilePath(String csvFilePath) {
		Settings.csvFilePath = csvFilePath;
	}

	public static String getCSVFilePath() {
		return csvFilePath;
	}

	public static void setColumnSeparator(String columnSeparator) {
		Settings.columnSeparator = columnSeparator;
	}

	public static String getColumnSeparator() {
		return columnSeparator;
	}

	public static void setCommentIndicator(String commentIndicator) {
		Settings.commentIndicator = commentIndicator;
	}

	public static String getCommentIndicator() {
		return commentIndicator;
	}

	public static void setTextQualifier(String textQualifier) {
		Settings.textQualifier = textQualifier;
	}

	public static String getTextQualifier() {
		return textQualifier;
	}

	public static void setThousandsSeparator(String thousandsSeparator) {
		Settings.thousandsSeparator = thousandsSeparator;
	}

	public static String getThousandsSeparator() {
		return thousandsSeparator;
	}

	public static void setDecimalSeparator(String decimalSeparator) {
		Settings.decimalSeparator = decimalSeparator;
	}

	public static String getDecimalSeparator() {
		return decimalSeparator;
	}
}

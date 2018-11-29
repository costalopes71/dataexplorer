package com.sinapsisenergia.dataexplorer.analyser;

public class FieldAnalyser {

	public static boolean isInteger(String field) {
		try {
			Integer.parseInt(field);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isDouble(String field) {
		try {
			Double.parseDouble(field);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isDate(String field) {
		return field.matches("^([1-9]|([012][0-9])|(3[01]))/([0]{0,1}[1-9]|1[012])/\\d\\d\\d\\d [012]{0,1}[0-9]:[0-6][0-9]$");
	}
	
}
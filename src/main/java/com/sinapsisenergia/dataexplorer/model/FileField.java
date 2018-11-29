package com.sinapsisenergia.dataexplorer.model;

public class FileField {

	private String fieldName;
	private int numberOfNulls;
	private int numberOfEmptys;
	private int numberOfFilled;
	private boolean dominio = false;

	public boolean isDominio() {
		return dominio;
	}

	public void setDominio(boolean dominio) {
		this.dominio = dominio;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public int getNumberOfNulls() {
		return numberOfNulls;
	}

	public void addOneToNumberOfNulls() {
		++this.numberOfNulls;
	}

	public int getNumberOfEmptys() {
		return numberOfEmptys;
	}

	public void addOneToNumberOfEmptys() {
		++this.numberOfEmptys;
	}

	public int getNumberOfFilled() {
		return numberOfFilled;
	}

	public void addOneToNumberOfFilled() {
		++this.numberOfFilled;
	}

}
package com.sinapsisenergia.dataexplorer.model;

public class FileField {
	
	private String fileName;
	private String fieldName;
	private FieldType type;
	private int numberOfNulls;
	private int numberOfEmptys;
	private int numberOfFilled;
	private String fieldExamples;
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

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public void setNumberOfNulls(int numberOfNulls) {
		this.numberOfNulls = numberOfNulls;
	}

	public void setNumberOfEmptys(int numberOfEmptys) {
		this.numberOfEmptys = numberOfEmptys;
	}

	public void setNumberOfFilled(int numberOfFilled) {
		this.numberOfFilled = numberOfFilled;
	}

	public String getFieldExamples() {
		return fieldExamples;
	}

	public void setFieldExamples(String fieldExamples) {
		this.fieldExamples = fieldExamples;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(fileName).
		append(";").append(fieldName).
		append(";").append(type.toString()).
		append(";").append(numberOfNulls).
		append(";").append(numberOfFilled).
		append(";").append(numberOfEmptys).
		append(";").append(dominio ? "SIM" : "NAO").
		append(";").append(fieldExamples);
		return sb.toString();
	}
	
}
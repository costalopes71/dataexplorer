package com.sinapsisenergia.dataexplorer.analyser;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldAnalyserTest {

	@Test
	public void isIntegerWithMinusSignal() {
		boolean isInteger = FieldAnalyser.isInteger("-5689");
		assertEquals(true, isInteger);
	}

	@Test
	public void isIntegerWithPlusSignal() {
		assertEquals(true, FieldAnalyser.isInteger("+87236"));
	}
	
	@Test
	public void isIntegerWithoutSign() {
		assertEquals(true, FieldAnalyser.isInteger("42566"));
	}
	
	@Test
	public void isNotInteger() {
		assertEquals(false, FieldAnalyser.isInteger("983232.66"));
	}
	
	@Test
	public void isNotIntegerWithCharacters() {
		assertEquals(false, FieldAnalyser.isInteger("8732l"));
	}
	
	@Test
	public void isNotIntegerWithSignsOtherThenBeggining() {
		assertEquals(false, FieldAnalyser.isInteger("8+3732"));
	}
	
	@Test(expected = NumberFormatException.class)
	public void cannotParseEmptyStringToInt() {
		Integer.parseInt(" ");
	}

	@Test
	public void isNotDoubleWithComma() {
		assertEquals(false, FieldAnalyser.isDouble("92,3"));
	}
	
	@Test
	public void isDoubleWithPoint() {
		assertEquals(true, FieldAnalyser.isDouble("92,3".replaceAll(",", ".")));
	}

	@Test
	public void isValidDateFormat() {
		assertEquals(true, FieldAnalyser.isDate("11/05/1214 01:25"));
	}
	
}

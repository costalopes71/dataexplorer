package com.sinapsisenergia.dataexplorer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.junit.Test;

public class SinapsisUtilTest {

	@Test
	public void listAllFilesRecursively() {
		List<File> files;
		
		files = SinapsisUtil.listFiles(Constants.FILES_DIRECTORY);
		
		assertEquals(673, files.size());
	}

	@Test
	public void createPrintWriter() {
		OutputStreamWriter pw = null;
		try {
			pw = SinapsisUtil.createOutputStreamWriter(Constants.DESINATION_DIR);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		assertNotNull(pw);
	}
	
	@Test
	public void openBufferedReader() {
		File file = SinapsisUtil.listFiles(Constants.FILES_DIRECTORY).get(0);
		BufferedReader br = null;
		try {
			br = SinapsisUtil.openBufferedReader(file);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		}
		assertNotNull(br);
	}
	
	@Test
	public void countNumberOfLinesInFile() {
		File file = new File(Constants.FILES_DIRECTORY + "/2014-09/2014-09-SC_Contas_Analitico_PROLAGOS.csv");
		int lines = 0;
		try {
			lines = SinapsisUtil.countTotalFileLines(file);
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		assertEquals(516258, lines);
	}
	
	@Test
	public void removeQuotes() {
		String example = "\"O Joao nao gosta de \"no meio da frase\""; 
		example = example.replaceAll("\"", "");
		assertEquals("O Joao nao gosta de no meio da frase", example);
	}
	
}
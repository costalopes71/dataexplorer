package com.sinapsisenergia.dataexplorer.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SinapsisUtil {

	public static List<File> listFiles(String directoryName) {
		List<File> files = new ArrayList<>();
		
		try {
			File directory = new File(directoryName);
			File[] filesArray = directory.listFiles();
			files.addAll(Arrays.asList(filesArray));
			
			for (File file : filesArray) {
				if (file.isDirectory()) {
					files.remove(file);
					files.addAll(listFiles(file.getAbsolutePath()));
				}
			}
			
			return files;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao adquirir os arquivos do diretorio especificado: [" + directoryName + "].");
		}
		
	}

	public static OutputStreamWriter createOutputStreamWriter(String desinationDir) throws IOException {
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(desinationDir);
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Nao foi possivel criar o arquivo de resultados. Erro: " + e.getMessage());
		}
		return new OutputStreamWriter(outputStream, Charset.forName("Windows-1252"));
	}
	
	public static BufferedReader openBufferedReader(File file) throws FileNotFoundException {
		FileInputStream inputStream;
		InputStreamReader isr;
		try {
			inputStream = new FileInputStream(file);
			isr = new InputStreamReader(inputStream, Charset.forName("Windows-1252"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FileNotFoundException("Arquivo [" + file.getAbsolutePath() + "] nao encontrado.");
		}
		return new BufferedReader(isr);
	}

	public static int countTotalFileLines(File file) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean endsWithoutNewLine = false;
			while ((readChars = is.read(c)) != -1) {
				for (int i = 0; i < readChars; i++) {
					if (c[i] == '\n') {
						++count;
					}
				}
				endsWithoutNewLine = (c[readChars - 1] != '\n');
			}
			if (endsWithoutNewLine) {
				++count;
			}
			return count;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("Erro ao tentar contar as linhas do arquivo [" + file.getName() + "].");
		} finally {
			is.close();
		}
	}

	public static void removeQuotes(String line) {
		line.replaceAll("\"", "");
	}
	
}
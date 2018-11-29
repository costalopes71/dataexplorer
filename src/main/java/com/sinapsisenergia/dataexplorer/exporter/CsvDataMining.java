package com.sinapsisenergia.dataexplorer.exporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sinapsisenergia.dataexplorer.analyser.FieldAnalyser;
import com.sinapsisenergia.dataexplorer.model.FileField;
import com.sinapsisenergia.dataexplorer.util.SinapsisUtil;

public class CsvDataMining {

	private OutputStreamWriter pw;
	private StringBuilder sb;
	private Map<Integer, FileField> fieldsInformationsMap;
	private static final String RESULT_FILE_HEADER = "NOME ARQUIVO;CAMPO;TIPO;TOTAL NULOS;TOTAL COM DADOS;TOTAL VAZIOS;DOMINIO;EXEMPLO 1;EXEMPLO 2;EXEMPLO 3;EXEMPLO 4;EXEMPLO 5;EXEMPLO 6;EXEMPLO 7;EXEMPLO 8;EXEMPLO 9;EXEMPLO 10";
	
	public void analyzeData(String inputDirectoryName, String outputDirectoryPath) throws IOException {
		
		//
		// obtendo os arquivos de entrada (recursivamente em todos os subdiretorios do diretorio informado)
		//
		List<File> files = SinapsisUtil.listFiles(inputDirectoryName);
		
		int totalFiles = files.size();
		System.out.println(totalFiles + " arquivos para processar.");
		
		//
		// certificando que o diretorio onde o arquivo de resultado sera gerado existe
		//
		File resultDirectory = new File(outputDirectoryPath);
		if (!resultDirectory.exists()) {
			resultDirectory.mkdirs();
		}
		
		//
		// criando o print writer para escrever o arquivo de resultado
		//
		pw = SinapsisUtil.createOutputStreamWriter(outputDirectoryPath);
		
		//
		// escreve o header do arquivo de resultado
		//
		pw.write(RESULT_FILE_HEADER);
		pw.write("\n");
		
		int counter = 0;
		for (File file : files) {
			
			System.out.println("Processando [" + ++counter + "/" + totalFiles + "] - [" + file.getName() + "]");
			//
			// obtendo o nome do arquivo
			//
			String nomeArquivo = file.getName();
			
			//
			// abrindo conexao com o arquivo
			//
			BufferedReader br = SinapsisUtil.openBufferedReader(file);
			
			//
			// Recuperando o cabecalho
			//
			String header = br.readLine().replaceAll("\"", "");
			
			//
			// obtendo mapa de Campos vazios/nulos/com data
			//
			fieldsInformationsMap = createFieldsInformationsMap(file);
			
			//
			// obtendo amostra de linhas do arquivo (1000 registros) 
			//
			int totalLinesInFile = SinapsisUtil.countTotalFileLines(file);
			List<String> sample = getOneThousandLinesSample(totalLinesInFile, br);
			
			// 
			// obtendo 10% de amostragem de dados
			//
			List<String> tenPercentSample = getTenPercentLinesSample(file, totalLinesInFile);
			
			//
			// tokenizando o header
			//
			String[] fields = header.split(";");
			
			//
			// iterando sobre cada campo para escrever a linha de cada
			//
			String fieldName;
			String fieldType;
			String resultLine;
			String fieldExamples;
			for (int fieldIndex = 0; fieldIndex < fields.length; fieldIndex++) {
				fieldName = fields[fieldIndex];
				fieldType = analyzeFieldType(fieldIndex, sample);
				
				if (fieldType.equalsIgnoreCase("TEXTO")) {
					if (isDominio(tenPercentSample, fieldIndex)) {
						fieldsInformationsMap.get(fieldIndex).setDominio(true);
						fieldExamples = getTenFirstDomainsV2(fieldIndex, file);
					} else {
						fieldExamples = getFieldExamples(fieldIndex, sample);
					}
				} else {
					fieldExamples = getFieldExamples(fieldIndex, sample);
				}
				
				resultLine = buildResultLine(nomeArquivo, 
						fieldName, 
						fieldType, 
						fieldExamples, 
						fieldsInformationsMap.get(fieldIndex).getNumberOfNulls(), 
						fieldsInformationsMap.get(fieldIndex).getNumberOfFilled(), 
						fieldsInformationsMap.get(fieldIndex).getNumberOfEmptys(),
						fieldsInformationsMap.get(fieldIndex).isDominio());
				
				pw.write(resultLine);
				pw.write("\n");
			}
			
			//
			// fechando o recurso de leitura
			//
			br.close();
		}
		
		//
		// salvando o arquivo de resultado
		//
		pw.close();
	}

	private String getFieldExamples(int fieldIndex, List<String> sample) {
		StringBuilder fieldExample = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			String registry = null;
			try {
				registry = sample.get(i);
			} catch (IndexOutOfBoundsException e) {
				fieldExample.append(";");
				continue;
			}
			String tokens[] = registry.split("\";");
			String field = tokens[fieldIndex].replaceAll("\"", "");
			fieldExample.append(field).append(";");
		}
		return fieldExample.toString();
	}
	
	private String getTenFirstDomainsV2(int fieldIndex, File file) throws IOException {
		BufferedReader reader = SinapsisUtil.openBufferedReader(file);
		reader.readLine();
		Set<String> domains = new HashSet<>();
		
		String line;
		while ((line = reader.readLine()) != null) {
			String[] tokens = line.split("\";");
			String field = tokens[fieldIndex].replaceAll("\"", "");
			
			if (field == null || field.equals("") || field.equalsIgnoreCase("null")) {
				continue;
			}
			
			domains.add(field);
			
			if (domains.size() == 10) {
				break;
			}
			
		}
		
		List<String> aux = new ArrayList<>(domains);
		if (aux.size() < 10) {
			for (int i = aux.size(); i < 10; i++) {
				aux.add("");
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (String domain : aux) {
			sb.append(domain).append(";");
		}
		
		reader.close();
		domains.clear();
		aux.clear();
		System.gc();
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	private String getTenFirstDomains(int fieldIndex, List<String> sample) {
		Set<String> domains = new HashSet<>();
		
		for (int i = 0; i < sample.size(); i++) {
			String registry = sample.get(i);
			String[] tokens = registry.split("\";");
			String field = tokens[fieldIndex].replaceAll("\"", "");
			
			if (field == null || field.equals("") || field.equalsIgnoreCase("null")) {
				continue;
			}
			
			domains.add(field);
			
			if (domains.size() == 10) {
				break;
			}
		}
		
		List<String> aux = new ArrayList<>(domains);
		if (aux.size() < 10) {
			for (int i = aux.size(); i < 10; i++) {
				aux.add("");
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (String domain : aux) {
			sb.append(domain).append(";");
		}
		
		domains.clear();
		aux.clear();
		System.gc();
		return sb.toString();
	}

	private Map<Integer, FileField> createFieldsInformationsMap(File file) throws IOException {
		Map<Integer, FileField> map = new HashMap<>();
		BufferedReader reader = SinapsisUtil.openBufferedReader(file);
		
		//
		// pulando o header
		//
		reader.readLine();
		
		String line = null;
		String[] tokens = null;
		while ((line = reader.readLine()) != null) {
			tokens = line.split("\";");
			
			for (int i = 0; i < tokens.length; i++) {
				if (map.containsKey(i)) {
					analyseData(map.get(i), tokens[i]);
				} else {
					map.put(i, new FileField());
					analyseData(map.get(i), tokens[i]);
				}
				
			}
		}
		
		reader.close();
		return map;
	}

	private void analyseData(FileField fileField, String data) {
		data = data.replaceAll("\"", "");
		if (data.equalsIgnoreCase("null")) {
			fileField.addOneToNumberOfNulls();
		} else if (data.equals("")) {
			fileField.addOneToNumberOfEmptys();
		} else {
			fileField.addOneToNumberOfFilled();
		}
	}

	private String buildResultLine(String nomeArquivo, String fieldName, String fieldType, String fieldExamples, int nulls, int filleds, int emptys, boolean dominio) {
		sb = new StringBuilder();
		sb.append(nomeArquivo).
		append(";").append(fieldName).
		append(";").append(fieldType).
		append(";").append(nulls).
		append(";").append(filleds).
		append(";").append(emptys).
		append(";").append(dominio ? "SIM" : "NAO").
		append(";").append(fieldExamples);
		return sb.toString();
	}

	private List<String> getOneThousandLinesSample(int totalLines, BufferedReader br) throws IOException {
		List<String> sample = new ArrayList<>();
		
		if (totalLines < 1000) {
			for (int i = 0; i < totalLines - 1; i++) {
				sample.add(br.readLine());
			}
		} else {
			for (int i = 0; i < 1000; i++) {
				sample.add(br.readLine());
			}
		}
		return sample;
	}

	private String analyzeFieldType(int fieldIndex, List<String> sample) {
		
		if (allFieldsAreNull(fieldIndex, sample)) {
			return "NULO";
		} else if (allFieldsAreInteger(fieldIndex, sample)) {
			return "INTEIRO";
		} else if (allFieldsAreDouble(fieldIndex, sample)) {
			return "REAL";
		} else if (allFieldsAreDate(fieldIndex, sample)) {
			return "DATA";
		} else {
			return "TEXTO";
		}
	}

	private boolean allFieldsAreDate(int fieldIndex, List<String> sample) {
		for (String registry : sample) {
			String tokens[] = registry.split("\";");
			String field = tokens[fieldIndex].replaceAll("\"", "");
			if (!FieldAnalyser.isDate(field)) {
				if (field.equalsIgnoreCase("null")) {
					continue;
				}
				return false;
			}
		}
		return true;
	}

	private boolean allFieldsAreDouble(int fieldIndex, List<String> sample) {
		for (String registry : sample) {
			String tokens[] = registry.split("\";");
			String field = tokens[fieldIndex].replaceAll(",", ".");
			field = field.replaceAll("\"", "");
			if (!FieldAnalyser.isDouble(field)) {
				if (field.equalsIgnoreCase("null")) {
					continue;
				}
				return false;
			}
		}
		return true;
	}

	private boolean allFieldsAreNull(int fieldIndex, List<String> sample) {
		for (String registry : sample) {
			String[] tokens = registry.split("\";");
			String field = tokens[fieldIndex].replaceAll("\"", "");
			if (!field.equalsIgnoreCase("null")) {
				return false;
			}
		}
		return true;
	}

	private boolean allFieldsAreInteger(int fieldIndex, List<String> sample) {
		for (String registry : sample) {
			String[] tokens = registry.split("\";");
			String field = tokens[fieldIndex].replaceAll("\"", "");
			if (!FieldAnalyser.isInteger(field)) {
				if (field.equalsIgnoreCase("null")) {
					continue;
				}
				return false;
			}
		}
		return true;
	}

	private List<String> getTenPercentLinesSample(File file, int totalLines) throws IOException {
		List<String> tenPercentSample = new ArrayList<>();
		BufferedReader br = SinapsisUtil.openBufferedReader(file);
		
		//
		// pula o header
		//
		br.readLine();
		
		if (totalLines < 500) {
			for (int i = 0; i < totalLines-1; i++) {
				tenPercentSample.add(br.readLine());
			}
			return tenPercentSample;
		}
		
		int tenPercent = totalLines / 10;
		for (int i = 0; i < tenPercent; i++) {
			tenPercentSample.add(br.readLine());
		}
		
		return tenPercentSample;
	}
	
	private boolean isDominio(List<String> sample, int fieldIndex) {
		long minimumRepetitionRequired = 1;
		
		if (sample.size() > 50 && sample.size() < 100) {
			minimumRepetitionRequired = Math.round(sample.size() * 0.02);
		} else if (sample.size() >= 100) {
			minimumRepetitionRequired = Math.round(sample.size() * 0.05);
		}
		
		int counter = 0;
		String wordToRepeat = null;
		List<String> auxSample = new ArrayList<>(sample);
		
		for (int j = 0; j < sample.size(); j++) {
			String[] tokens = sample.get(j).split("\";");
			
			String token = tokens[fieldIndex].replaceAll("\"", "");
			if (!token.equals("") && !token.equalsIgnoreCase("null")) {
				wordToRepeat = token;
				auxSample.remove(j);
				break;
			}
		}
		
		if (wordToRepeat == null) {
			return false;
		}
		
		for (int i = 0; i < auxSample.size(); i++) {
			String tokens[] = auxSample.get(i).split("\";");
			String token = tokens[fieldIndex].replaceAll("\"", ""); 
			if (token.equals(wordToRepeat)) {
				++counter;
				if (counter == minimumRepetitionRequired) {
					return true;
				}
			}
		}
		return false;
	}
	
}

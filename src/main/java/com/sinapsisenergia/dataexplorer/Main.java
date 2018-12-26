package com.sinapsisenergia.dataexplorer;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

import com.sinapsisenergia.dataexplorer.exporter.CsvDataMining;

/**
 * @author Joao Costa Lopes
 * @since 28/11/2018
 * @version beta 1.1
 */
public class Main {

	private static String input;
	private static String output;
	
    public static void main( String[] args ) {
    	Instant start = Instant.now();
//    	long start = System.nanoTime();
    	
    	//
    	// exibe o menu de boas vindas e solicita o caminho dos arquivos e o caminho onde o arquivo de resultado deve ser gravado
    	//
    	printWelcome();
    	
    	System.out.println("Iniciando...");
    	
    	//
    	// normalizando o input do usuario
    	//
    	normalizeUserInput(input, output);
    	
    	try {
    		CsvDataMining dataTransform = new CsvDataMining();
			dataTransform.analyzeData(input, output);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro FATAL ao tentar processar os arquivos. O programa será encerrado.\nErro: " + e.getMessage());
		}
    	
    	//
    	// calcula o tempo de processamento total do programa e imprime o FIM
    	//
    	Instant end = Instant.now();
    	printGoodbye(Duration.between(start, end).toSeconds());
    }

	private static void normalizeUserInput(String input2, String output2) {
		input = input.replaceAll("\\\\", "/").trim();
    	output = output.replaceAll("\\\\", "/").trim();
	}

	private static void printGoodbye(long elapsed) {
		System.out.println("==================================================================");
    	System.out.println("=================== FIM! Obrigado por usar! ======================");
    	System.out.println("==================================================================");
    	System.out.println("Elapsed time: " + elapsed + " segundos.");
	}

	private static void printWelcome() {
		Scanner scan = new Scanner(System.in);
		System.out.println("==================================================================");
    	System.out.println("====================== CSV Data Explorer =========================");
    	System.out.println("==================================================================");
    	System.out.println("");
    	System.out.println("Digite o caminho completo do diretorio onde se encontram os arquivos a processar: ");
    	System.out.print("--> ");
    	input = scan.next() + scan.nextLine();
    	System.out.println("Digite o caminho completo do diretorio onde voce deseja que o arquivo de resultado seja salvo: ");
    	output = scan.next() + scan.nextLine();
    	scan.close();
	}
    
}
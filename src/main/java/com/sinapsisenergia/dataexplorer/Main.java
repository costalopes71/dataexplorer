package com.sinapsisenergia.dataexplorer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.sinapsisenergia.dataexplorer.exporter.CsvDataMining;

/**
 * @author Joao Costa Lopes
 * @since 28/11/2018
 * @version beta 1.1
 */
public class Main {
	
    public static void main( String[] args ) {
    	long start = System.nanoTime();
    	Scanner teclado = new Scanner(System.in);
    	CsvDataMining dataTransform = new CsvDataMining();

    	System.out.println("==================================================================");
    	System.out.println("====================== CSV Data Explorer =========================");
    	System.out.println("==================================================================");
    	System.out.println("");
    	System.out.println("Digite o caminho completo do diretorio onde se encontram os arquivos a processar: ");
    	String input = teclado.next() + teclado.nextLine();
    	System.out.println("Digite o caminho completo do diretorio onde voce deseja que o arquivo de resultado seja salvo: ");
    	String output = teclado.next() + teclado.nextLine();
    	System.out.println("Iniciando...");
    	
    	input = input.replaceAll("\\\\", "/");
    	output = output.replaceAll("\\\\", "/");
    	teclado.close();
    	
    	try {
			dataTransform.analyzeData(input, output);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Erro FATAL ao tentar processar os arquivos. O programa será encerrado.");
		}
    	
    	long elapsed = System.nanoTime() - start;
    	System.out.println("==================================================================");
    	System.out.println("=================== FIM! Obrigado por usar! ======================");
    	System.out.println("==================================================================");
    	System.out.println("Elapsed time: " + TimeUnit.NANOSECONDS.toSeconds(elapsed) + " segundos.");
    }
    
}
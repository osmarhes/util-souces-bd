package com.projeto.util.testelinhacomando;

public class TesteLinhaComando {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i=0; i<args.length; i++){
			System.out.println("Variavel n. " + i + ", valor " + args[i]);
		}
	}
}

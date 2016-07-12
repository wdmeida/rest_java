package br.com.cervejaria.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class Cliente {
	public static void main(String[] args) throws IOException{
		/*
		 * Para desenvolver um cliente para o arquivo xml, basta utilizar a API de I/O do Java,
		 * começando pela classe java.net.URL, que indica qual o caminho a ser seguido.
		 */
		String caminho = "file:///home/wagner/Documentos/JavaWeb/rest_java/"
				+ "cervejaria/src/br/com/cervejaria/cliente/clientes.xml";
		URL url = new URL(caminho);
		
		/*
		 * Conecta ao endereço definido na URL e abre um canal de leitura de dados com o endereço indicado. Para
		 * conectar-se, basta utilizar o método openConnection da URL, e para abrir o inputStream, basta utilizar o
		 * método getInputStream;
		 */
		InputStream inputStream = url.openConnection().getInputStream();
		
		/*
		 * Encapsula a leitura dos dados em um BufferedReader. Esta classe possui um método utilitário para ler 
		 * informações linha a linha, facilitando o processo de leitura de dados. Para utilizá-lo, no entando, 
		 * é necessário encapsular a InputStream em um InputStreamReader:
		 */
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		
		/*
		 * Uma vez feito esse procedimento, basta utilizar o método readLine em um laço, até que este método retorne null:
		 */
		String line = null;
		
		while ((line = bufferedReader.readLine()) != null){
			System.out.println(line);
		}
	}
	
	
}

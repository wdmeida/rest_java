package br.com.cervejaria.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/clientes", loadOnStartup = 1)
public class ClientesServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private String clientes;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//Retorna os dados do arquivo de clientes.
		resp.getWriter().print(clientes);
	}
	
	//Faz a leitura do arquivo clientes.xml e coloca na String clientes.
	@Override
	public void init() throws ServletException {
		
		StringBuffer line = new StringBuffer();
		String caminho = "file:///home/wagner/Documentos/JavaWeb/rest_java/"
				+ "cervejaria/src/br/com/cervejaria/cliente/clientes.xml";
		
		try {
			
			URL url = new URL(caminho);
		
			InputStream inputStream = url.openConnection().getInputStream();
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			while (bufferedReader.ready()){
				line.append(bufferedReader.readLine());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		clientes = line.toString();
		super.init();
	}
	
	
}

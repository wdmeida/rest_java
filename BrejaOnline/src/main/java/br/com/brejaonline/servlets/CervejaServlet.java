package br.com.brejaonline.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamWriter;

import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import br.com.brejaonline.exception.RecursoSemIdentificadorException;
import br.com.brejaonline.model.Cerveja;
import br.com.brejaonline.model.Estoque;
import br.com.brejaonline.model.rest.Cervejas;

@SuppressWarnings("restriction")
@WebServlet(value = "/cervejas/*")
public class CervejaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Estoque estoque = new Estoque();
	private static javax.xml.bind.JAXBContext context;
	
	static {
		try {
			context = javax.xml.bind.JAXBContext.newInstance(Cervejas.class);
		} catch (javax.xml.bind.JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
	// Implementa o método responsável por obter um recurso do web service.
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//	Obtém o cabeçalho para processar o formato requerido pelo usuário.
		String acceptHeader = req.getHeader("Accept");		
		
		if (acceptHeader == null || acceptHeader.contains("application/xml")) {
			escreveXML(req, resp);
		} else if (acceptHeader.contains("application/json")) {
			escreveJSON(req, resp);
		} else {
			//	O header accept foi recebido com um valor não suportado.
			resp.sendError(415); //Formato não suportado.
		}
		
	}//doGet()
	
	private void escreveXML(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Object objetoAEscrever = localizaObjetoASerEnvidado(req);
		
		if (objetoAEscrever == null) {
			resp.sendError(404); //Objeto não encontrado
			return;
		}
		
		try {
			resp.setContentType("application/xml;charset=UTF-8");
			Marshaller marchaller = context.createMarshaller();
			marchaller.marshal(objetoAEscrever, resp.getWriter());
			
		} catch (JAXBException e) {
			resp.sendError(500); //Erro interno inesperado.
		}
	}//escreveXML()
	
	//	Este código assume o Jettison como provedor de mapeamento JSON.
	private void escreveJSON(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Object objetoAEscrever = localizaObjetoASerEnvidado(req);
		
		if (objetoAEscrever == null) {
			resp.sendError(404); //Objeto não encontrado
			return;
		}
		
		try {
			resp.setContentType("application/json;charset=UTF-8");
			MappedNamespaceConvention con = new MappedNamespaceConvention();
			
			XMLStreamWriter xmlStreamWriter =  new MappedXMLStreamWriter(con, resp.getWriter());
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(objetoAEscrever, xmlStreamWriter);
			
		} catch (JAXBException e) {
			resp.sendError(500);
		}
	}//escreveJSON()
	
	private String obtemIdentificador(HttpServletRequest req) throws RecursoSemIdentificadorException{
		String requestUri = req.getRequestURI();
		
		String[] pedacosDaUri = requestUri.split("/");
		
		boolean contextoCervejasEncontrado = false;
		for (String contexto : pedacosDaUri) {
			if (contexto.equals("cervejas")) {
				contextoCervejasEncontrado = true;
				continue;
			}
			
			if (contextoCervejasEncontrado) {
				try {
					//Tenta decodificar usando o charset UTF-8.
					return URLDecoder.decode(contexto, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					//Caso o charset não seja encontrado.
					return URLDecoder.decode(contexto);
				}
			}
		}
		
		throw new RecursoSemIdentificadorException("Recurso sem identificador");
	}//obtemIdentificador()
	
	private Object localizaObjetoASerEnvidado(HttpServletRequest req) {
		Object objeto = null;
		
		try {
			String identificador = obtemIdentificador(req);
			objeto = estoque.recuperarCervejaPeloNome(identificador);
		} catch (RecursoSemIdentificadorException e) {
			Cervejas cervejas = new Cervejas();
			cervejas.setCervejas(new ArrayList<Cerveja>(estoque.listarCervejas()));
			objeto = cervejas;
		}
		return objeto;
	}//localizaObjetoASerEnviado()
	
	//	Método responsável por criar um recurso.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			String identificador = null;
			try {
				identificador = obtemIdentificador(req);
			} catch (RecursoSemIdentificadorException e) {
				resp.sendError(400, e.getMessage()); //Manda um erro 400 - Bad Request
			}
			
			if (identificador != null && estoque.recuperarCervejaPeloNome(identificador) != null) {
				resp.sendError(409, "Já existe uma cerveja com esse nome");
				return;
			}
			
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Cerveja cerveja = (Cerveja) unmarshaller.unmarshal(req.getInputStream());
			cerveja.setNome(identificador);
			estoque.adicionarCerveja(cerveja);
			String requestURI = req.getRequestURI();
			resp.setHeader("Location", requestURI);
			resp.setStatus(201); //Recurso criado com sucesso.
			escreveXML(req, resp);
		} catch (JAXBException e) {
			// TODO: handle exception
		}
	}
	
	
}//class CervejaServlet

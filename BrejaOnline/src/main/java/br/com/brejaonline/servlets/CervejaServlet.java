package br.com.brejaonline.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String acceptHeader = req.getHeader("Accept");		
/*		
		try {
			Marshaller marshaller = context.createMarshaller();
			resp.setContentType("application/xml;charset=UTF-8");
			PrintWriter out = resp.getWriter();
			
			Cervejas cervejas = new Cervejas();
			cervejas.setCervejas(new ArrayList<Cerveja>(estoque.listarCervejas()));
			marshaller.marshal(cervejas, out);
		} catch (Exception e) {
			resp.sendError(500, e.getMessage());
		}*/
		
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
		
		Cervejas cervejas = new Cervejas();
		cervejas.setCervejas(new ArrayList<Cerveja>(estoque.listarCervejas()));
		
		try {
			resp.setContentType("application/xml;charset=UTF-8");
			Marshaller marchaller = context.createMarshaller();
			marchaller.marshal(cervejas, resp.getWriter());
			
		} catch (JAXBException e) {
			resp.sendError(500); //Erro interno inesperado.
		}
	}//escreveXML()
	
	private void escreveJSON(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Cervejas cervejas = new Cervejas();
		cervejas.setCervejas(new ArrayList<Cerveja>(estoque.listarCervejas()));
		
		try {
			resp.setContentType("application/json;charset=UTF-8");
			MappedNamespaceConvention con = new MappedNamespaceConvention();
			
			XMLStreamWriter xmlStreamWriter =  new MappedXMLStreamWriter(con, resp.getWriter());
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(cervejas, xmlStreamWriter);
		} catch (JAXBException e) {
			resp.sendError(500);
		}
	}
}//class CervejaServlet

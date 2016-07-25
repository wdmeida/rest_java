package br.com.brejaonline.exception;

public class RecursoSemIdentificadorException extends Exception {

	private static final long serialVersionUID = 1L;

	public RecursoSemIdentificadorException() { super("O recurso informado não possuí identificador válido."); }

	public RecursoSemIdentificadorException(String arg0) { super(arg0); }
}//class RecursoSemIdentificadorException

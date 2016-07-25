package br.com.brejaonline.exception;

public class RecursoSemIdentificadorException extends Exception {

	private static final long serialVersionUID = 1L;

	public RecursoSemIdentificadorException() { super("O recurso informado n�o possu� identificador v�lido."); }

	public RecursoSemIdentificadorException(String arg0) { super(arg0); }
}//class RecursoSemIdentificadorException

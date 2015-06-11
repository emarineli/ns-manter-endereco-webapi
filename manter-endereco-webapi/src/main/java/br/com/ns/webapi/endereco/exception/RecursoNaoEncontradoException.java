package br.com.ns.webapi.endereco.exception;

/**
 * Exceção geral para recursos que não puderam ser encontrados.
 * 
 * @author Erico Marineli
 * @version 1.0, 10/06/2015
 * @since 1.0
 *
 */
public class RecursoNaoEncontradoException extends RuntimeException {

	/**
	 * Serial default.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão para levar ao cliente a mensagem de erro.
	 * 
	 * @param message
	 *            mensagem de erro.
	 */
	public RecursoNaoEncontradoException(String message) {
		super(message);
	}

}

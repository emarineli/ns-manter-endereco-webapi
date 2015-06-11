package br.com.ns.webapi.endereco.exception.handler;

import static br.com.ns.webapi.endereco.util.ConversaoUtil.fromJSON;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.web.client.HttpClientErrorException;

import br.com.ns.webapi.endereco.exception.RecursoNaoEncontradoException;
import br.com.ns.webapi.endereco.util.MensagemErro;

/**
 * Error handler para consumo de serviços REST.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,11/06/2015
 * @since 1.0
 *
 */
public class HttpClientErrorHandler {

	/**
	 * Realiza o handle do erro para lançar a exceção correta.
	 * 
	 * @param e
	 *            exception específica a ser tratada.
	 * 
	 * @throws RecursoNaoEncontradoException
	 *             lançado para erros 404.
	 * @throws RuntimeException
	 *             lançado para os demais erros.
	 */
	public static void handleHttpClientError(HttpClientErrorException e) {

		/* O recurso possui mensagem padronizada de erro */
		MensagemErro erro = fromJSON(e.getResponseBodyAsString(),
				MensagemErro.class);

		if (NOT_FOUND.compareTo(e.getStatusCode()) == 0) {
			throw new RecursoNaoEncontradoException(erro.getMensagem());
		} else {

			throw new RuntimeException(erro.getMensagem());
		}
	}

}

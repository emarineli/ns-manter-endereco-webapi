package br.com.ns.webapi.endereco.util;

/**
 * Representa a abstração de uma mensagem de erro que será enviada ao cliente da
 * API.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0, 09/06/2015
 * @since 1.0
 *
 */
public class MensagemErro {

	private String mensagem;

	/**
	 * Construtor padrão.
	 */
	public MensagemErro() {
	}

	public MensagemErro(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Informa a mensagem de erro.
	 * @param mensagem mensagem de erro.
	 */
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * Obtém a mensagem de erro encapsulada.
	 * 
	 * @return mensagem de erro.
	 */

	public String getMensagem() {
		return mensagem;
	}

}

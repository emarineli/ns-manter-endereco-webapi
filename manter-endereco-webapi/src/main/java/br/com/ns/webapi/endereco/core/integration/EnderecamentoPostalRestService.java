package br.com.ns.webapi.endereco.core.integration;

import br.com.ns.webapi.endereco.core.integration.modelo.Endereco;

/**
 * Interface de integração com serviço REST de Endereçamento Postal.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 */
public interface EnderecamentoPostalRestService {

	/**
	 * Obtém um endereço pelo seu código de endereçamento.
	 * 
	 * @param cep
	 *            código de endereçamento postal.
	 * @return entidade que representa o cep caso encontrado.
	 */
	public Endereco obterEnderecoPorCep(String cep);
}

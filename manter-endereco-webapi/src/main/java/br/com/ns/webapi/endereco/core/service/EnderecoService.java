package br.com.ns.webapi.endereco.core.service;

import br.com.ns.webapi.endereco.modelo.Endereco;

/**
 * Interface responsável por expor as operações da entidade Endereco.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 *
 */
public interface EnderecoService {

	/**
	 * Obtém um endereço pelo seu identificador primário.
	 * 
	 * @param id
	 *            identificador primário do endereço.
	 * @return a representação do Endereço caso encontrado ou um código 404.
	 */
	public Endereco obterEnderecoPeloIdentificador(Long id);

	/**
	 * Remove um endereço pelo seu identificador primário.
	 * 
	 * @param id
	 *            identificador primário do endereço.
	 * @return se existia um registro a ser deletado.
	 */
	public boolean removerEnderecoPeloIdentificador(Long id);

	/**
	 * Cria um endereço.
	 * 
	 * @param endereco
	 *            representação da entidade.
	 * @return endereço criado.
	 */
	public Endereco criarEndereco(Endereco endereco);

	/**
	 * Atualiza um endereço.
	 * 
	 * @param endereco
	 *            endereço a ser atualizado.
	 * @return
	 */
	public Endereco atualizarEndereco(Endereco endereco);

}

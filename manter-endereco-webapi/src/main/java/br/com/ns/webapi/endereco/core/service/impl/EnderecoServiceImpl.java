package br.com.ns.webapi.endereco.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ns.webapi.endereco.core.integration.EnderecamentoPostalRestService;
import br.com.ns.webapi.endereco.core.repositorio.EnderecoRepositorio;
import br.com.ns.webapi.endereco.core.service.EnderecoService;
import br.com.ns.webapi.endereco.exception.RecursoNaoEncontradoException;
import br.com.ns.webapi.endereco.modelo.Endereco;

/**
 * Classe responsável por abstrair a lógica de negócio e tratamento da
 * informação.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 *
 */
@Service
public class EnderecoServiceImpl implements EnderecoService {

	@Autowired
	private EnderecoRepositorio enderecoRepositorio;

	@Autowired
	private EnderecamentoPostalRestService restService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Endereco obterEnderecoPeloIdentificador(Long id) {
		Endereco ret = enderecoRepositorio.findOne(id);

		if (ret == null) {
			throw new RecursoNaoEncontradoException("Endereço não encontrado");
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removerEnderecoPeloIdentificador(Long id) {

		/* Verifica se um endereço será realmente removido ou não */
		if (enderecoRepositorio.exists(id)) {
			enderecoRepositorio.delete(id);

			return true;
		} else {

			return false;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Endereco criarEndereco(Endereco endereco) {

		if (endereco.getId() != null) {
			throw new IllegalArgumentException(
					"Identificador do Endereço não deve ser informado");
		}

		return enderecoRepositorio.save(endereco);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Endereco atualizarEndereco(Endereco endereco) {

		if (endereco.getId() == null) {
			throw new IllegalArgumentException(
					"Identificador do Endereço deve ser informado");
		}

		if (enderecoRepositorio.exists(endereco.getId())) {

			/* Obtem o endereço baseado no CEP enviado para validar as demais informaçoes enviadas
			 * pelo cliente */
			br.com.ns.webapi.endereco.core.integration.modelo.Endereco enderecoBucaCep = restService
					.obterEnderecoPorCep(endereco.getCep());

			
			
			return enderecoRepositorio.save(endereco);

		} else {
			throw new RecursoNaoEncontradoException(
					"Endereço não pode ser atualizado pois não foi encontrado");
		}

	}

}

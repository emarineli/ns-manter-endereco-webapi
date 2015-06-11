package br.com.ns.webapi.endereco.core.service.impl;

import static javax.transaction.Transactional.TxType.*;

import javax.transaction.Transactional;

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

	private static final String CEP_SEM_LOGRADOURO_SUFIX = "000";

	@Autowired
	private EnderecoRepositorio enderecoRepositorio;

	@Autowired
	private EnderecamentoPostalRestService restService;

	/**
	 * {@inheritDoc}
	 */
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
	@Transactional(REQUIRED)
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
	@Transactional(REQUIRED)
	public Endereco criarEndereco(Endereco endereco) {

		if (endereco.getId() != null) {
			throw new IllegalArgumentException(
					"Identificador do Endereço não deve ser informado");
		}

		/*
		 * Obtem o endereço baseado no CEP enviado para validar as demais
		 * informaçoes enviadas pelo cliente
		 */
		br.com.ns.webapi.endereco.core.integration.modelo.Endereco enderecoBucaCep = restService
				.obterEnderecoPorCep(endereco.getCep());

		if (enderecoCorrespondente(endereco, enderecoBucaCep)) {
			return enderecoRepositorio.save(endereco);
		} else {
			throw new IllegalStateException(
					"As informacoes enviadas são divergentes em relacao ao CEP informado");
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional(REQUIRED)
	public Endereco atualizarEndereco(Endereco endereco) {

		if (endereco.getId() == null) {
			throw new IllegalArgumentException(
					"Identificador do Endereço deve ser informado");
		}

		if (enderecoRepositorio.exists(endereco.getId())) {

			/*
			 * Obtem o endereço baseado no CEP enviado para validar as demais
			 * informaçoes enviadas pelo cliente
			 */
			br.com.ns.webapi.endereco.core.integration.modelo.Endereco enderecoBucaCep = restService
					.obterEnderecoPorCep(endereco.getCep());

			if (enderecoCorrespondente(endereco, enderecoBucaCep)) {
				return enderecoRepositorio.save(endereco);
			} else {
				throw new IllegalStateException(
						"As informacoes enviadas são divergentes em relacao ao CEP informado");
			}

		} else {
			throw new RecursoNaoEncontradoException(
					"Endereço não pode ser atualizado pois não foi encontrado");
		}

	}

	/**
	 * Realiza a validação entre um endereço qualquer informado e as informações
	 * relativas à busca de CEP.
	 *
	 * Existe praticamente dois cenários que deverão ser validados:
	 * 
	 * 1. Quando o código de endereçamento obtém as informações de endereço
	 * completas. Neste caso todos os atributos devem ser iguais, menos número
	 * da residência e complemento (que não é obrigatório).
	 * 
	 * 2. Quando o código de endereçamento obtém apenas as informações da
	 * localidade, basicamente por se tratar de uma localidade sem CEP por
	 * logradouro. Nesse caso, apenas os atributos de UF e Localidade deverão
	 * ser validados.
	 *
	 * @param endereco
	 *            representação do endereço a ser testado.
	 * @param enderecoBucaCep
	 *            representação do endereço chave para validação.
	 * @return se o endereço que está testado possui informações válidas.
	 */
	private boolean enderecoCorrespondente(
			Endereco endereco,
			br.com.ns.webapi.endereco.core.integration.modelo.Endereco enderecoBucaCep) {

		/* Primeira sessão válida para lolidades sem CEP por logradouro */
		if (!enderecoBucaCep.getUf().equalsIgnoreCase(endereco.getUf())
				|| !enderecoBucaCep.getLocalidade().equalsIgnoreCase(
						endereco.getLocalidade())) {
			return false;
		}

		/* Verifica se a localidade possui CEP por lograoduro */
		if (cepPorLogradouro(enderecoBucaCep.getCep())) {

			if (!(endereco.getBairro() != null
					&& !"".equals(endereco.getBairro().trim()) && enderecoBucaCep
					.getBairro().equalsIgnoreCase(endereco.getBairro()))
					|| !enderecoBucaCep.getLogradouro().equalsIgnoreCase(
							endereco.getLogradouro())) {
				return false;
			}

		}

		return true;
	}

	/**
	 * Retorna se uma localidade possui CEP por Logradouro.
	 * 
	 * @param cep
	 *            código de endereçamento a ser validado.
	 */
	private boolean cepPorLogradouro(String cep) {
		return !CEP_SEM_LOGRADOURO_SUFIX.equals(cep.substring(5, 8));
	}

}

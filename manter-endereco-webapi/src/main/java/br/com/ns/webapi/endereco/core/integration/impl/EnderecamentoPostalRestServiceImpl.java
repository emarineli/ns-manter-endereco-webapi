package br.com.ns.webapi.endereco.core.integration.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.ns.webapi.endereco.core.integration.EnderecamentoPostalRestService;
import br.com.ns.webapi.endereco.core.integration.modelo.Endereco;

/**
 * Serviço de integração com serviço REST de Endereçamento Postal.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 */
@Service
public class EnderecamentoPostalRestServiceImpl implements
		EnderecamentoPostalRestService {

	@Value("${enderecamento.postal.get.uri}")
	private String enderecamentoPostalUri;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Endereco obterEnderecoPorCep(String cep) {

		RestTemplate restTemplate = new RestTemplate();

		Endereco endereco = null;
		try {
			endereco = restTemplate.getForObject(enderecamentoPostalUri + cep,
					Endereco.class);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			throw e;

		}

		return endereco;
	}

}

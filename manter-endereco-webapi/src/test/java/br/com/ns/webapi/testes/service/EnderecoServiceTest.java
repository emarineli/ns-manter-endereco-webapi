package br.com.ns.webapi.testes.service;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.ns.webapi.endereco.core.integration.EnderecamentoPostalRestService;
import br.com.ns.webapi.endereco.core.repositorio.EnderecoRepositorio;
import br.com.ns.webapi.endereco.core.service.impl.EnderecoServiceImpl;
import br.com.ns.webapi.endereco.exception.RecursoNaoEncontradoException;
import br.com.ns.webapi.endereco.modelo.Endereco;

/**
 * Classe de testes para a classe de servico EnderecoService.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 *
 */
public class EnderecoServiceTest {

	@Mock
	private EnderecamentoPostalRestService enderecamentoPostalRestService;

	@Mock
	private EnderecoRepositorio enderecoRepositorio;

	@InjectMocks
	private EnderecoServiceImpl enderecoService;

	private Endereco enderecoBaseCepLogradouro;
	private Endereco enderecoBaseCepSemLogradouro;

	private Endereco enderecoCriado;

	/**
	 * Inicializa o serviço.
	 */
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		enderecoBaseCepLogradouro = new Endereco("Rua Fransico Regina", 247,
				null, "Osasco", "Jardim Elvira", "Sao Paulo", "06250080");

		/* TODO Clonar */
		enderecoCriado = new Endereco("Rua Fransico Regina", 247,
				"Complemento Teste", "Osasco", "Jardim Elvira", "Sao Paulo",
				"06250080");
		enderecoCriado.setId(2L);

		br.com.ns.webapi.endereco.core.integration.modelo.Endereco enderecoIntegracaoCepLogradouro = new br.com.ns.webapi.endereco.core.integration.modelo.Endereco(
				"Rua Fransico Regina", "Osasco", "Jardim Elvira", "Sao Paulo",
				"06250080");

		br.com.ns.webapi.endereco.core.integration.modelo.Endereco enderecoIntegracaoCepSemLogradouro = new br.com.ns.webapi.endereco.core.integration.modelo.Endereco(
				null, "Nova Europa", "Centro", "Sao Paulo", "14920000");

		/* Representa um a localidade onde o CEP possui logradouro */
		when(enderecamentoPostalRestService.obterEnderecoPorCep("06250080"))
				.thenReturn(enderecoIntegracaoCepLogradouro);

		/* Representa um a localidade onde o CEP não possui logradouro */
		when(enderecamentoPostalRestService.obterEnderecoPorCep("14920000"))
				.thenReturn(enderecoIntegracaoCepSemLogradouro);

		/* Obtençãp pelo Identificador */
		when(enderecoRepositorio.findOne(1L)).thenReturn(
				enderecoBaseCepLogradouro);
		when(enderecoRepositorio.findOne(2L)).thenReturn(null);

		/* Remoção */
		when(enderecoRepositorio.exists(3L)).thenReturn(true);
		when(enderecoRepositorio.exists(4L)).thenReturn(false);

		/* Criação */
		when(enderecoRepositorio.save(enderecoBaseCepLogradouro)).thenReturn(
				enderecoCriado);

		/* Atualização */
		when(enderecoRepositorio.save(enderecoBaseCepSemLogradouro))
				.thenReturn(enderecoCriado);
	}

	/**
	 * Testa a obtenção de um endereço pelo seu identificador.
	 */
	@Test
	public void testObterEnderecoPeloIdentificador() {
		Assert.assertEquals(enderecoBaseCepLogradouro,
				enderecoService.obterEnderecoPeloIdentificador(1L));
	}

	/**
	 * Caso um identificador inválido for informado o recurso não pode ser
	 * encontrado.
	 */
	@Test(expected = RecursoNaoEncontradoException.class)
	public void testObterEnderecoPeloIdentificadorNulo() {
		enderecoService.obterEnderecoPeloIdentificador(null);
	}

	/**
	 * Caso um endereço exista na base uma exceção será lançada indicando que o
	 * recurso não foi encontrado.
	 */
	@Test(expected = RecursoNaoEncontradoException.class)
	public void testObterEnderecoInexistentePeloIdentificador() {
		enderecoService.obterEnderecoPeloIdentificador(2L);
	}

	/**
	 * Testa a remoção de um endereço exsistente, portanto o retorno do método
	 * será verdadeiro indicando que o endereço removido resistia em base.
	 */
	@Test
	public void testRemoverEnderecoExistentePeloIdentificador() {
		Assert.assertTrue(enderecoService.removerEnderecoPeloIdentificador(3L));
	}

	/**
	 * Testa a remoção de um endereço inexistente, portanto o retorno do método
	 * será falso indicando que o endereço não existia em base.
	 */
	@Test
	public void testRemoverEnderecoInexistentePeloIdentificador() {
		Assert.assertFalse(enderecoService.removerEnderecoPeloIdentificador(4L));
	}

	/**
	 * Testa a criação de endereço quando é passado uma entidade que possui
	 * identificador.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCriarEnderecoPassandoIdentificadorEntidade() {

		enderecoBaseCepLogradouro.setId(1L);
		enderecoService.criarEndereco(enderecoBaseCepLogradouro);
	}

	/**
	 * Testa a criação de endereço quando é passado uma entidade que possui
	 * identificador.
	 */
	@Test
	public void testCriarEnderecoValido() {
		Assert.assertEquals(enderecoCriado,
				enderecoService.criarEndereco(enderecoBaseCepLogradouro));
	}

	/**
	 * Testa a criação de endereço válido mas com a UF divergente do endereço
	 * que foi obtido pela busca de CEP.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCriarEnderecoValidoComDivergenciaUF() {

		enderecoBaseCepLogradouro.setUf("Santa Catarina");
		enderecoService.criarEndereco(enderecoBaseCepLogradouro);
	}

	/**
	 * Testa a criação de endereço válido mas com a Localidade divergente do
	 * endereço que foi obtido pela busca de CEP.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCriarEnderecoValidoComDivergenciaLocalidade() {

		enderecoBaseCepLogradouro.setLocalidade("Nova Europa");
		enderecoService.criarEndereco(enderecoBaseCepLogradouro);
	}

	/**
	 * Testa a criação de endereço válido mas com o Bairro divergente do
	 * endereço que foi obtido pela busca de CEP.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCriarEnderecoValidoComDivergenciaBairro() {

		enderecoBaseCepLogradouro.setBairro("Centro");
		enderecoService.criarEndereco(enderecoBaseCepLogradouro);
	}

	/**
	 * Testa a criação de endereço válido mas com o Logradouro divergente do
	 * endereço que foi obtido pela busca de CEP.
	 */
	@Test(expected = IllegalStateException.class)
	public void testCriarEnderecoValidoComDivergenciaLogradouro() {

		enderecoBaseCepLogradouro.setLogradouro("Rua 9 de Julho");
		enderecoService.criarEndereco(enderecoBaseCepLogradouro);
	}

}

package br.com.ns.webapi.testes.recursos;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ns.webapi.endereco.core.service.EnderecoService;
import br.com.ns.webapi.endereco.exception.handler.GlobalResourceExceptionHandler;
import br.com.ns.webapi.endereco.modelo.Endereco;
import br.com.ns.webapi.endereco.recursos.EnderecoRecurso;

/**
 * Classe de testes para o recurso de Endereco.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 *
 */
@EnableWebMvc
public class EnderecoRecursoTest {

	private static final String BASE_RESOURCE_URI = "/endereco";

	private static final String RESOURCE_URI_GET_DELETE = BASE_RESOURCE_URI
			+ "/{id}";

	@Autowired
	private WebApplicationContext ctx;

	@Mock
	private EnderecoService enderecoService;

	@InjectMocks
	private EnderecoRecurso enderecoRecurso;

	private MockMvc mockMvc;

	private Endereco enderecoBase;

	private Endereco enderecoCriado;

	/**
	 * Inicializa o contexto do mock.
	 */
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);

		enderecoBase = new Endereco("Rua Fransico Regina", 247, null, "Osasco",
				"Jardim Elvira", "Sao Paulo", "06250080");

		/* TODO Clonar */
		enderecoCriado = new Endereco("Rua Fransico Regina", 247,
				"Complemento Teste", "Osasco", "Jardim Elvira", "Sao Paulo",
				"06250080");
		enderecoCriado.setId(2L);

		/*
		 * Para testes standalone deve ser criado o exception resolver
		 * apropriadamente.
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(enderecoRecurso)
				.setHandlerExceptionResolvers(createExceptionResolver())
				.build();

		/* GET */
		when(enderecoService.obterEnderecoPeloIdentificador(1L)).thenReturn(
				enderecoBase);

		/* DELETE */
		when(enderecoService.removerEnderecoPeloIdentificador(1L)).thenReturn(
				true);
		when(enderecoService.removerEnderecoPeloIdentificador(2L)).thenReturn(
				false);

		/* ṔOST */
		when(enderecoService.criarEndereco(enderecoBase)).thenReturn(
				enderecoCriado);

		when(enderecoService.criarEndereco(enderecoCriado)).thenThrow(
				new IllegalArgumentException(
						"Identificador do Endereço não deve ser informado"));

		/* PUT */
		when(enderecoService.atualizarEndereco(enderecoCriado)).thenReturn(
				enderecoCriado);

		when(enderecoService.atualizarEndereco(enderecoBase)).thenThrow(
				new IllegalArgumentException(
						"Identificador do Endereço deve ser informado"));

	}

	/**
	 * Testa a obtenção de um endereço.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCepValidoDadosIncompletos() throws Exception {

		mockMvc.perform(
				get(RESOURCE_URI_GET_DELETE, 1L).accept(APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(APPLICATION_JSON))
				.andExpect(jsonPath("$.localidade", is("Osasco")))
				.andExpect(jsonPath("$.uf", is("Sao Paulo")))
				.andExpect(jsonPath("$.cep", is("06250080")))
				.andExpect(jsonPath("$.logradouro", is("Rua Fransico Regina")))
				.andExpect(jsonPath("$.numero", is(247)))
				.andExpect(jsonPath("$.bairro", is("Jardim Elvira")))
				.andExpect(jsonPath("$.complemento").doesNotExist());

	}

	/**
	 * Testa a remocao de um endereço existente em base.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testRemocaoEnderecoExistente() throws Exception {

		mockMvc.perform(
				delete(RESOURCE_URI_GET_DELETE, 1L).accept(APPLICATION_JSON))
				.andExpect(status().isOk());

	}

	/**
	 * Testa a remocao de um endereço não existente em base.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testRemocaoEnderecoInexistente() throws Exception {

		mockMvc.perform(
				delete(RESOURCE_URI_GET_DELETE, 2L).accept(APPLICATION_JSON))
				.andExpect(status().isNoContent());

	}

	/**
	 * Testa a criação de um endereço válido e inexistente na base.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoInexistente() throws Exception {

		templatePerformPost(CREATED.value()).andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.localidade", is("Osasco")))
				.andExpect(jsonPath("$.uf", is("Sao Paulo")))
				.andExpect(jsonPath("$.cep", is("06250080")))
				.andExpect(jsonPath("$.logradouro", is("Rua Fransico Regina")))
				.andExpect(jsonPath("$.numero", is(247)))
				.andExpect(jsonPath("$.bairro", is("Jardim Elvira")))
				.andExpect(jsonPath("$.complemento", is("Complemento Teste")));
	}

	/**
	 * Testa a criação de um endereço com UF inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoInexistenteUfInvalidaOuNula() throws Exception {

		enderecoBase.setUf(null);

		templatePerformPost(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("UF não pode ser nula ou vazia")));

	}

	/**
	 * Testa a criação de um endereço com Localidade inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoInexistenteLocalidadeInvalidaOuNula()
			throws Exception {

		enderecoBase.setLocalidade(null);

		templatePerformPost(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("Localidade não pode ser nula ou vazia")));

	}

	/**
	 * Testa a criação de um endereço com CEP inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoInexistenteCepNulo() throws Exception {

		enderecoBase.setCep(null);

		templatePerformPost(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("CEP não pode ser nulo ou vazio")));

	}

	/**
	 * Testa a criação de um endereço com CEP inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoInexistenteCepPadraoInvalido()
			throws Exception {

		enderecoBase.setCep("12234");

		templatePerformPost(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("CEP não válido")));

	}

	/**
	 * Testa a criação de um endereço com Logradouro inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoInexistenteLogradouroInvalidoOuNulo()
			throws Exception {

		enderecoBase.setLogradouro(null);

		templatePerformPost(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("Logradouro não pode ser nula ou vazio")));

	}

	/**
	 * Testa a criação de um endereço já criado ou com o identificador já
	 * preenchido,
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testCriarEnderecoJaExistenteOuComIdentificadorPreenchido()
			throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(
				post(BASE_RESOURCE_URI)
						.content(mapper.writeValueAsString(enderecoCriado))
						.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(
						jsonPath(
								"$.mensagem",
								is("Identificador do Endereço não deve ser informado")));

	}

	/**
	 * Testa a criação de um endereço válido e inexistente na base.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoExistente() throws Exception {

		templatePerformPut(OK.value()).andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.localidade", is("Osasco")))
				.andExpect(jsonPath("$.uf", is("Sao Paulo")))
				.andExpect(jsonPath("$.cep", is("06250080")))
				.andExpect(jsonPath("$.logradouro", is("Rua Fransico Regina")))
				.andExpect(jsonPath("$.numero", is(247)))
				.andExpect(jsonPath("$.bairro", is("Jardim Elvira")))
				.andExpect(jsonPath("$.complemento", is("Complemento Teste")));
	}

	/**
	 * Testa a criação de um endereço válido e inexistente na base.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoInexistente() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(
				put(BASE_RESOURCE_URI)
						.content(mapper.writeValueAsString(enderecoBase))
						.contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(
						jsonPath(
								"$.mensagem",
								is("Identificador do Endereço deve ser informado")));
	}

	
	/**
	 * Testa a atualização de um endereço com UF inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoInexistenteUfInvalidaOuNula() throws Exception {

		enderecoCriado.setUf(null);

		templatePerformPut(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("UF não pode ser nula ou vazia")));

	}

	/**
	 * Testa a atualização de um endereço com Localidade inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoInexistenteLocalidadeInvalidaOuNula()
			throws Exception {

		enderecoCriado.setLocalidade(null);

		templatePerformPut(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("Localidade não pode ser nula ou vazia")));

	}

	/**
	 * Testa a atualização de um endereço com CEP inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoInexistenteCepNulo() throws Exception {

		enderecoCriado.setCep(null);

		templatePerformPut(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("CEP não pode ser nulo ou vazio")));

	}

	/**
	 * Testa a atualização de um endereço com CEP inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoInexistenteCepPadraoInvalido()
			throws Exception {

		enderecoCriado.setCep("12234");

		templatePerformPut(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("CEP não válido")));

	}

	/**
	 * Testa a atualização de um endereço com Logradouro inválido.
	 * 
	 * @throws Exception
	 *             caso algum erro ocorra.
	 */
	@Test
	public void testAtualizarEnderecoInexistenteLogradouroInvalidoOuNulo()
			throws Exception {

		enderecoCriado.setLogradouro(null);

		templatePerformPut(BAD_REQUEST.value()).andExpect(
				jsonPath("$").isArray()).andExpect(
				jsonPath("$.[0]", is("Logradouro não pode ser nula ou vazio")));

	}
	
	/**
	 * Template de post para as validações.
	 * 
	 * @return post realizado e pronto para assert.
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	private ResultActions templatePerformPost(int status)
			throws JsonProcessingException, Exception {

		ObjectMapper mapper = new ObjectMapper();

		return mockMvc
				.perform(
						post(BASE_RESOURCE_URI)
								.content(
										mapper.writeValueAsString(enderecoBase))
								.contentType(APPLICATION_JSON)
								.accept(APPLICATION_JSON))
				.andExpect(status().is(status))
				.andExpect(jsonPath("$").exists());
	}

	/**
	 * Template de put para validaçoes.
	 * 
	 * @return put realizado e pronto para assert.
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	private ResultActions templatePerformPut(int status)
			throws JsonProcessingException, Exception {

		ObjectMapper mapper = new ObjectMapper();

		return mockMvc
				.perform(
						put(BASE_RESOURCE_URI)
								.content(
										mapper.writeValueAsString(enderecoCriado))
								.contentType(APPLICATION_JSON)
								.accept(APPLICATION_JSON))
				.andExpect(status().is(status))
				.andExpect(jsonPath("$").exists());
	}

	/**
	 * Cria o Exception Resolver baseado na classe
	 * GlobalResourceExceptionHandler deste projeto.
	 * 
	 * @see GlobalResourceExceptionHandler#GlobalResourceExceptionHandler()
	 * 
	 * @return Exception Resolver.
	 */
	private ExceptionHandlerExceptionResolver createExceptionResolver() {
		ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
			@Override
			protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
					HandlerMethod handlerMethod, Exception exception) {
				Method method = new ExceptionHandlerMethodResolver(
						GlobalResourceExceptionHandler.class)
						.resolveMethod(exception);
				return new ServletInvocableHandlerMethod(
						new GlobalResourceExceptionHandler(), method);
			}
		};
		exceptionResolver.getMessageConverters().add(
				new MappingJackson2HttpMessageConverter());
		exceptionResolver.afterPropertiesSet();
		return exceptionResolver;
	}

}

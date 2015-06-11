package br.com.ns.webapi.endereco.recursos;

import static br.com.ns.webapi.endereco.util.ValidatorUtil.transformarEmMensagemErro;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ns.webapi.endereco.core.service.EnderecoService;
import br.com.ns.webapi.endereco.modelo.Endereco;

/**
 * Representa o recurso do endereço e suas operações expostas.
 * 
 * O recurso irá agir apenas como fachada que delega sua invocação para a camada
 * de serviço que efetivamente irá tratar a solicitação.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0, 10/06/2015
 * @since 1.0
 *
 */
@Controller
@RequestMapping("/endereco")
public class EnderecoRecurso {

	@Autowired
	private EnderecoService enderecoService;

	/**
	 * Obtém um endereço pelo seu identificador primário.
	 * 
	 * @param id
	 *            identificador primário do endereço.
	 * @return a representação do Endereço caso encontrado ou um código 404.
	 */
	@RequestMapping(produces = { APPLICATION_JSON_VALUE }, value = "/{id}", method = GET)
	public final @ResponseBody Endereco obterEnderecoPeloIdentificador(
			@PathVariable Long id) {
		return enderecoService.obterEnderecoPeloIdentificador(id);
	}

	/**
	 * Remove um endereço pelo seu identificador primário.
	 * 
	 * Delete é um método idempotente, mas aqui serão tratados códigos HTTP
	 * distintos para indicar se realmente existia algum registo a ser excluído.
	 * 
	 * @param id
	 *            identificador primário do endereço.
	 * @return se realmente ocorreu a remoção de um registro.
	 */
	@RequestMapping(produces = { APPLICATION_JSON_VALUE }, value = "/{id}", method = DELETE)
	public final ResponseEntity<String> removerEnderecoPeloIdentificador(
			@PathVariable Long id) {
		boolean statusRemocao = enderecoService
				.removerEnderecoPeloIdentificador(id);

		return statusRemocao ? new ResponseEntity<String>(OK)
				: new ResponseEntity<String>(NO_CONTENT);
	}

	/**
	 * Cria um endereço.
	 * 
	 * @param endereco
	 *            representação da entidade a ser criada.
	 * 
	 * @return se realmente ocorreu a remoção de um registro.
	 */
	@RequestMapping(consumes = { APPLICATION_JSON_VALUE }, method = POST)
	public final ResponseEntity<?> criarEndereco(
			@RequestBody @Valid Endereco endereco, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			return new ResponseEntity<List<String>>(
					transformarEmMensagemErro(bindingResult.getFieldErrors()),
					HttpStatus.BAD_REQUEST);

		} else {

			return new ResponseEntity<Endereco>(
					enderecoService.criarEndereco(endereco), CREATED);
		}
	}

	/**
	 * Atualiza um endereço.
	 * 
	 * @param endereco
	 *            representação da entidade a ser criada.
	 * 
	 * @return se realmente ocorreu a remoção de um registro.
	 */
	@RequestMapping(consumes = { APPLICATION_JSON_VALUE }, method = PUT)
	public final ResponseEntity<?> atualizarEndereco(
			@RequestBody @Valid Endereco endereco, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<List<String>>(
					transformarEmMensagemErro(bindingResult.getFieldErrors()),
					HttpStatus.BAD_REQUEST);
		} else {

			return new ResponseEntity<Endereco>(
					enderecoService.atualizarEndereco(endereco), OK);
		}
	}
}

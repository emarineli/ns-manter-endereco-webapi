package br.com.ns.webapi.endereco.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

/**
 * Utilidades para validação de erros.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0, 09/06/2015
 * @since 1.0
 *
 */
public class ValidatorUtil {

	/**
	 * Transforma uma lista de erros criada durante a validação do bean em
	 * mensagens mais amigáveis para serem retornadas.
	 * 
	 * @param fieldErros
	 *            lista com os erros de validação.
	 * @return lista com as mensagens de erros transformadas.
	 */
	public static final List<String> transformarEmMensagemErro(
			List<FieldError> fieldErros) {

		List<String> listaErro = new ArrayList<String>();

		for (FieldError fe : fieldErros) {
			listaErro.add(fe.getDefaultMessage());
		}

		return listaErro;
	}
}

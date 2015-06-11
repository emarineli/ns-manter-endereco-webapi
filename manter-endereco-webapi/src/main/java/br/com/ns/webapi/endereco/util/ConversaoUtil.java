package br.com.ns.webapi.endereco.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utilidades para validação de erros.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0, 09/06/2015
 * @since 1.0
 *
 */
public class ConversaoUtil {

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

	/**
	 * Transforma uma representação em JSON em um objeto.
	 * 
	 * @param json
	 *            representação válida JSON.
	 * @param t
	 *            tipo do objeto a ser transformado.
	 * @return objeto convertido da representação JSON.
	 */
	public static final <T> T fromJSON(String json, Class<T> t) {
		ObjectMapper mp = new ObjectMapper();

		try {
			return mp.readValue(json, t);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}

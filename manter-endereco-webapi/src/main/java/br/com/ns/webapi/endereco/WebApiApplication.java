package br.com.ns.webapi.endereco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application class do Spring Boot responsável pela inicialização da Web Api.
 * 
 * Esta WebApi será responsável por prover operações de manutenção do domínio de endereco.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 *
 */
@SpringBootApplication
@EnableTransactionManagement
public class WebApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebApiApplication.class, args);
	}
}

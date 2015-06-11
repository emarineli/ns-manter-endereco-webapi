package br.com.ns.webapi.endereco.core.repositorio;

import org.springframework.data.repository.CrudRepository;

import br.com.ns.webapi.endereco.modelo.Endereco;

/**
 * Interface de repositório responsável por expor as operações relacionadas ao
 * acesso aos dados da entidade Endereco.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0,10/06/2015
 * @since 1.0
 *
 */
public interface EnderecoRepositorio extends CrudRepository<Endereco, Long> {
}

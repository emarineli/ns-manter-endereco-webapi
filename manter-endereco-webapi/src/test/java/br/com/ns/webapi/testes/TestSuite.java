package br.com.ns.webapi.testes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import br.com.ns.webapi.testes.recursos.EnderecoRecursoTest;
import br.com.ns.webapi.testes.service.EnderecoServiceTest;

/**
 * Suite de teste.
 * 
 * @author Erico Marineli (emarineli@gmail.com)
 * @version 1.0, 09/06/2015
 * @since 1.0
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ EnderecoRecursoTest.class, EnderecoServiceTest.class })
public class TestSuite {
	// nada por aqui :D
}
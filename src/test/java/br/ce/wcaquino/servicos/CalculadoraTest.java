package br.ce.wcaquino.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

  private Calculadora calculadora;

  @Before
  public void init() {
    calculadora = new Calculadora();
  }

  @Test
  public void testSomaValores() {
    // cenario
    var a = 5;
    var b = 3;

    // acao
    var resultado = calculadora.somar(a, b);

    // verificacao
    assertEquals(8, resultado);
  }

  @Test
  public void testSubtraiValores() {
    // cenario
    var a = 5;
    var b = 3;

    // acao
    var resultado = calculadora.subtracao(a, b);

    // verificacao
    assertEquals(2, resultado);
  }

  @Test
  public void testDivideValores() throws NaoPodeDividirPorZeroException {
    // cenario
    var a = 10;
    var b = 5;

    // acao
    var resultado = calculadora.divisao(a, b);

    // verificacao
    assertEquals(2, resultado);
  }

  @Test
  public void testExcecaoAoDividirPorZero() {
    // cenario
    var a = 10;
    var b = 0;

    // acao
    var exception =
        assertThrows(NaoPodeDividirPorZeroException.class, () -> calculadora.divisao(a, b));

    // verificacao
    assertEquals("Nao pode dividir por zero", exception.getMessage());
  }
}

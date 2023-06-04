package br.ce.wcaquino.servicos;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class CalculadoraMockTest {

  @Test
  public void teste() {
    var calc = mock(Calculadora.class);

    when(calc.soma(eq(1), anyInt())).thenReturn(5);

    System.out.println(calc.soma(1,8));
  }
}

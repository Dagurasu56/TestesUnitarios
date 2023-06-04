package br.ce.wcaquino.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;

public class CalculadoraMockTest {

  @Mock
  private Calculadora calcMock;

  @Spy
  private Calculadora calcSpy;

  @Spy
  private EmailService emailService;

  @Before
  public void setUp() {
    initMocks(this);
  }

  @Test
  public void devoMostrarDiferencaEntreMockSpy() {
    when(calcMock.somar(1, 2)).thenCallRealMethod();
    when(calcSpy.somar(1, 3)).thenReturn(8);

    doReturn(5).when(calcSpy).somar(1, 2);
    doNothing().when(calcSpy).imprime();

    System.out.println("Mock: " + calcMock.somar(1, 2));
    System.out.println("Spy: " + calcSpy.somar(1, 2));

    System.out.println("Mock");
    calcMock.imprime();

    System.out.println("Spy");
    calcSpy.imprime();
  }

  @Test
  public void teste() {
    var calc = mock(Calculadora.class);

    var argCaptor = ArgumentCaptor.forClass(Integer.class);
    when(calc.somar(argCaptor.capture(), argCaptor.capture())).thenReturn(5);

    assertEquals(5, calc.somar(1,8));
  }
}

package br.ce.wcaquino.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrdemTest {

  public static int contador = 0;

  @Test
  @Order(1)
  public void inicia() {
    contador = 1;
  }

  @Test
  @Order(2)
  public void verifica() {
    assertEquals(1, contador);
  }
}

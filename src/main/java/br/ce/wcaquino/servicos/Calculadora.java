package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class Calculadora {
  public int somar(int a, int b) {
    return a + b;
  }

  public int subtracao(int a, int b) {
    return a - b;
  }

  public int divisao(int a, int b) throws NaoPodeDividirPorZeroException {
    if (b == 0) {
      throw new NaoPodeDividirPorZeroException("Nao pode dividir por zero");
    }
    return a / b;
  }

  public void imprime() {
    System.out.println("Passei aqui");
  }
}

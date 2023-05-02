package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.junit.jupiter.api.Test;

class LocacaoServiceTest {

  @Test
  void testLocacaoFilme() throws Exception {
    // cenario
    var service = new LocacaoService();
    var usuario = new Usuario("Usuario 1");
    var filme = new Filme("Filme 1", 2, 5.0);

    // acao
    var locacao = service.alugarFilme(usuario, filme);

    // verificacao
    assertThat(locacao.getValor(), is(5.0));
    assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
  }

  @Test
  void testLocacaoFilmeSemEstoque() {
    // cenario
    var service = new LocacaoService();
    var usuario = new Usuario("Usuario 1");
    var filme = new Filme("Filme 1", 0, 5.0);

    // acao
    var exception = assertThrows(Exception.class, () -> service.alugarFilme(usuario, filme));

    // verificacao
    assertEquals("Filme sem estoque", exception.getMessage());
  }
}

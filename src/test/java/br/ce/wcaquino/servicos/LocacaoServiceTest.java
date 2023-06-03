package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocacaoServiceTest {

  private LocacaoService locacaoService;

  @BeforeEach
  void setUp() {
    locacaoService = new LocacaoService();
  }

  @Test
  void testLocacaoFilme() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filme = new Filme("Filme 1", 2, 5.0);

    // acao
    var locacao = locacaoService.alugarFilme(usuario, filme);

    // verificacao
    assertThat(locacao.getValor(), is(5.0));
    assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    assertThat(
        DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
        is(true));
  }

  @Test
  void testLocacaoFilmeSemEstoque() {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filme = new Filme("Filme 1", 0, 5.0);

    // acao
    var exception =
        assertThrows(
            FilmeSemEstoqueException.class, () -> locacaoService.alugarFilme(usuario, filme));

    // verificacao
    assertEquals("Filme sem estoque", exception.getMessage());
  }

  @Test
  void testLocacaoUsuarioVazio() {
    // cenario
    var filme = new Filme("Filme 2", 1, 4.0);

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(null, filme));

    // verificacao
    assertEquals("Usuario vazio", locadoraException.getMessage());
  }

  @Test
  void testFilmeVazio() {
    // cenario
    var usuario = new Usuario("Usuario 1");

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));

    // verificacao
    assertEquals("Filme vazio", locadoraException.getMessage());
  }
}

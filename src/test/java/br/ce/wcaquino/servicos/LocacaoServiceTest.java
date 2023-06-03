package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocacaoServiceTest {

  private LocacaoService locacaoService;

  @BeforeEach
  void setUp() {
    locacaoService = new LocacaoService();
  }

  @Test
  void testDeveAlugarFilme() throws FilmeSemEstoqueException, LocadoraException {
    assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(new Filme("Filme 1", 2, 5.0));

    // acao
    var locacao = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertThat(locacao.getValor(), is(5.0));
    assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
    assertThat(
        DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
        is(true));
  }

  @Test
  void testNaoDeveAlugarFilmeSemEstoque() {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(new Filme("Filme 1", 0, 5.0));

    // acao
    var exception =
        assertThrows(
            FilmeSemEstoqueException.class, () -> locacaoService.alugarFilme(usuario, filmes));

    // verificacao
    assertEquals("Filme sem estoque", exception.getMessage());
  }

  @Test
  void testNaoDeveAlugarFilmeSemUsuario() {
    // cenario
    var filmes = List.of(new Filme("Filme 2", 1, 4.0));

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(null, filmes));

    // verificacao
    assertEquals("Usuario vazio", locadoraException.getMessage());
  }

  @Test
  void testNaoDeveAlugarFilmeSemFilme() {
    // cenario
    var usuario = new Usuario("Usuario 1");

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));

    // verificacao
    assertEquals("Filme vazio", locadoraException.getMessage());
  }

  @Test
  void testDevePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes =
        List.of(
            new Filme("Filme 1", 2, 4.0),
            new Filme("Filme 2", 2, 4.0),
            new Filme("Filme 3", 2, 4.0));

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(11, resultado.getValor());
  }

  @Test
  void testDevePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes =
        List.of(
            new Filme("Filme 1", 2, 4.0),
            new Filme("Filme 2", 2, 4.0),
            new Filme("Filme 3", 2, 4.0),
            new Filme("Filme 4", 2, 4.0));

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(13, resultado.getValor());
  }

  @Test
  void testDevePagar75PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes =
        List.of(
            new Filme("Filme 1", 2, 4.0),
            new Filme("Filme 2", 2, 4.0),
            new Filme("Filme 3", 2, 4.0),
            new Filme("Filme 4", 2, 4.0),
            new Filme("Filme 5", 2, 4.0));

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(14, resultado.getValor());
  }

  @Test
  void testDevePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes =
        List.of(
            new Filme("Filme 1", 2, 4.0),
            new Filme("Filme 2", 2, 4.0),
            new Filme("Filme 3", 2, 4.0),
            new Filme("Filme 4", 2, 4.0),
            new Filme("Filme 5", 2, 4.0),
            new Filme("Filme 6", 2, 4.0));

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(14, resultado.getValor());
  }

  @Test
  void testDeveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
    
    assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes = List.of(new Filme("Filme 1", 1, 5.0));

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    var ehSegunda = DataUtils.verificarDiaSemana(resultado.getDataRetorno(), Calendar.MONDAY);
    assertTrue(ehSegunda);
  }
}

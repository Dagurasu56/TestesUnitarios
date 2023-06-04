package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprio.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprio.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprio.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprio.ehHojeComDiferencaDeDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class LocacaoServiceTest {

  private LocacaoService locacaoService;

  @Before
  public void setUp() {
    locacaoService = new LocacaoService();
  }

  @Test
  public void testDeveAlugarFilme() throws FilmeSemEstoqueException, LocadoraException {
    Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    // cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilme().comValor(5.0).agora());

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertThat(resultado.getValor(), is(5.0));
    assertThat(resultado.getDataLocacao(), ehHoje());
    assertThat(resultado.getDataRetorno(), ehHojeComDiferencaDeDias(1));
  }

  @Test
  public void testNaoDeveAlugarFilmeSemEstoque() {
    // cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilmeSemEstoque().agora());

    // acao
    var exception =
        assertThrows(
            FilmeSemEstoqueException.class, () -> locacaoService.alugarFilme(usuario, filmes));

    // verificacao
    assertEquals("Filme sem estoque", exception.getMessage());
  }

  @Test
  public void testNaoDeveAlugarFilmeSemUsuario() {
    // cenario
    var filmes = List.of(umFilme().agora());

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(null, filmes));

    // verificacao
    assertEquals("Usuario vazio", locadoraException.getMessage());
  }

  @Test
  public void testNaoDeveAlugarFilmeSemFilme() {
    // cenario
    var usuario = umUsuario().agora();

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> locacaoService.alugarFilme(usuario, null));

    // verificacao
    assertEquals("Filme vazio", locadoraException.getMessage());
  }

  @Test
  public void testDevePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");
    var filmes =
        List.of(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora());

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(11, resultado.getValor());
  }

  @Test
  public void testDevePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = umUsuario().agora();
    var filmes =
        List.of(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora());

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(13, resultado.getValor());
  }

  @Test
  public void testDevePagar75PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = umUsuario().agora();
    var filmes =
        List.of(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora());

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(14, resultado.getValor());
  }

  @Test
  public void testDevePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = umUsuario().agora();
    var filmes =
        List.of(
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora(),
                umFilme().agora());

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(14, resultado.getValor());
  }

  @Test
  public void testDeveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

    Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    // cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilme().agora());

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertThat(resultado.getDataRetorno(), caiEm(Calendar.MONDAY));
    assertThat(resultado.getDataRetorno(), caiNumaSegunda());
  }
}

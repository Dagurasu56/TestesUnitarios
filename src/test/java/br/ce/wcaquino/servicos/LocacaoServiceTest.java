package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprio.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprio.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprio.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprio.ehHojeComDiferencaDeDias;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.ce.wcaquino.daos.LocacaoDAO;
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

  private LocacaoService service;
  private LocacaoDAO dao;
  private SPCService spcService;
  private EmailService emailService;
  @Before
  public void setup(){
    dao = mock(LocacaoDAO.class);
    spcService = mock(SPCService.class);
    emailService = mock(EmailService.class);
    service = new LocacaoService(dao, spcService, emailService);
  }

  @Test
  public void testDeveAlugarFilme() throws FilmeSemEstoqueException, LocadoraException {
    Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

    // cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilme().comValor(5.0).agora());

    // acao
    var resultado = service.alugarFilme(usuario, filmes);

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
            FilmeSemEstoqueException.class, () -> service.alugarFilme(usuario, filmes));

    // verificacao
    assertEquals("Filme sem estoque", exception.getMessage());
  }

  @Test
  public void testNaoDeveAlugarFilmeSemUsuario() {
    // cenario
    var filmes = List.of(umFilme().agora());

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> service.alugarFilme(null, filmes));

    // verificacao
    assertEquals("Usuario vazio", locadoraException.getMessage());
  }

  @Test
  public void testNaoDeveAlugarFilmeSemFilme() {
    // cenario
    var usuario = umUsuario().agora();

    // acao
    var locadoraException =
        assertThrows(LocadoraException.class, () -> service.alugarFilme(usuario, null));

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
    var resultado = service.alugarFilme(usuario, filmes);

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
    var resultado = service.alugarFilme(usuario, filmes);

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
    var resultado = service.alugarFilme(usuario, filmes);

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
    var resultado = service.alugarFilme(usuario, filmes);

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
    var resultado = service.alugarFilme(usuario, filmes);

    // verificacao
    assertThat(resultado.getDataRetorno(), caiEm(Calendar.MONDAY));
    assertThat(resultado.getDataRetorno(), caiNumaSegunda());
  }

  @Test
  public void naoDevealugarFilmeParaNegativadoSPC() {
    // cenario
    var usuario = umUsuario().agora();
    var filmes = List.of(umFilme().agora());

    when(spcService.possuiNegativacao(usuario)).thenReturn(true);

    // acao
    var exception = assertThrows(LocadoraException.class, () -> service.alugarFilme(usuario, filmes));

    // verificacao
    assertEquals("Usuário negativado", exception.getMessage());
  }

  @Test
  public void deveEnviarEmailParaLocacoesAtrasadas() {
    // cenario
    var usuario = umUsuario().agora();
    var locacoes = List.of(umLocacao().comUsuario(usuario).comDataRetorno(obterDataComDiferencaDias(-2)).agora());

    when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

    // acao
    service.notificarAtraso();

    // verificacao
    verify(emailService).notificarAtraso(usuario);
  }
}

package br.ce.wcaquino.servicos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CalculoValorLocacaoTest {

  private LocacaoService locacaoService;

  private static final Filme filme1 = new Filme("Filme 1", 2, 4.0);
  private static final Filme filme2 = new Filme("Filme 2", 2, 4.0);
  private static final Filme filme3 = new Filme("Filme 3", 2, 4.0);
  private static final Filme filme4 = new Filme("Filme 4", 2, 4.0);
  private static final Filme filme5 = new Filme("Filme 5", 2, 4.0);
  private static final Filme filme6 = new Filme("Filme 6", 2, 4.0);
  private static final Filme filme7 = new Filme("Filme 7", 2, 4.0);

  @BeforeEach
  void init() {
    locacaoService = new LocacaoService();
  }

  public static Stream<Arguments> getParametros() {
    return Stream.of(
        Arguments.of(List.of(filme1, filme2), 8, "2 Filmes: Sem Desconto "),
        Arguments.of(List.of(filme1, filme2, filme3), 11, "3 Filmes: 25%"),
        Arguments.of(List.of(filme1, filme2, filme3, filme4), 13, "4 Filmes: 50%"),
        Arguments.of(List.of(filme1, filme2, filme3, filme4, filme5), 14, "5 Filmes: 75%"),
        Arguments.of(List.of(filme1, filme2, filme3, filme4, filme5, filme6), 14, "6 Filmes: 100%"),
        Arguments.of(List.of(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18, "7 Filmes: Sem Desconto"));
  }

  @MethodSource("getParametros")
  @ParameterizedTest(name = "{2}")
  void testCalcularValorLocacaoConsiderandoDescontos(List<Filme> filmes, int valorEsperado, String cenario)
      throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = new Usuario("Usuario 1");

    // acao
    var resultado = locacaoService.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(valorEsperado, resultado.getValor());
  }
}

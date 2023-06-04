package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {

  @InjectMocks private LocacaoService service;
  @Mock
  private LocacaoDAO dao;
  @Mock private SPCService spcService;
  @Mock private EmailService emailService;

  @Parameter
  public List<Filme> filmes;

  @Parameter(value=1)
  public Double valorLocacao;

  @Parameter(value=2)
  public String cenario;
  
  private static final Filme filme1 = FilmeBuilder.umFilme().agora();
  private static final Filme filme2 = FilmeBuilder.umFilme().agora();
  private static final Filme filme3 = FilmeBuilder.umFilme().agora();
  private static final Filme filme4 = FilmeBuilder.umFilme().agora();
  private static final Filme filme5 = FilmeBuilder.umFilme().agora();
  private static final Filme filme6 = FilmeBuilder.umFilme().agora();
  private static final Filme filme7 = FilmeBuilder.umFilme().agora();

  @Before
  public void setup(){
    initMocks(this);
  }

  @Parameterized.Parameters(name="{2}")
  public static Collection<Object[]> getParametros(){
    return Arrays.asList(new Object[][] {
            {Arrays.asList(filme1, filme2), 8.0, "2 Filmes: Sem Desconto"},
            {Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes: 25%"},
            {Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes: 50%"},
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes: 75%"},
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes: 100%"},
            {Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.0, "7 Filmes: Sem Desconto"}
    });
  }


  @Test
  public void testCalcularValorLocacaoConsiderandoDescontos()
      throws FilmeSemEstoqueException, LocadoraException {
    // cenario
    var usuario = umUsuario().agora();

    // acao
    var resultado = service.alugarFilme(usuario, filmes);

    // verificacao
    assertEquals(resultado.getValor(), resultado.getValor());
  }
}

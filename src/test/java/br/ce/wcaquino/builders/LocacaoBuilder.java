package br.ce.wcaquino.builders;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import java.util.List;

public class LocacaoBuilder {

  private Locacao elemento;

  private LocacaoBuilder() {}

  public static void inicializarDadosPadroes(LocacaoBuilder builder) {
    builder.elemento = new Locacao();
    Locacao elemento = builder.elemento;

    elemento.setUsuario(umUsuario().agora());
    elemento.setFilmes(List.of(umFilme().agora()));
    elemento.setDataLocacao(new Date());
    elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
    elemento.setValor(4.0);
  }

  public LocacaoBuilder comUsuario(Usuario usuario) {
    elemento.setUsuario(usuario);
    return this;
  }

  public LocacaoBuilder comListaFilmes(Filme... filmes) {
    elemento.setFilmes(List.of(filmes));
    return this;
  }

  public LocacaoBuilder comDataLocacao(Date data) {
    elemento.setDataLocacao(data);
    return this;
  }

  public LocacaoBuilder comDataRetorno(Date data) {
    elemento.setDataRetorno(data);
    return this;
  }

  public LocacaoBuilder comValor(Double valor) {
    elemento.setValor(valor);
    return this;
  }

  public Locacao agora() {
    return elemento;
  }
}

package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;

import java.util.Date;
import java.util.List;

public class LocacaoService {

  public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
    if (usuario == null) {
      throw new LocadoraException("Usuario vazio");
    }

    if (filmes == null) {
      throw new LocadoraException("Filme vazio");
    }

    if (Boolean.TRUE.equals(isSemEstoque(filmes))) {
      throw new FilmeSemEstoqueException("Filme sem estoque");
    }

    var locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    locacao.setValor(getPreco(filmes));


    var dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);
    locacao.setDataRetorno(dataEntrega);

    // Salvando a locacao...
    // TODO adicionar método para salvar

    return locacao;
  }

  private Boolean isSemEstoque(List<Filme> filmes) {
    for (Filme filme : filmes) {
      if (filme.getEstoque() == 0) {
        return true;
      }
    }
    return false;
  }

  private Double getPreco(List<Filme> filmes) {
    for (Filme filme: filmes) {
      if (filme != null) {
        return filme.getPrecoLocacao();
      }
    }
    return null;
  }
}

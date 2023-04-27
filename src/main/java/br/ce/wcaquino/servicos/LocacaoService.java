package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import java.util.Date;

public class LocacaoService {

  public Locacao alugarFilme(Usuario usuario, Filme filme) {
    var locacao = new Locacao();
    locacao.setFilme(filme);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());
    locacao.setValor(filme.getPrecoLocacao());

    var dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);
    locacao.setDataRetorno(dataEntrega);

    // Salvando a locacao...
    // TODO adicionar método para salvar

    return locacao;
  }
}

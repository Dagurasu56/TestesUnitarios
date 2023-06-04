package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocacaoService {

  private final LocacaoDAO dao;
  private final SPCService spcService;
  private final EmailService emailService;

  public LocacaoService(LocacaoDAO dao, SPCService spcService, EmailService emailService) {
    this.dao = dao;
    this.spcService = spcService;
    this.emailService = emailService;
  }

  public Locacao alugarFilme(Usuario usuario, List<Filme> filmes)
      throws FilmeSemEstoqueException, LocadoraException {
    if (usuario == null) {
      throw new LocadoraException("Usuario vazio");
    }

    if (filmes == null) {
      throw new LocadoraException("Filme vazio");
    }

    if (Boolean.TRUE.equals(isSemEstoque(filmes))) {
      throw new FilmeSemEstoqueException("Filme sem estoque");
    }

    if (spcService.possuiNegativacao(usuario)) {
      throw new LocadoraException("Usuário negativado");
    }

    var locacao = new Locacao();
    locacao.setFilmes(filmes);
    locacao.setUsuario(usuario);
    locacao.setDataLocacao(new Date());

    var valorTotal = 0d;
    for (int i = 0; i < filmes.size(); i++) {
      var filme = filmes.get(i);
      var valorFilme = filme.getPrecoLocacao();
      switch (i) {
        case 2:
          valorFilme = valorFilme * 0.75;
          break;
        case 3:
          valorFilme = valorFilme * 0.50;
          break;
        case 4:
          valorFilme = valorFilme * 0.25;
          break;
        case 5:
          valorFilme = 0d;
          break;
      }
      valorTotal += valorFilme;
    }

    locacao.setValor(valorTotal);

    var dataEntrega = new Date();
    dataEntrega = adicionarDias(dataEntrega, 1);

    if (DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
      dataEntrega = adicionarDias(dataEntrega, 1);
    }

    locacao.setDataRetorno(dataEntrega);

    // Salvando a locacao...
    dao.salvar(locacao);

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

  public void notificarAtraso() {
    List<Locacao> locacoes = dao.obterLocacoesPendentes();
    for (Locacao locacao : locacoes) {
      if (locacao.getDataRetorno().before(new Date())) {
        emailService.notificarAtraso(locacao.getUsuario());
      }
    }
  }
}

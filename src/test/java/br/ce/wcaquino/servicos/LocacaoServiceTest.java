package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.junit.Assert.assertTrue;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.junit.Test;

public class LocacaoServiceTest {

    @Test
    public void teste() {
        // cenario
        var service = new LocacaoService();
        var usuario = new Usuario("Usuario 1");
        var filme = new Filme("Filme 1", 2, 5.0);

        // acao
        Locacao locacao = service.alugarFilme(usuario, filme);

        // verificacao
        assertTrue(locacao.getValor() == 5.0);
        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}

package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.junit.jupiter.api.Test;

class LocacaoServiceTest {

    @Test
    void teste() {
        // cenario
        var service = new LocacaoService();
        var usuario = new Usuario("Usuario 1");
        var filme = new Filme("Filme 1", 2, 5.0);

        // acao
        var locacao = service.alugarFilme(usuario, filme);

        // verificacao
        assertEquals(5.0, locacao.getValor(), 0.01);
        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}

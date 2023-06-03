package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import java.util.Date;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

  private final Integer qtdDias;

  public DataDiferencaDiasMatcher(Integer qtdDias) {
    this.qtdDias = qtdDias;
  }

  @Override
  protected boolean matchesSafely(Date data) {
    return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(qtdDias));
  }

  @Override
  public void describeTo(Description description) {}
}

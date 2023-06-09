package br.ce.wcaquino.matchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

  private final Integer diaSemana;

  public DiaSemanaMatcher(Integer diaSemana) {
    this.diaSemana = diaSemana;
  }

  @Override
  protected boolean matchesSafely(Date data) {
    return DataUtils.verificarDiaSemana(data, diaSemana);
  }

  @Override
  public void describeTo(Description description) {
    var data = Calendar.getInstance();
    data.set(Calendar.DAY_OF_WEEK, diaSemana);

    var dataExtenso = data.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("pt", "BR"));
    description.appendText(dataExtenso);
  }
}

package foo.view;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

public class V8ScriptTemplateViewResolver extends UrlBasedViewResolver implements ViewResolver {

  public V8ScriptTemplateViewResolver() {
    setViewClass(requiredViewClass());
  }

  public V8ScriptTemplateViewResolver(String prefix, String suffix) {
    this();
    setPrefix(prefix);
    setSuffix(suffix);
  }

  public View resolveViewName(String viewName, Locale locale) throws Exception {
    View v = super.resolveViewName(viewName, locale);

    return v;
  }

  @Override
  protected Class<?> requiredViewClass() {
    return V8ScriptTemplateView.class;
  }

}

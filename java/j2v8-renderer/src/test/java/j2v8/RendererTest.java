package j2v8;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class RendererTest {

  private final Renderer renderer = new Renderer();

  @Test
  public void foo() {

    String[] scripts = new String[] {
      "window.foo = function () { return 'bar'; }",
      "window.bar = function () { return window.requestObject; }",
      "window.add = function (a, b) { return a + b; }",
      "window.main = function () { return foo() + bar() + add(2, 4); }"
    };

    String mainFunction = "main";

    Map<String, Object> model = new HashMap<>();
    model.put("window.requestObject", "foovalue!");

    String result = new Renderer().render(scripts, mainFunction,"", model);

    fail(result);
  }


}

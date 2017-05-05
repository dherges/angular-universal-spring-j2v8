package j2v8;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Renderer {


  public String render(String[] scripts, String mainFunction, String template, Map<String, Object> model) {
    V8 v8 = V8.createV8Runtime("window");

    List<V8Value> runtimeObjects = new ArrayList<>();

    List<V8Value> scriptResults = Arrays.stream(scripts)
      .map(script -> {
        try {
          return v8.executeScript(script);
        } catch (Exception e) {
          throw new IllegalStateException(String.format("Failed to execute script %s", script), e);
        }
      })
      .filter(o -> o instanceof V8Value)
      .map(o -> (V8Value) o)
      .collect(toList());

    runtimeObjects.addAll(scriptResults);

    V8Object modelAttributes = mapToV8Object(v8, runtimeObjects, model);
    runtimeObjects.add(modelAttributes);

    Object html = v8.executeJSFunction(mainFunction, template, modelAttributes/*, messages*/);

    runtimeObjects.forEach(V8Value::release);
    v8.release();

    return String.valueOf(html);
  }







  private V8Object mapToV8Object(V8 v8, List<V8Value> runtimeObjects, Map<String, Object> model) {
    V8Object result = new V8Object(v8);

    model.forEach((key, object) -> {
      if (object == null) {
        result.addNull(key);
      } else if (object instanceof V8Value) {
        result.add(key, (V8Value) object);
      } else if (object instanceof Integer) {
        result.add(key, (Integer) object);
      } else if (object instanceof Double) {
        result.add(key, (Double) object);
      } else if (object instanceof Long) {
        result.add(key, ((Long) object).doubleValue());
      } else if (object instanceof Float) {
        result.add(key, (Float) object);
      } else if (object instanceof Boolean) {
        result.add(key, (Boolean) object);
      } else if (object instanceof String) {
        result.add(key, (String) object);
      } else if (object instanceof Map) {
        result.add(key, mapToV8Object(v8, runtimeObjects, (Map<String, Object>) object));
      } else if (object instanceof Iterable) {
        result.add(key, iterableToV8Array(v8, runtimeObjects, (Iterable<Object>) object));
      } else if (object.getClass().isArray()) {
        result.add(key, iterableToV8Array(v8, runtimeObjects, Arrays.asList((Object[]) object)));
      } else {
        throw new IllegalArgumentException("Unsupported Object of type: " + object.getClass());
      }
    });

    runtimeObjects.add(result);
    return result;
  }

  private V8Array iterableToV8Array(V8 v8, List<V8Value> runtimeObjects, Iterable<Object> iterable) {
    V8Array result = new V8Array(v8);

    iterable.forEach(object -> {
      if (object == null) {
        result.pushNull();
      } else if (object instanceof V8Value) {
        result.push((V8Value) object);
      } else if (object instanceof Integer) {
        result.push((Integer) object);
      } else if (object instanceof Double) {
        result.push((Double) object);
      } else if (object instanceof Long) {
        result.push(((Long) object).doubleValue());
      } else if (object instanceof Float) {
        result.push((Float) object);
      } else if (object instanceof Boolean) {
        result.push((Boolean) object);
      } else if (object instanceof String) {
        result.push((String) object);
      } else if (object instanceof Map) {
        result.push(mapToV8Object(v8, runtimeObjects, (Map<String, Object>) object));
      } else if (object instanceof Iterable) {
        result.push(iterableToV8Array(v8, runtimeObjects, (Iterable<Object>) object));
      } else if (object.getClass().isArray()) {
        result.push(iterableToV8Array(v8, runtimeObjects, Arrays.asList((Object[]) object)));
      } else {
        throw new IllegalArgumentException("Unsupported Object of type: " + object.getClass());
      }
    });

    runtimeObjects.add(result);
    return result;
  }

}

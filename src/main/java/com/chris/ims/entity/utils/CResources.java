package com.chris.ims.entity.utils;

import com.chris.ims.entity.AbstractEntity;
import com.chris.ims.entity.DbTable;
import com.chris.ims.entity.EntField;
import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.security.AuthFacade;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.*;

@Slf4j
public class CResources {

  private CResources() {
    throw new IllegalStateException("Utility class");
  }

  private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("strings");
  private static final HashMap<Integer, String> RESOURCES_VALUES = new HashMap<>();
  private static final HashMap<String, Integer> RESOURCES_IDS = new HashMap<>();
  private static volatile int currentId = 0;

  private static synchronized int getCurrentId() {
    return currentId++;
  }

  public static synchronized int create() {
    return -getCurrentId();
  }

  public static synchronized <T> int create(Class<T> tClass) {
    Res res = tClass.getAnnotation(Res.class);
    if (res == null)
      throw new IllegalStateException("class " + tClass.getSimpleName() + " must be annotated with @Res");

    return create(res.value());
  }

  public static int create(String name) {
    if (RESOURCES_VALUES.containsValue(name)) {
      log.warn("duplicate calls for resource: " + name);
    }

    int id = getCurrentId();
    log.info("creating resource: " + name + " [" + id + "]");
    RESOURCES_IDS.put(name, id);
    RESOURCES_VALUES.put(id, name);
    return id;
  }

  public static int keyOf(String name) {
    Integer id = RESOURCES_IDS.get(name);
    if (id == null)
      throw new IllegalStateException("resource '" + name + "' not found");

    return id;
  }

  public static String getValue(int id) {
    return internalGet(RESOURCES_VALUES.get(id));
  }

  private static String internalGet(String value) {
    try {
      Locale.setDefault(AuthFacade.getCurrentLocale());
      return BUNDLE.getString(value);
    } catch (MissingResourceException e) {
      return "!" + value + "!";
    }
  }

  public static synchronized String getValue(Class<?> clazz) {
    Res res = clazz.getAnnotation(Res.class);
    if (res == null)
      throw new IllegalStateException("class " + clazz.getSimpleName() + " must be annotated with @Res");

    return internalGet(res.value());
  }

  public static String getFormattedValue(int id, Object... args) {
    Object[] formattedValues = Arrays.stream(args).map(CResources::format).toArray();
    MessageFormat format = new MessageFormat(getValue(id), AuthFacade.getCurrentLocale());
    return format.format(formattedValues);
  }

  public static String format(Object value) {
    String label = switch (value) {
      case EntField field -> field.getLabel();
      case AbstractEntity entity -> entity.getLabel();
      case DbTable table -> table.getLabel();
      case null -> "null";
      default -> value.toString();
    };

    if (!label.startsWith("!"))
      return "(" + label + ")";
    return label;
  }

}

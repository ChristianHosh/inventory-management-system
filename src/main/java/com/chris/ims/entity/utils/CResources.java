package com.chris.ims.entity.utils;

import com.chris.ims.entity.annotations.Res;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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

    return create(tClass.getSimpleName());
  }

  public static synchronized int create(String name) {
    if (RESOURCES_VALUES.containsValue(name)) {
      log.warn("duplicate calls for resource: " + name);
    }

    int id = getCurrentId();
    log.info("creating resource: " + name + " [" + id + "]");
    RESOURCES_IDS.put(name, id);
    RESOURCES_VALUES.put(id, name);
    return id;
  }

  public static synchronized int keyOf(String name) {
    Integer id = RESOURCES_IDS.get(name);
    if (id == null)
      throw new IllegalStateException("resource '" + name + "' not found");

    return id;
  }

  public static synchronized String getValue(int id) {
    return internalGet(RESOURCES_VALUES.get(id));
  }

  private static String internalGet(String value) {
    try {
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

}

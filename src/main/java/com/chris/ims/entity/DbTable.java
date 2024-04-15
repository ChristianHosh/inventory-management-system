package com.chris.ims.entity;

import com.chris.ims.entity.utils.CResources;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class DbTable {

  private static final HashMap<Class<? extends AbstractEntity>, DbTable> TABLES = new HashMap<>();

  private final Class<? extends AbstractEntity> tableClass;
  private final Map<Integer, EntField> fields;

  public static DbTable byCode(Class<? extends AbstractEntity> entityClass) {
    return TABLES.get(entityClass);
  }

  DbTable(Class<? extends AbstractEntity> tableClass) {
    this.fields = new HashMap<>();
    this.tableClass = tableClass;
    TABLES.put(tableClass, this);
  }

  void add(EntField field) {
    if (fields.put(field.getId(), field) != null)
      throw new IllegalStateException("field " + field + "  already added to table " + this);
  }

  void add(Field field, int id) {
    EntField entField = new EntField(this, field, id);
    add(entField);
  }


  public EntField findField(int id) {
    return fields.get(id);
  }

  public String getLabel() {
    return CResources.getValue(tableClass);
  }

  @Override
  public String toString() {
    return "DbTable: TABLECLASS=" + getTableClass().getSimpleName();
  }

  void runScripts() {
    for (Class<?> entityClass = tableClass; entityClass != null && entityClass != Object.class; entityClass = entityClass.getSuperclass()) {
      try {
        Method method = entityClass.getMethod("scripts", DbTable.class);
        method.invoke(null, this);
      } catch (NoSuchMethodException e) {
        log.warn("no scripts method found on class " + tableClass);
      } catch (InvocationTargetException e) {
        log.error("invocation target exception: " + e.getMessage(), e);
      } catch (IllegalAccessException e) {
        log.error("illegal access exception: " + e.getMessage(), e);
      }
    }
  }
}

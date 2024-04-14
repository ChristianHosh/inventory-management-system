package com.chris.ims.entity;

import com.chris.ims.entity.utils.CResources;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class DbTable {

  private static final HashMap<Class<? extends AbstractEntity>, DbTable> TABLES = new HashMap<>();

  private final Class<? extends AbstractEntity> tableClass;
  private final List<EntField> fields;

  public static DbTable byCode(Class<? extends AbstractEntity> entityClass) {
    return TABLES.get(entityClass);
  }

  DbTable(Class<? extends AbstractEntity> tableClass) {
    this.fields = new ArrayList<>();
    this.tableClass = tableClass;
    TABLES.put(tableClass, this);
  }

  void add(EntField field) {
    fields.add(field);
  }

  void add(Field field, int id) {
    EntField entField = new EntField(this, field, id);
    add(entField);
  }


  public EntField findField(int id) {
    for (EntField field : fields) {
      if (field.getId() == id) {
        return field;
      }
    }
    return null;
  }

  public String getLabel() {
    return CResources.getValue(tableClass);
  }
}

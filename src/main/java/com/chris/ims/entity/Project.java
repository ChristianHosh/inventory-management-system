package com.chris.ims.entity;

import com.chris.ims.entity.annotations.Res;
import com.chris.ims.entity.utils.CResources;
import io.swagger.v3.core.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class Project {

  private Project() {

  }

  public static void createTable(Class<? extends AbstractEntity> entityClass) {
    log.info("Creating table for {}", entityClass.getSimpleName());
    DbTable table = new DbTable(entityClass);
    for (Field field : ReflectionUtils.getDeclaredFields(entityClass)) {
      Res res = field.getAnnotation(Res.class);
      if (res != null) {
        table.add(field, CResources.keyOf(res.value()));
      }
    }
    table.runScripts();
  }
}

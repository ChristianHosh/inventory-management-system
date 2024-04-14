package com.chris.ims.entity.utils;

import com.chris.ims.entity.user.User;

public interface Resource {

  int F_ID = CResources.create("id");
  int F_NAME = CResources.create("name");
  int F_CREATED_ON = CResources.create("createdOn");
  int F_UPDATED_ON = CResources.create("updatedOn");
  int F_DESCRIPTION = CResources.create("description");
  int F_PARENT = CResources.create("parent");
  int F_KEYWORDS = CResources.create("keywords");
  int F_USER = CResources.create(User.class);

}

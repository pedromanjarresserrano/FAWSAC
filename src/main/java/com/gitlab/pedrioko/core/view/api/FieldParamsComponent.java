package com.gitlab.pedrioko.core.view.api;

import java.util.List;
import java.util.Map;

public interface FieldParamsComponent {

    List<Class> getToClass();

    String getFieldName();

    Map<String, Object> getParam();

}

package com.gitlab.pedrioko.core.lang.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public @interface FieldForm {

}

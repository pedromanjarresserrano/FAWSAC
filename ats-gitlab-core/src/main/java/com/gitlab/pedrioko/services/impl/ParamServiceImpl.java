package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.ParamService;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParamServiceImpl implements ParamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParamServiceImpl.class);

    @Autowired
    private CrudService crudService;


    private AppParam get(String names) {
        PathBuilder<?> pathBuilder = crudService.getPathBuilder(AppParam.class);
        StringPath name = pathBuilder.getString("name");

        return (AppParam) crudService.query().from(pathBuilder).where(name.eq(names)).fetchFirst();
    }


    @Override
    public AppParam getParam(String name) {
        return get(name);
    }

    @Override
    public String getParamValue(String name) {
        return get(name).getValue();
    }

    @Override
    public AppParam saveParam(AppParam param) {
        return crudService.saveOrUpdate(param);
    }

    @Override
    public AppParam saveParam(String name, String value) {
        AppParam param = new AppParam();
        param.setName(name);
        param.setValue(value);
        return crudService.saveOrUpdate(param);
    }
}

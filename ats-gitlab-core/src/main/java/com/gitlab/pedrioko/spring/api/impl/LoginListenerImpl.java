package com.gitlab.pedrioko.spring.api.impl;

import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.LoginLog;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.spring.api.LoginListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginListenerImpl implements LoginListener {

    @Autowired
    private CrudService crudService;

    @Override
    public void onLoginEvent(Usuario usuario) {
        LoginLog loginLog = new LoginLog();
        loginLog.setIp(ApplicationContextUtils.getBean(FHSessionUtil.class).getIpLocal());
        loginLog.setUser(usuario);
        crudService.save(loginLog);
    }

}

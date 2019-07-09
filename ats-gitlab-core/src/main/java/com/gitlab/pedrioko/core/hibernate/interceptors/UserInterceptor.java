package com.gitlab.pedrioko.core.hibernate.interceptors;

import com.gitlab.pedrioko.core.lang.AuditLog;
import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.services.CrudService;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.Executions;

import java.io.Serializable;
import java.util.Date;

@Component
public class UserInterceptor extends EmptyInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyInterceptor.class);

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return false;//logging(entity, "OnLoad", "Load");
    }

    private boolean logging(Object entity, String event, String type) {
        if (!(entity instanceof AuditLog)) {
            try {
                String remoteAddr = Executions.getCurrent().getRemoteAddr();
                Usuario user = ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser();
                AuditLog auditLog = new AuditLog(-1, event, user.getIdusuario() + "", user.getUsername(), type + " " + entity.toString(), new Date(), Long.parseLong(ReflectionJavaUtil.getIdValue(entity).toString()), entity.getClass().getName(), remoteAddr);
                ApplicationContextUtils.getBean(CrudService.class).saveOrUpdate(auditLog);
            } catch (Exception e) {
                LOGGER.error("Error -UserInterceptor()");
            }
        }
        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        return logging(entity, "OnSave", "Save");
    }

    @Override

    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        return logging(entity, "OnUpdate", "Update");
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        logging(entity, "OnDelete", "Delete");
    }


}
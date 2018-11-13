package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.view.reflection.ReflectionJavaUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * The Class FHSessionUtil.
 */
@Component("fhsessionutil")
@Scope("session")
public class FHSessionUtil {

    private final String uuid = UUID.randomUUID().toString();
    @Autowired
    private CrudService crudService;

    /**
     * Gets the current user.
     *
     * @return the current user
     */
    public Usuario getCurrentUser() {
        Object currentUser = getSession().getAttribute("CurrentUser");
        if (currentUser != null)
            return crudService.getById(Usuario.class, ReflectionJavaUtil.getIdValue(currentUser));
        else
            return (Usuario) currentUser;
    }

    /**
     * Sets the current user.
     *
     * @param userProfile the new current user
     */
    public void setCurrentUser(Usuario userProfile) {
        getSession().setAttribute("CurrentUser", userProfile);
    }

    /**
     * Gets the session.
     *
     * @return the session
     */
    public HttpSession getSession() {
        RequestAttributes atr = RequestContextHolder.currentRequestAttributes();
        return (HttpSession) atr.getSessionMutex();
    }

}

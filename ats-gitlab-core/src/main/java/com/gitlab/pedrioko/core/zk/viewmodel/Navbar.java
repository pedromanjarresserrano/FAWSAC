package com.gitlab.pedrioko.core.zk.viewmodel;

import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.navegation.MenuPages;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Navbar {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuPages.class);
    @WireVariable
    private transient ContentView contentView;
    private Usuario user;
    private AImage image;

    @WireVariable("fhsessionutil")
    private transient FHSessionUtil fhsessionutil;
    @WireVariable
    private SecurityService securityService;

    @Init
    public void init() {
        user = fhsessionutil.getCurrentUser();

        loadImage();
        EventQueues.lookup("loadImage", EventQueues.SESSION, true).subscribe(event -> {
            if ("loadImage".equals(event.getName())) {
                user = fhsessionutil.getCurrentUser();
                loadImage();
            }
        });
    }

    @NotifyChange({"image"})
    public void loadImage() {
        try {
            String pic;
            if (user.getPicture() != null) {
                pic = user.getPicture().getUrl();
                if (pic != null && !pic.isEmpty()) {
                    image = new AImage(pic);
                }
            }
        } catch (Exception e) {
            LOGGER.error("ERROR on doAfterCompose()", e);
        }
    }


    @Command
    public void clickAction(@BindingParam("action") MenuProvider e) {
        contentView.addContent(e);
    }

    public String getFullName() {
        return user.getNombres() + " " + user.getApellidos();
    }

    @Command
    public void logout() {
        if (fhsessionutil.getCurrentUser().getTipo() != TipoUsuario.ROLE_USER) {
            Clients.evalJavaScript("disconnect()");
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        fhsessionutil.setCurrentUser(null);
        Messagebox.show("Bye");
        Executions.sendRedirect("/");
    }

    @Command
    public void profile() {
        contentView.addView(Executions.createComponents("~./zul/content/userbasic/profile.zul", null, null), "perfil_tab_id", ReflectionZKUtil.getLabel("perfil"));
    }


    public AImage getImage() {
        return image;
    }

    public void setImage(AImage image) {
        this.image = image;
    }

}

package com.gitlab.pedrioko.core.zk.viewmodel;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.navegation.MenuPages;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;


@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Menu {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuPages.class);

    private transient ContentView target;
    private Usuario user;
    private List<Groupbox> groups;
    private Image image;
    private Label labelnombre;
    private Map<String, List<MenuProvider>> menues;
    private List<MenuProvider> navmenues;

    @Init
    public void init() {
        user = ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser();
        target = ApplicationContextUtils.getBean(ContentView.class);
        groups = new LinkedList<>();
        labelnombre = new Label(user.getNombres() + " " + user.getApellidos());
        image = new Image();
        labelnombre.setZclass("label-nombre");
        image.setSclass("justify-content-between center-block center-block img-circle border border-info profile-sidebar-image");

        List<MenuProvider> beansOfType = ApplicationContextUtils.getBeansOfType(MenuProvider.class);
        List<MenuProvider> menus = new ArrayList<>();
        if (user.getTipo() != TipoUsuario.ROLE_ADMIN) {
            List<String> access = user.getUserprofiles().parallelStream().map(UserProfile::getProvidersaccess)
                    .flatMap(List::stream).map(ProviderAccess::getMenuprovider).collect(Collectors.toList());
            menus.addAll(beansOfType.parallelStream().filter(e -> access.contains(e.getClass().getSimpleName()))
                    .sorted(Comparator.comparingInt(MenuProvider::getPosition)).collect(Collectors.toList()));
        } else {
            menus = beansOfType;
        }

        menues = menus.stream().collect(Collectors.groupingBy(MenuProvider::getGroup));
        navmenues= menues.remove("");
        /*
        loadImage();

        EventQueues.lookup("loadImage", EventQueues.SESSION, true).subscribe(event -> {
            if ("loadImage".equals(event.name())) {
                loadImage();
            }
        });*/
    }

    public void loadImage() {
        try {
            String pic;
            if (user.getPicture() != null) {
                pic = user.getPicture().getUrl();
                if (pic != null && !pic.isEmpty()) {
                    AImage ai = new AImage(pic);
                    image.setContent(ai);
                }
            } else
                image.setSrc("~./zul/images/male.png");
        } catch (Exception e) {
            image.setSrc("~./zul/images/male.png");
            LOGGER.error("ERROR on doAfterCompose()", e);
        }
    }


    @Command
    public void clickAction(@BindingParam("action")MenuProvider e) {
        target.addContent(e);
    }




    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Label getLabelnombre() {
        return labelnombre;
    }

    public void setLabelnombre(Label labelnombre) {
        this.labelnombre = labelnombre;
    }

    public Map<String, List<MenuProvider>> getMenues() {
        return menues;
    }

    public void setMenues(Map<String, List<MenuProvider>> menues) {
        this.menues = menues;
    }

    public List<MenuProvider> getNavmenues() {
        return navmenues;
    }

    public void setNavmenues(List<MenuProvider> navmenues) {
        this.navmenues = navmenues;
    }
}

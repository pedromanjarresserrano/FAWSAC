package com.gitlab.pedrioko.core.view.navegation;

import com.gitlab.pedrioko.core.lang.ProviderAccess;
import com.gitlab.pedrioko.core.lang.UserProfile;
import com.gitlab.pedrioko.core.view.api.ContentView;
import com.gitlab.pedrioko.core.view.api.MenuProvider;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.*;

import java.util.*;
import java.util.stream.Collectors;

public @Data
class MenuPages extends Layout {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuPages.class);

    private Boolean userimage;
    private transient ContentView target;
    private Usuario user;
    private List<Groupbox> groups;
    private Image image;
    private Label labelnombre;

    public MenuPages() {
        user = ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser();
        target = ApplicationContextUtils.getBean(ContentView.class);
        groups = new LinkedList<>();
        labelnombre = new Label(user.getNombres() + " " + user.getApellidos());
        image = new Image();
        labelnombre.setZclass("label-nombre");
        image.setSclass("justify-content-between center-block center-block img-circle border border-info profile-sidebar-image");
        Div div = new Div();
        div.setSclass("image-container justify-content-between center-block ");
        div.appendChild(image);
        Div div2 = new Div();
        div.setAlign("center");
        div2.appendChild(labelnombre);
        div.appendChild(div2);
        appendChild(div);
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

        Map<String, List<MenuProvider>> collect = menus.stream().collect(Collectors.groupingBy(MenuProvider::getGroup));
        collect.forEach((k, v) -> {
            Groupbox gb = getGroupbox(k);
            if (k != "")
                groups.add(gb);

            v.stream().forEach(e -> {
                        Button menuitem = getButton(e);
                        if (k != "") {
                            gb.getChildren().add(getWhiteSeparator());
                            gb.appendChild(menuitem);
                        } else
                            appendChild(menuitem);

                    }
            );
        });

        groups.forEach(this::appendChild);
        //
        loadImage();
        //addEventListener(CrudEvents.ON_AFTER_SIZE, e -> {
        collect.forEach((k, v) -> v.stream().filter(MenuProvider::isOpenByDefault).forEach(target::addContent));
        //});
        EventQueues.lookup("loadImage", EventQueues.SESSION, true).subscribe(event -> {
            if ("loadImage".equals(event.getName())) {
                loadImage();
            }
        });
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

    public Groupbox getGroupbox(String k) {
        Groupbox gb = new Groupbox();
        gb.setId(k);
        gb.setMold("3d");
        gb.setClass("menu-groupbox");
        Caption c = new Caption(ReflectionZKUtil.getLabel(k));
        c.setId("caption" + k);
        c.setIconSclass("fa fa-chevron-down pull-right");
        gb.getChildren().add(c);
        gb.addEventListener(Events.ON_CLICK, (Event e) ->
                groups.forEach(g -> {
                    if (g.getId() != k)
                        g.setOpen(false);
                })
        );
        gb.setOpen(false);
        return gb;
    }

    public Button getButton(MenuProvider e) {
        Button menuitem = new Button();
        menuitem.setIconSclass(e.getIcon());
        menuitem.setLabel(e.getLabel());
        menuitem.addEventListener(Events.ON_CLICK, w -> target.addContent(e));
        menuitem.setClass("btn-menu-siderbar");
        return menuitem;
    }


    private Separator getWhiteSeparator() {
        Separator separator = new Separator();
        separator.setHeight("1px");
        separator.setStyle("background:white; width:100%;");
        return separator;
    }

}

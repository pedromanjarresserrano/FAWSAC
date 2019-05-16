package com.gitlab.pedrioko.core.zk.widgets;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Anuncio;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@VariableResolver(DelegatingVariableResolver.class)
public class Anuncios extends SelectorComposer<Window> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Anuncios.class);

    private static final String OVERFLOW_Y_AUTO_IMPORTANT_OVERFLOW_X_AUTO_IMPORTANT = "overflow-y: auto !important; overflow-x: auto !important;";
    @WireVariable
    private transient CrudService crudService;
    @WireVariable
    private transient FHSessionUtil fhsessionutil;
    @WireVariable
    private transient StorageService storageservice;
    @Wire
    private Div imagesdiv;

    @Override
    public void doAfterCompose(Window comp) throws Exception {
        super.doAfterCompose(comp);
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        fhsessionutil = ApplicationContextUtils.getBean(FHSessionUtil.class);
        storageservice = ApplicationContextUtils.getBean(StorageService.class);
        load(comp);
        EventQueues.lookup("saveAnuncio", EventQueues.APPLICATION, true).subscribe(event -> {
            if ("saveAnuncio".equals(event.getName())) {
                load(comp);
            }
        });
    }

    public void load(Window comp) {
        List<Component> collect = imagesdiv.getChildren().stream().filter(e -> !(e instanceof Image)).collect(Collectors.toList());
        imagesdiv.getChildren().clear();
        imagesdiv.getChildren().addAll(collect);
        List<Anuncio> all = crudService.getAll(Anuncio.class);
        all.forEach(e -> {
            List<TipoUsuario> typelistuser = e.getTypelistuser();
            if (typelistuser.contains(fhsessionutil.getCurrentUser().getTipo())
                    || typelistuser.contains(TipoUsuario.ALL)) {
                FileEntity urlbanner = e.getUrlbanner();
                File file = storageservice.getFile(urlbanner.getFilename());
                if (urlbanner != null) {
                    AImage ai;
                    try {
                        ai = new AImage(file);
                        Image image = new Image();
                        image.setContent(ai);
                        image.setHeight("290px");
                        image.setClass("child mySlides img-responsive");
                        imagesdiv.appendChild(image);
                        if (e.getContent() != null && !e.getContent().isEmpty()) {
                            image.setTooltip("Click me!");
                            image.addEventListener(Events.ON_CLICK, w -> {
                                Window view = new Window();
                                view.setParent(comp.getParent());
                                view.setClosable(true);
                                view.setTitle(e.getNombre());
                                Html content = new Html(e.getContent());
                                content.setHflex("1");
                                content.setVflex("1");
                                view.setContentStyle(OVERFLOW_Y_AUTO_IMPORTANT_OVERFLOW_X_AUTO_IMPORTANT);
                                view.setStyle(OVERFLOW_Y_AUTO_IMPORTANT_OVERFLOW_X_AUTO_IMPORTANT);
                                Div section = new Div();
                                section.setZclass("section");
                                Div container = new Div();
                                container.setZclass("container");
                                section.appendChild(container);
                                Div row = new Div();
                                row.setZclass("row");
                                container.appendChild(row);
                                row.appendChild(content);
                                view.appendChild(section);
                                view.setHeight("90%");
                                view.setWidth("90%");
                                view.doModal();
                            });
                        }
                    } catch (IOException e1) {
                        LOGGER.error("ERROR on doAfterCompose()", e);

                    }
                }
            }
        });
    }

}

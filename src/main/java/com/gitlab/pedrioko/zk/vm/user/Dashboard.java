package com.gitlab.pedrioko.zk.vm.user;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.ChosenItem;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.zk.component.ChosenBoxImage;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The Class Dashboard.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class Dashboard extends SelectorComposer<Window> {

    private static final long serialVersionUID = 1L;


    @WireVariable
    private transient FHSessionUtil fhsessionutil;
    @Wire
    private Div content;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.zkoss.zk.ui.util.Composer#doAfterCompose(org.zkoss.zk.ui.Component)
     */
    @Override
    public void doAfterCompose(Window window) throws Exception {
        super.doAfterCompose(window);
        List<DashBoardComponent> listView = ApplicationContextUtils
                .getBeansOfType(DashBoardComponent.class)
                .stream()
                .sorted(Comparator.comparingInt(DashBoardComponent::getPosicion))
                .collect(Collectors.toList());

        for (DashBoardComponent e : listView) {
            List<TipoUsuario> collect = Arrays.asList(e.getForUser());

            if (!collect.isEmpty() && (collect.contains(TipoUsuario.ALL)
                    || collect.contains(fhsessionutil.getCurrentUser().getTipo()))) {
                Panel content = e.getContent();
                if (e.haveViewMore()) {
                    Button viewmore = new Button("View More");
                    viewmore.setSclass("pull-right btn-success");
                    viewmore.addEventListener(Events.ON_CLICK, w -> {
                        Window windowviewmore = (Window) Executions.createComponents(e.urlViewMore(), null, null);
                        windowviewmore.setSclass("col-md-10");
                        windowviewmore.setBorder("normal");
                        windowviewmore.setWidth("70%");
                        windowviewmore.setHeight("80%");
                        windowviewmore.setTitle(e.getLabel());
                        windowviewmore.setClosable(true);
                        windowviewmore.doModal();
                    });
                    viewmore.setStyle("margin-bottom:10px; margin-right:10px;");
                    content.getChildren().get(0).appendChild(viewmore);
                }
                content.setStyle("margin-bottom:10px;");
                content.setCollapsible(true);
                content.setTitle(e.getLabel());
                content.setClosable(true);
                content.setBorder("normal");
                content.getChildren().forEach(w -> w.setId(""));
                content.setSclass("col-lg-6 col-md-6 col-sm-12");
                this.content.appendChild(content);
            }
        }
    }

    public boolean isMobile() {
        return Executions.getCurrent().getBrowser("mobile") != null;
    }
}

package com.gitlab.pedrioko.zk.composer;

import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.West;

/**
 * The Class SidebarComposer.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class BarsComposer extends SelectorComposer<Component> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2961593954959975127L;

    /**
     * The navbar.
     */
    @Wire
    private Borderlayout borderlayout;

    @Wire
    private Button collapser;


    @WireVariable
    private transient FHSessionUtil fhsessionutil;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.zkoss.zk.ui.select.SelectorComposer#doAfterCompose(org.zkoss.zk.ui.
     * Component)
     */
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        West west = borderlayout.getWest();
        setIcon(collapser);
        if (ZKUtil.isMobile()) {
            west.setOpen(false);
            west.setCollapsible(false);
        }
        collapser.setClass("btn-siderbar-collapser pull-left height100");
        collapser.setWidth("60px");
        collapser.setHeight("54px");
        collapser.setStyle("margin-top:-10px;");
        setOnClickEvent(collapser, west);
        collapser.setIconSclass("fa fa-bars");
        collapser.addEventListener(Events.ON_MOUSE_OVER, e -> collapser.setIconSclass(west.isSlide() ? "z-icon-arrow-left" : "z-icon-arrow-right"));
        collapser.addEventListener(Events.ON_MOUSE_OUT, e -> setIcon(collapser));
        borderlayout.addEventListener(Events.ON_MOUSE_OVER, e -> borderlayout.resize());
        collapser.setVisible(ZKUtil.isMobile());
        Clients.evalJavaScript("connect()");
        if (fhsessionutil.getCurrentUser().getTipo() != TipoUsuario.ROLE_TURISTA)
            comp.addEventListener(Events.ON_CREATE, e -> Clients.evalJavaScript("connectAdminTopic()"));
    }


    private void setIcon(Button menuitemcollapser) {
        menuitemcollapser.setIconSclass("fa fa-bars");
    }

    private void setOnClickEvent(Button b, West west) {
        b.addEventListener(Events.ON_CLICK, w -> {
            ZKUtil.tootgleRegion(west,"220px");
        });
    }
}

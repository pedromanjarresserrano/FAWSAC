package com.gitlab.pedrioko.zk.composer;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.Usuario;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.*;

/**
 * The Class NavbarComposer.
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class NavbarComposer extends SelectorComposer<Component> {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NavbarComposer.class);

    /**
     * The atask.
     */
    @Wire
    private A atask;

    /**
     * The anoti.
     */
    @Wire
    private A anoti;

    /**
     * The amsg.
     */
    @Wire
    private A amsg;

    /**
     * The menu user.
     */
    @Wire
    private Menu menuUser;
    /**
     * The image.
     */
    @Wire("#avatar")
    private Image image;
    @WireVariable
    private transient FHSessionUtil fhsessionutil;

    @Wire
    private Menupopup editPopup;

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
        loadImage();
        image.addEventListener(Events.ON_MOUSE_OVER, e -> editPopup.open(image, "after_end"));
        image.addEventListener(Events.ON_CLICK, e -> {
            if (editPopup.isVisible())
                editPopup.close();
            else
                editPopup.open(image, "after_end");
        });
        EventQueues.lookup("loadImage", EventQueues.SESSION, true).subscribe(event -> {
            if ("loadImage".equals(event.getName())) {
                loadImage();
            }
        });
    }

    @GlobalCommand("loadImage")
    @Command("loadImage")
    @NotifyChange("image")
    public void loadImage() {
        Usuario user = fhsessionutil.getCurrentUser();
        try {

            FileEntity picture = user.getPicture();
            if (picture == null)
                image.setSrc("~./zul/images/male.png");
            else {
                String pic = picture.getUrl();
                if (pic != null && !pic.isEmpty()) {
                    AImage ai = new AImage(pic);
                    image.setContent(ai);
                }
            }
        } catch (Exception e) {
            image.setSrc("~./zul/images/male.png");

            LOGGER.error("ERROR on doAfterCompose()", e);
        }
    }

    /**
     * Show bye.
     */
    @Listen("onClick = menuitem#miLogout")
    public void showBye() {
        if (fhsessionutil.getCurrentUser().getTipo() != TipoUsuario.ROLE_TURISTA) {
            Clients.evalJavaScript("disconnect()");
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        fhsessionutil.setCurrentUser(null);
        Messagebox.show("Bye");
        Executions.sendRedirect("/");
    }

    /**
     * Toggle noti popup.
     *
     * @param event the event
     */
    @Listen("onOpen = #notipp")
    public void toggleNotiPopup(OpenEvent event) {
        toggleOpenClass(event.isOpen(), anoti);
    }

    /**
     * Toggle open class.
     *
     * @param open      the open
     * @param component the component
     */
    // Toggle open class to the component
    private void toggleOpenClass(Boolean open, Component component) {
        HtmlBasedComponent comp = (HtmlBasedComponent) component;
        String scls = comp.getSclass();
        if (open) {
            //comp.setSclass(scls + " open");
            comp.setStyle("display:block;");

        } else {
            comp.setStyle("display:none;");
            //comp.setSclass(scls.replace(" open", ""));
        }
    }

}

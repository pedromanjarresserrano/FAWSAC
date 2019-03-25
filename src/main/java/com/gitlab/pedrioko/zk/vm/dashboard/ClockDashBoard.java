package com.gitlab.pedrioko.zk.vm.dashboard;

import com.gitlab.pedrioko.core.zk.component.Clock;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent;
import org.springframework.stereotype.Component;
import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

/**
 * The Class TestStatsDashBoard.
 */
@Component
public class ClockDashBoard implements DashBoardComponent {

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #getContent()
     */
    @Override
    public Panel getContent() {
        Panel panel = new Panel();
        Panelchildren panelchildren = new Panelchildren();
        panelchildren.appendChild(new Clock("America/Bogota"));
        panel.appendChild(panelchildren);
        return panel;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #getForUser()
     */
    @Override
    public TipoUsuario[] getForUser() {
        return new TipoUsuario[]{TipoUsuario.ALL};
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #getIcon()
     */
    @Override
    public String getIcon() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #getLabel()
     */
    @Override
    public String getLabel() {
        return Labels.getLabel("reloj");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #getPosicion()
     */
    @Override
    public int getPosicion() {
        return 2;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #hasViewMore()
     */
    @Override
    public boolean haveViewMore() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.gitlab.pedrioko.zk.composer.interfaces.DashBoardComponent
     * #urlViewMore()
     */
    @Override
    public String urlViewMore() {
        return null;
    }

}

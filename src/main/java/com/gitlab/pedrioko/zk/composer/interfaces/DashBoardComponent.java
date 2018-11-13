package com.gitlab.pedrioko.zk.composer.interfaces;

import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.zkoss.zul.Panel;

/**
 * The Interface VMAstractView.
 */
public interface DashBoardComponent {

    /**
     * Checks for view more.
     *
     * @return true, if successful
     */
    public boolean haveViewMore();

    /**
     * Url view more.
     *
     * @return the string
     */
    public String urlViewMore();

    /**
     * Gets the content URL.
     *
     * @return the content URL
     */
    public Panel getContent();

    /**
     * Gets the for user.
     *
     * @return the for user
     */
    public TipoUsuario[] getForUser();

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon();

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel();

    /**
     * Gets the posicion.
     *
     * @return the posicion
     */
    public int getPosicion();

}

package com.gitlab.pedrioko.zk.composer.util;

import org.zkoss.gmaps.Gmaps;
import org.zkoss.zk.ui.event.Events;

/**
 * The Class CustomGmaps.
 */
public class CustomGmaps extends Gmaps {

    /**
     * The Constant BIND.
     */
    private static final String BIND = "onAfterRealBind";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4982965989007143005L;

    static {
        addClientEvent(CustomGmaps.class, BIND, CE_IMPORTANT | CE_NON_DEFERRABLE);
    }

    /**
     * Instantiates a new custom gmaps.
     */
    public CustomGmaps() {
        setWidgetOverride("_initListeners", "function(n) {\n" + "                      this.$_initListeners(n);\n"
                + "                      var wgt = this;\n" + "                      setTimeout ( function () {\n"
                + "                          wgt.fireX(new zk.Event(wgt, 'onAfterRealBind', {}, {toServer: true}));\n"
                + "                      }, 0);\n" + "                  }\n");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.zkoss.gmaps.Gmaps#service(org.zkoss.zk.au.AuRequest, boolean)
     */
    @Override
    public void service(org.zkoss.zk.au.AuRequest request, boolean everError) {
        final String cmd = request.getCommand();
        if (BIND.equals(cmd)) {
            Events.postEvent(BIND, this, null);
        } else
            super.service(request, everError);
    }
}
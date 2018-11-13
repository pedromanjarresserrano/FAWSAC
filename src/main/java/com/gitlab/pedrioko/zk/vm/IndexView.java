package com.gitlab.pedrioko.zk.vm;

import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.impl.CrudServiceImpl;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;

/**
 * The Class IndexView.
 */
@SuppressWarnings("rawtypes")
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class IndexView extends GenericForwardComposer {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The my service.
     */
    @WireVariable
    private transient CrudService crudService;
    @WireVariable
    private transient FHSessionUtil fhsessionutil;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.
     * ui.Component)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        crudService = ApplicationContextUtils.getBean(CrudServiceImpl.class);
    }

    /**
     * On user$info.
     *
     * @param event the event
     */
    public void onUser$info(Event event) {
        ForwardEvent eventx = (ForwardEvent) event;
        if (fhsessionutil.getCurrentUser().getTipo().equals(TipoUsuario.ROLE_ENTIDADCONTROL)) {
            fhsessionutil.getCurrentUser().setToken(eventx.getOrigin().getData().toString());
            crudService.save(fhsessionutil.getCurrentUser());
        }

    }


}

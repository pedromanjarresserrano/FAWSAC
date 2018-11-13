package com.gitlab.pedrioko.core.view.viewers;


import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.core.view.util.FHSessionUtil;
import com.gitlab.pedrioko.core.view.util.ZKUtil;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import com.gitlab.pedrioko.services.CrudService;
import lombok.Data;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Html;
import org.zkoss.zul.Window;

public @Data
class FireBase extends Window {

    private transient CrudService crudService;
    private transient FHSessionUtil fhsessionutil;
    private String apiKey;
    private String authDomainM;
    private String databaseURL;
    private String projectId;
    private String storageBucket;
    private String messagingSenderId;

    public FireBase() {
        setVisible(false);
        crudService = ApplicationContextUtils.getBean(CrudService.class);
        fhsessionutil = ApplicationContextUtils.getBean(FHSessionUtil.class);

        EventQueues.lookup("Emergencia", EventQueues.APPLICATION, true).subscribe(event -> {
            if ("Emergencia".equals(event.getName())) {
                ZKUtil.showMessage("Emergencia", MessageType.INFO);
            }
        });
    }

    private void init() {
        setId("firebase");
        if (apiKey == null || apiKey.isEmpty())
            return;
        if (authDomainM == null || authDomainM.isEmpty())
            return;
        if (databaseURL == null || databaseURL.isEmpty())
            return;
        if (projectId == null || projectId.isEmpty())
            return;
        if (storageBucket == null || storageBucket.isEmpty())
            return;
        if (messagingSenderId == null || messagingSenderId.isEmpty())
            return;
        Html html = new Html();
        html.setContent("<![CDATA[ <!-- Firebase App is always required and must be first -->\n" +
                "<script src=\"https://www.gstatic.com/firebasejs/5.4.1/firebase-app.js\"></script>\n" +
                "\n" +
                "<!-- Add additional services that you want to use -->\n" +
                "<script src=\"https://www.gstatic.com/firebasejs/5.4.1/firebase-auth.js\"></script>\n" +
                "<script src=\"https://www.gstatic.com/firebasejs/5.4.1/firebase-messaging.js\"></script>\n" +
                "\t\t\t<script type=\"text/javascript\"> " +
                "\n" +
                "var config = {\n" +
                "    apiKey: \"" + apiKey + "\",\n" +
                "    authDomain: \"" + authDomainM + "\",\n" +
                "    databaseURL: \"" + databaseURL + "\",\n" +
                "    projectId: \"" + projectId + "\",\n" +
                "    storageBucket: \"" + storageBucket + "\",\n" +
                "    messagingSenderId: \"" + messagingSenderId + "\"\n" +
                "};\n" +
                "firebase.initializeApp(config);\n" +
                "const messaging = firebase.messaging();\n" +
                "messaging.requestPermission().then(function () {\n" +
                "    console.log('have permition');\n" +
                "    return messaging.getToken();\n" +
                "}).then(function (token) {\n" +
                "    payload = token;\n" +
                "    zAu.send(new zk.Event(zk.Widget.$(this), 'onUser', payload));\n" +
                "    console.log(token);\n" +
                "}).catch(function (err) {\n" +
                "    console.log('Error Occured.');\n" +
                "    console.log(err);\n" +
                "});\n" +
                "messaging.onMessage(function (payload) {\n" +
                "    console.log('onMessage', payload);\n" +
                "}); </script> " +
                "]]>");
        appendChild(html);
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        init();
    }

    public void setAuthDomainM(String authDomainM) {
        this.authDomainM = authDomainM;
        init();
    }

    public void setDatabaseURL(String databaseURL) {
        this.databaseURL = databaseURL;
        init();
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
        init();
    }

    public void setStorageBucket(String storageBucket) {
        this.storageBucket = storageBucket;
        init();
    }

    public void setMessagingSenderId(String messagingSenderId) {
        this.messagingSenderId = messagingSenderId;
        init();
    }

    public void onUser(Event event) {
        TipoUsuario tipo = fhsessionutil.getCurrentUser().getTipo();
        if (!tipo.equals(TipoUsuario.ROLE_TURISTA)) {
            fhsessionutil.getCurrentUser().setToken(event.getData().toString());
            crudService.save(fhsessionutil.getCurrentUser());
        }
    }

    public void onUser$info(Event event) {
        ForwardEvent eventx = (ForwardEvent) event;
        if (fhsessionutil.getCurrentUser().getTipo().equals(TipoUsuario.ROLE_ENTIDADCONTROL)) {
            fhsessionutil.getCurrentUser().setToken(eventx.getOrigin().getData().toString());
            crudService.save(fhsessionutil.getCurrentUser());
        }
    }
}


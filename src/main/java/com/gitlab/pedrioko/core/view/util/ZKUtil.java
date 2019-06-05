package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.LayoutRegion;
import org.zkoss.zul.Window;

public class ZKUtil {

    private ZKUtil() {
    }

    public static void showMessage(String message) {
        if (ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser().getTipo() != TipoUsuario.ROLE_USER)
            showMessage(message, MessageType.ERROR);
    }

    public static void showMessage(String message, MessageType type, TipoUsuario tipoUsuario) {
        if (ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser().getTipo() == tipoUsuario)
            showMessage(message, type);

    }

    public static void showMessage(String message, MessageType type) {
        String string = "alertify." + getTypeMessage(type) + "('" + message + "', 5 ); ";
        Clients.evalJavaScript(string);
    }

    private static String getTypeMessage(MessageType type) {
        String typetoast;
        switch (type) {
            case WARNING:
                typetoast = "warning";
                break;
            case ERROR:
                typetoast = "error";
                break;
            case SUCCESS:
                typetoast = "success";
                break;
            case INFO:
                typetoast = "message";
                break;
            default:
                typetoast = "message";
                break;
        }
        return typetoast;
    }

    public static boolean tootgleRegion(LayoutRegion region) {
        if (isMobile())
            return tootgleRegion(region, "100%");
        else
            return tootgleRegion(region, "320px");
    }

    public static boolean tootgleRegion(LayoutRegion region, String width) {
        String widthaux = width == null || width.isEmpty() ? "220px" : width;
        if (ZKUtil.isMobile()) {
            boolean open = !region.isSlide();
            region.setSlide(open);
            if (!open) {
                region.setWidth(widthaux);
            }
            return open;
        } else {
            boolean open = !region.isOpen();
            if (open) {
                region.setStyle("width:" + widthaux + ";");
            }
            region.setOpen(open);
            region.setCollapsible(true);
            if (!region.isVisible()) region.setVisible(true);
            return open;
        }
    }

    public static boolean isMobile() {
        Execution current = Executions.getCurrent();
        return current != null && current.getBrowser("mobile") != null;
    }

    public static void showDialogWindow(Component window) {
        Page currentPage = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
        Window modal = new Window();
        modal.setClosable(true);
        modal.setTitle(ReflectionZKUtil.getLabel("View"));
        modal.setBorder("normal");
        modal.setSclass("w-75");
        modal.setParent(currentPage.getFirstRoot());
        modal.appendChild(window);
        modal.setHeight("70vh");
        modal.doModal();
    }

    public static void showDialogWindow(Window window) {
        Page currentPage = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
        window.setClosable(true);
        window.setBorder("normal");
        window.setParent(currentPage.getFirstRoot());
        window.setSclass("window-overlapped");
        window.doModal();
    }
}

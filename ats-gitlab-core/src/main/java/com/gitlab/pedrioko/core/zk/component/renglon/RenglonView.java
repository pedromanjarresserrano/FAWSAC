package com.gitlab.pedrioko.core.zk.component.renglon;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class RenglonView extends Div {

    protected HtmlBasedComponent view;
    protected String label;
    protected String _aclass = "col-12 col-sm-12 col-md-12 col-lg-12";

    public RenglonView() {
        setZclass(_aclass);
    }

    public HtmlBasedComponent getView() {
        return view;
    }

    public void setView(HtmlBasedComponent view) {
        this.view = view;
        smartUpdate("view", view);
        view.setSclass(_aclass);
        getChildren().clear();
        appendChild(new Label(label));
        appendChild(view);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        // labelC.setModel(this.label);
        appendChild(new Label(label));
        smartUpdate("label", label);

    }

    public String getAclass() {
        return _aclass;
    }

    public void setAclass(String _sclass) {
        _aclass = _sclass;
        super.setSclass(_sclass);
        if (view != null) view.setSclass(_sclass);
        smartUpdate("sclass", _sclass);

    }
}

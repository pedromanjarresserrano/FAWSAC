package com.gitlab.pedrioko.core.zk.component;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class Renglon extends org.zkoss.zul.Div {

    protected HtmlBasedComponent input;
    protected String label;
    protected String _sclass = "col-12 col-sm-12 col-md-6 col-lg-6 form-group row";
    protected String _labelSclass = "col-sm-3 col-form-label";
    protected String _inputSclass = "col-sm-9  order-last";
    protected String _inputinnerSclass = "text-dark";
    private final Div inputdiv;
    private Label labelC;

    public Renglon() {
        setZclass(_sclass);
        labelC = new Label("");
        labelC.setZclass(_labelSclass);
        appendChild(labelC);
        inputdiv = new Div();
        inputdiv.setZclass(_inputSclass);
        appendChild(inputdiv);
    }

    public HtmlBasedComponent getInput() {
        return input;
    }

    public void setInput(HtmlBasedComponent input) {
        this.input = input;
        smartUpdate("input", input);
        inputdiv.appendChild(input);
        input.setSclass(_inputSclass);

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        labelC.setValue(this.label);
        smartUpdate("label", label);
    }

    public String get_sclass() {
        return _sclass;
    }

    public void set_sclass(String _sclass) {
        this._sclass = _sclass;
        smartUpdate("_sclass", _sclass);
    }

    public String getlLabelSclass() {
        return _labelSclass;
    }

    public void setLabelSclass(String _labelSclass) {
        this._labelSclass = _labelSclass;
        labelC.setZclass(_labelSclass);
        smartUpdate("_labelSclass", _labelSclass);
    }

    public String get_inputSclass() {
        return _inputSclass;
    }

    public void set_inputSclass(String _inputSclass) {
        this._inputSclass = _inputSclass;
        smartUpdate("_inputSclass", _inputSclass);
    }

    public String get_inputinnerSclass() {
        return _inputinnerSclass;

    }

    public void set_inputinnerSclass(String _inputinnerSclass) {
        this._inputinnerSclass = _inputinnerSclass;
        smartUpdate("_inputinnerSclass", _inputinnerSclass);

    }
}

package com.gitlab.pedrioko.core.view.viewers.crud.grid;

import com.gitlab.pedrioko.core.lang.annotation.AlphabetSearch;
import com.gitlab.pedrioko.core.view.viewers.crud.controllers.CrudController;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.A;
import org.zkoss.zul.Div;

public class AlphabetFilter extends Div {

    private final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public AlphabetFilter(CrudController controlle, Class klass) {
        String field = null;
        AlphabetSearch alphabetSearch = (AlphabetSearch) klass.getAnnotation(AlphabetSearch.class);
        if (alphabetSearch != null) {
            field = alphabetSearch.field();
        }
        load(controlle, field, "ALL");
        for (char letter : alphabet) {
            load(controlle, field, letter + "");
        }
        this.setSclass("color-system");
        this.setStyle("justify-content: center; display: flex;flex-direction: row;flex-wrap: wrap;padding-top: 10px; padding-bottom: 10px;");
    }

    protected void load(CrudController controlle, String field, String letter) {
        A a = new A();
        a.setLabel(letter + "");
        a.setStyle("padding-bottom: 14px; padding-left: 10px;padding-right: 10px;font-size:25px;");
        String finalField = field;
        a.addEventListener(Events.ON_CLICK, e -> {
            controlle.doQueryStringBegin(finalField, letter);

        });
        this.appendChild(a);
    }
}

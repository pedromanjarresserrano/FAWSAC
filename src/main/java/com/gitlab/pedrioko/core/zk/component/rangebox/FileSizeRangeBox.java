package com.gitlab.pedrioko.core.zk.component.rangebox;

import com.gitlab.pedrioko.core.lang.FileSizeRange;
import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import org.apache.commons.io.FileUtils;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Div;

import java.math.BigDecimal;
import java.util.Arrays;

public class FileSizeRangeBox extends Div {
    private static final long serialVersionUID = -8290174092737445717L;
    Decimalbox fin = new Decimalbox();
    Decimalbox inicio = new Decimalbox();
    private FileSizeRange value;
    String[] sizes = new String[]{"KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
    private Combobox comboboxinicio;
    private Combobox comboboxfin;

    public FileSizeRangeBox() {
        value = new FileSizeRange();
        load();

    }

    private void load() {
        inicio.setWidth("100%");
        fin.setWidth("100%");

        Div hlayout = new Div();
        hlayout.setStyle("display:flex;");
        hlayout.appendChild(inicio);
        comboboxinicio = new Combobox();
        comboboxinicio.setWidth("60px");
        ReflectionZKUtil.populate(comboboxinicio, Arrays.asList(sizes), true);
        hlayout.appendChild(comboboxinicio);
        hlayout.setWidth("100%");
        appendChild(hlayout);

        hlayout = new Div();
        hlayout.setStyle("display:flex;");
        hlayout.appendChild(fin);
        comboboxfin = new Combobox();
        comboboxfin.setWidth("60px");
        ReflectionZKUtil.populate(comboboxfin, Arrays.asList(sizes), true);
        hlayout.appendChild(comboboxfin);
        hlayout.setWidth("100%");
        appendChild(hlayout);
    }

    public FileSizeRange getValue() {
        BigDecimal inicio = this.inicio.getValue();

        BigDecimal fin = this.fin.getValue();
        if (inicio != null && fin != null) {
            value = new FileSizeRange();
            value.setInicio(getBytes(inicio, comboboxinicio.getValue()));
            value.setFin(getBytes(fin, comboboxfin.getValue()));
            return value;
        } else {
            return null;
        }
    }

    private BigDecimal getBytes(BigDecimal decimal, String metric) {
        switch (metric) {
            case ("KB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 10D)));
            }
            case ("MB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 20D)));
            }
            case ("GB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 30D)));
            }
            case ("TB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 40D)));
            }
            case ("PB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 50D)));
            }
            case ("EB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 60D)));
            }
            case ("ZB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 70D)));
            }
            case ("YB"): {
                return decimal.multiply(BigDecimal.valueOf(Math.pow(2D, 80D)));
            }
            default:
                return BigDecimal.ZERO;
        }
    }

    public void setValue(FileSizeRange value) {
        this.value = value;
        String displaySize = FileUtils.byteCountToDisplaySize(value.getInicio().longValue());
        String[] split = displaySize.split(" ");
        value.setInicio(BigDecimal.valueOf(Long.parseLong(split[0])));
        comboboxinicio.setSelectedIndex(Arrays.asList(sizes).indexOf(split));

        displaySize = FileUtils.byteCountToDisplaySize(value.getFin().longValue());
        split = displaySize.split(" ");
        value.setInicio(BigDecimal.valueOf(Long.parseLong(split[0])));
        comboboxfin.setSelectedIndex(Arrays.asList(sizes).indexOf(split));
    }
}


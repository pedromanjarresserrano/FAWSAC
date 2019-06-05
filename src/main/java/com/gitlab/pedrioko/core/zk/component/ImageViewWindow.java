package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.view.reflection.ReflectionZKUtil;
import com.gitlab.pedrioko.core.zk.component.window.Window;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
public @Data
class ImageViewWindow extends Window {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageViewWindow.class);
    protected transient BitMatrix matrix;
    protected transient Writer writer = new MultiFormatWriter();
    private Image contImage;

    public ImageViewWindow(String value, BarcodeFormat type) {
        contImage = new Image();
        Div div = new Div();
        try {
            EnumMap<EncodeHintType, String> hints = new EnumMap<>(EncodeHintType.class);

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            matrix = writer.encode(value != null ? value : "Vacio", type, 500, 500, hints);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            contImage.setContent(new AImage("image" + UUID.randomUUID().toString(), output.toByteArray()));
            contImage.setWidth("100%");
            contImage.setSclass("block img-thumbnail img-responsive");
            div.appendChild(contImage);
        } catch (Exception e) {
            LOGGER.error("Error", e);
        }
        Button btn = new Button();
        btn.setLabel(ReflectionZKUtil.getLabel("descargar"));
        btn.setClass("btn btn-block btn-primary btn-lg");
        div.setSclass("col-xs-5 base-boxe");
        btn.addEventListener(Events.ON_CLICK, e -> Filedownload.save(contImage.getContent(), null));
        div.appendChild(btn);
        appendChild(div);
        setHeight("100%");
    }

}

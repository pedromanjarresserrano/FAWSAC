package com.gitlab.pedrioko.core.zk.component.upload;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import lombok.Data;
import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.Base64;

public @Data
class ImageUpload extends HtmlBasedComponent {
    private String rawValue = "";
    private String filename = "";

    public ImageUpload() {
    }

    public ImageUpload(String rawvalue) {
        this.rawValue = rawvalue;
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "rawValue", rawValue);
    }


    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
        smartUpdate("rawValue", rawValue);
    }

    public void updateValue(Event evt) throws Exception {
        JSONObject data = (JSONObject) evt.getData();
        if (data != null) {
            this.rawValue = (String) data.get("value");
            this.filename = (String) data.get("filename");
            for (EventListener<? extends Event> e : this.getEventListeners(Events.ON_CHANGE)) {
                e.onEvent(null);
            }
        }
    }

    public FileEntity getValue() {
        try {
            byte[] decode = Base64.getMimeDecoder().decode(this.rawValue.split(",")[1]);
            return ApplicationContextUtils.getBean(StorageService.class).saveFileToFileEntity(this.filename, decode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setValue(FileEntity value) {
        if (value != null && value.getFilename() != null) {
            String urlFile = ApplicationContextUtils.getBean(StorageService.class).getUrlFile(value.getFilename(), false).replace("//", "/");
            this.rawValue = urlFile;
            this.filename = value.getFilename();
        } else {
            this.rawValue = "#";
            this.filename = "";
        }
        smartUpdate("rawValue", rawValue);
        smartUpdate("filename", filename);
    }

    public void setValue(byte[] value) {
        this.rawValue = Base64.getEncoder().encodeToString(value);
        smartUpdate("rawValue", rawValue);
    }

    public void setValue(String value) {
        this.rawValue = value;
        smartUpdate("rawValue", value);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        smartUpdate("filename", filename);

    }
}

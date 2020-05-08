package com.gitlab.pedrioko.core.zk.component.upload;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import lombok.Data;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.au.AuRequests;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import static com.gitlab.pedrioko.core.view.util.ApplicationContextUtils.getBean;

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

    public FileEntity getValue() {
        try {
            byte[] decode = Base64.getMimeDecoder().decode(this.rawValue.split(",")[1]);
            return getBean(StorageService.class).saveFileToFileEntity(this.filename, decode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setValue(FileEntity value) {
        if (value != null && value.getFilename() != null) {
            String urlFile = getBean(StorageService.class).getUrlFile(value.getFilename(), false).replace("//", "/");
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

    @Override
    public void service(AuRequest request, boolean everError) {

        String cmd = request.getCommand();
        Object value;
        switch (cmd) {
            case "onChange": {
                Map<String, Object> data = request.getData();
                String rawValue = (String) data.get("value");
                String filename = (String) data.get("filename");
                InputEvent evt = new InputEvent(cmd, this, rawValue, this.rawValue, AuRequests.getBoolean(data, "bySelectBack"), AuRequests.getInt(data, "start", 0));
                Events.postEvent(evt);
                this.rawValue = rawValue;
                this.filename = filename;
                break;
            }
            default: {
                super.service(request, everError);
                break;
            }
        }
    }
}

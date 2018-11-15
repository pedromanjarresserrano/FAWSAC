package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Fileupload;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class FileUpload extends Fileupload {
    private transient @Getter
    FileEntity value;
    public @Getter
    List<FileEntity> values = new ArrayList<>();
    private Media photo;
    private Media[] media;

    public FileUpload(String label, Boolean multiple, int count) {
        super(label);
        if (multiple) {
            addEventListener(Events.ON_CLICK, e -> {
                media = get(count);
            });
        }
        onUpload();
    }

    public FileUpload(String label, String image, Boolean multiple, int count) {
        super(label, image);
        if (multiple) {
            addEventListener(Events.ON_CLICK, e -> {
                media = get(count);
            });
        }
        onUpload();
    }

    public FileUpload(String label, String image, Boolean multiple) {
        super(label, image);
        if (multiple) {
            addEventListener(Events.ON_CLICK, e -> {
                media = get(10);

            });
        }
        onUpload();
    }

    public FileUpload(String label, String image) {
        super(label, image);
        onUpload();
    }


    private void onUpload() {
        this.addEventListener("onUpload", x -> {
            UploadEvent upEvent = (UploadEvent) x;
            if (upEvent != null) {
                if (media != null && media.length > 1) {
                    for (Media m : media) {
                        values.add(ApplicationContextUtils.getBean(StorageService.class).saveFile(m.getStreamData()));
                    }
                }
                photo = upEvent.getMedia();
                String name = photo.getName();
                this.setLabel(name);
                this.setValue(ApplicationContextUtils.getBean(StorageService.class).saveFile(photo.getStreamData()));

            }
        });
    }

    public FileUpload(String label) {
        super(label);
        onUpload();
    }

    /**
     * @param value the value to set
     */
    public void setValue(FileEntity value) {
        this.value = value;
        String filename = value.getFilename();
        setLabel(filename != null ? filename : "File");
    }

}

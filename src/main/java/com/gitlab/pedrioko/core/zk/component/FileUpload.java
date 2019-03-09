package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.FileEntity;
import com.gitlab.pedrioko.core.view.api.OnEvent;
import com.gitlab.pedrioko.core.view.util.ApplicationContextUtils;
import com.gitlab.pedrioko.services.StorageService;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Fileupload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class FileUpload extends Fileupload {
    private static final StorageService storageService = ApplicationContextUtils.getBean(StorageService.class);
    private transient @Getter
    FileEntity value;
    public @Getter
    List<FileEntity> values = new ArrayList<>();
    private Media photo;
    private Media[] media;
    private EventListener<UploadEvent> eventEventListener = x -> {
        UploadEvent upEvent = (UploadEvent) x;
        if (upEvent != null) {
            if (media != null && media.length > 1) {
                for (Media m : media) {
                    values.add(storageService.saveFileToFileEntity(m.getName(), m.getStreamData()));
                }
            }
            photo = upEvent.getMedia();
            String name = photo.getName();
            setLabel(name);
            setValue(storageService.saveFileToFileEntity(photo.getName(), photo.getStreamData()));

        }
    };

    public FileUpload(String label, Boolean multiple, int count) {
        super(label);
        if (multiple) {
            addEventListener(Events.ON_CLICK, e -> {
                media = get(count, eventEventListener);
            });
        }
    }

    public FileUpload(String label, String image, Boolean multiple, int count) {
        super(label, image);
        if (multiple) {
            addEventListener(Events.ON_CLICK, e -> {
                media = get(count, eventEventListener);
            });
        }
    }

    public FileUpload(String label, String image, Boolean multiple) {
        super(label, image);
        if (multiple) {
            addEventListener(Events.ON_CLICK, e -> {
                media = get(10, eventEventListener);

            });
        }
    }

    public FileUpload(String label, String image) {
        super(label, image);
        onUpload();
    }

    public static void get(Integer i, List<Media> media) {
        get(i, upEvent -> {
            if (upEvent != null) {
                Media[] medias = upEvent.getMedias();
                if (medias != null && medias.length > 1) {
                    media.addAll(Arrays.asList(medias));
                }
            }
        });
    }

    public static void get(int i, List<FileEntity> media, OnEvent event) {
        get(i, upEvent -> {
            if (upEvent != null) {
                Media[] medias = upEvent.getMedias();
                if (medias != null && medias.length > 0) {
                    Arrays.asList(medias).forEach(e -> {
                        if (e.getName().endsWith("zip")) {
                            media.addAll(storageService.saveZipFileToFileEntity(e.getName(), e.getStreamData()));

                        } else {
                            media.add(storageService.saveFileToFileEntity(e.getName(), e.getStreamData()));
                        }
                    });
                }
                if (event != null) event.doSomething();
            }
        });
    }

    public static void get(int i, List<FileEntity> media) {
        get(i, media, null);
    }

    private void onUpload() {

        addEventListener("onUpload", eventEventListener);
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
        Events.postEvent(Events.ON_CHANGE, this, null);

    }

}

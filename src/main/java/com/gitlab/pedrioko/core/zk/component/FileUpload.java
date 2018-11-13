package com.gitlab.pedrioko.core.zk.component;

import com.gitlab.pedrioko.core.lang.FileEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.zkoss.zul.Fileupload;

@EqualsAndHashCode(callSuper = false)
@SuppressWarnings("serial")
public class FileUpload extends Fileupload {
    private transient @Getter
    FileEntity value;

    public FileUpload(String label, String image) {
        super(label, image);
    }

    public FileUpload(String label) {
        super(label);
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

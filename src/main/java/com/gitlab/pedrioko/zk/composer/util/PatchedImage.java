package com.gitlab.pedrioko.zk.composer.util;

import org.zkoss.zul.Image;

import java.awt.image.RenderedImage;

/**
 * The Class PatchedImage.
 */
public class PatchedImage extends Image {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2011195853382676053L;

    /*
     * (non-Javadoc)
     *
     * @see org.zkoss.zul.Image#setContent(java.awt.image.RenderedImage)
     */
    @Override
    public void setContent(RenderedImage image) {
        if (image == null) {
            super.setContent((org.zkoss.image.Image) null);
        } else {
            super.setContent(image);
        }
    }
}
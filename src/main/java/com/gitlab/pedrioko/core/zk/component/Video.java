package com.gitlab.pedrioko.core.zk.component;

import lombok.Data;
import org.zkoss.zk.au.out.AuInvoke;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.sys.ContentRenderer;

import java.io.IOException;

public @Data
class Video extends HtmlBasedComponent {
    private String src = "";
    private boolean controls = false;
    private boolean muted = false;
    private boolean autoplay = false;
    private boolean loop = false;
    private String preload = "";
    private String poster = "";
    private String crossorigin = "";
    private double playbackRate = 1.0D;


    public Video() {
    }

    public Video(String source) {
        this.src = source;
    }

    @Override
    protected void renderProperties(ContentRenderer renderer) throws IOException {
        super.renderProperties(renderer);
        render(renderer, "src", src);
        render(renderer, "controls", controls);
        render(renderer, "muted", muted);
        render(renderer, "autoplay", autoplay);
        render(renderer, "loop", loop);
        render(renderer, "preload", preload);
        render(renderer, "poster", poster);
        render(renderer, "crossorigin", crossorigin);
        if (this.playbackRate != 1.0D) {
            this.render(renderer, "playbackRate", this.playbackRate);
        }}

    public void setSrc(String src) {
        this.src = src;
        smartUpdate("src", src);

    }

    public void setMute(boolean muted) {
        this.response((String)null, new AuInvoke(this, "setMuted", muted));
    }

    public void setPlaying(boolean playing) {
        this.response((String) null, new AuInvoke(this, "setPlaying", playing));
    }

    public void setCrossorigin(String crossorigin) {
        this.crossorigin = "use-credentials".equalsIgnoreCase(crossorigin) ? "use-credentials" : "anonymous";
        if (!this.crossorigin.equals(crossorigin)) {
            this.crossorigin = crossorigin;
            this.smartUpdate("crossorigin", (Object) this.crossorigin);
        }
    }

    public void setPreload(String preload) {
        preload = "none".equalsIgnoreCase(preload) ? "none" : ("metadata".equalsIgnoreCase(preload) ? "metadata" : "auto");
        if (!preload.equals(this.preload)) {
            this.preload = preload;
            this.smartUpdate("preload", (Object) this.preload);
        }
    }

    public void setPoster(String poster) {
        if (poster != null && !poster.equals(this.poster)) {
            this.poster = poster;
            this.smartUpdate("poster", (Object) this.poster);
        }
    }

    public void setAutoplay(boolean autoplay) {
        if (this.autoplay != autoplay) {
            this.autoplay = autoplay;
            this.smartUpdate("autoplay", this.autoplay);
        }
    }

    public void setControls(boolean controls) {
        if (this.controls != controls) {
            this.controls = controls;
            this.smartUpdate("controls", this.controls);
        }
    }

    public void setLoop(boolean loop) {
        if (this.loop != loop) {
            this.loop = loop;
            this.smartUpdate("loop", this.loop);
        }
    }

    public void setMuted(boolean muted) {
        if (this.muted != muted) {
            this.muted = muted;
            this.smartUpdate("muted", this.muted);
        }
    }

    public void setPlaybackRate(double playbackRate) {
        if (this.playbackRate != playbackRate) {
            this.playbackRate = playbackRate;
            this.smartUpdate("playbackRate", this.playbackRate);
        }
    }
}

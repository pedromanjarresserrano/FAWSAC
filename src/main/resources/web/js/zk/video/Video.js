zk.video.Video = zk.$extends(zk.Widget, {

    _src: '',
    _muted: false,
    _autoplay: false,
    _controls: false,
    _loop: false,
    _preload: '',
    _poster: '',
    _crossorigin: '',
    getSrc: function () {
        return this._src;
    },
    setSrc: function (src) {
        if (this._src != src) {
            this._src = src;
        }
    },
    getPreload: function () {
        return this._preload;
    },
    setPreload: function (src) {
        if (this._preload != src) {
            this._preload = src;
        }
    },
    getPoster: function () {
        return this._poster;
    },
    setPoster: function (src) {
        if (this._poster != src) {
            this._poster = src;
        }
    },
    getCrossorigin: function () {
        return this._crossorigin;
    },
    setCrossorigin: function (src) {
        if (this._crossorigin != src) {
            this._crossorigin = src;
        }
    },
    getMuted: function () {
        return this._muted;
    },
    setMuted: function (src) {
        if (this._muted != src) {
            this._muted = src;
        }
    },
    getAutoplay: function () {
        return this._autoplay;
    },
    setAutoplay: function (src) {
        if (this._autoplay != src) {
            this._autoplay = src;
        }
    },
    getControls: function () {
        return this._controls;
    },
    setControls: function (src) {
        if (this._controls != src) {
            this._controls = src;
        }
    },
    getLoop: function () {
        return this._loop;
    },
    setLoop: function (src) {
        if (this._loop != src) {
            this._loop = src;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var ctx = document.getElementById(this.uuid + '-video');
        if (this._src) {
            ctx.src = this._src;
            ctx.classList.add("pagination-centered");
            ctx.classList.add("text-center");
            ctx.classList.add("col-md-12");
            ctx.classList.add("col-lg-12");
            ctx.classList.add("col-xs-12");
        }
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    _videoDomAttrs: function () {
        var a = "";
        this._muted && (a += " muted");
        this._autoplay && (a += " autoplay");
        this._controls && (a += " controls");
        this._loop && (a += " loop");
        this._playsinline && (a += " playsinline");
        void 0 !== this._preload && (a += ' preload="' +
            this._preload + '"');
        this._poster && (a += ' poster="' + this._poster + '"');
        this._crossorigin && (a += " crossorigin=" + this._crossorigin + '"');
        return a
    },
});
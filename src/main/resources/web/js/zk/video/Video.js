zk.video.Video = zk.$extends(zk.Widget, {

    _src: '',
    _muted: false,
    _autoplay: false,
    _controls: false,
    _loop: false,
    _preload: '',
    _poster: '',
    _crossorigin: '',
    _playbackRate: 1,
    _currentTime: '',
    getSrc: function () {
        return this._src;
    },
    setSrc: function (value) {
        if (this._src != value) {
            this._src = value;
        }
    },
    getCurrentTime: function () {
        return this._currentTime;
    },
    setCurrentTime: function (value) {
        if (this._currentTime != value) {
            this._currentTime = value;
        }
    },
    getPreload: function () {
        return this._preload;
    },
    setPreload: function (value) {
        if (this._preload != value) {
            this._preload = value;
        }
    },
    getPoster: function () {
        return this._poster;
    },
    getPlaybackRate: function (a) {
        return this._playbackRate;
    },
    setPlaybackRate: function (a) {
        try {
            var ctx = document.getElementById(this.uuid + '-video');
            if (ctx) {
                ctx.playbackRate = value;
            }
        } catch (b) {
            zk.error(b.message || b)
        }
    },
    setPoster: function (value) {
        if (this._poster != value) {
            this._poster = value;
        }
    },
    getCrossorigin: function () {
        return this._crossorigin;
    },
    setCrossorigin: function (value) {
        if (this._crossorigin != value) {
            this._crossorigin = value;
        }
    },
    getMuted: function () {
        return this._muted;
    },
    setMuted: function (value) {
        if (this._muted != value) {
            this._muted = value;
        }
    },
    getAutoplay: function () {
        return this._autoplay;
    },
    setAutoplay: function (value) {
        if (this._autoplay != value) {
            this._autoplay = value;
        }
    },
    getControls: function () {
        return this._controls;
    },
    setControls: function (value) {
        if (this._controls != value) {
            this._controls = value;
        }
    },
    getLoop: function () {
        return this._loop;
    },
    setLoop: function (value) {
        if (this._loop != value) {
            this._loop = value;
        }
    },
    setPlaying: function (value) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.currentTime = value;
        }
    },
    setVolume: function (a) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.volume = value;
        }
    },
    setMuted: function (a) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.muted = value;
        }
    },
    setCurrentTime: function (value) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.currentTime = parseFloat(value);
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = this.uuid + '-video';
        var ctx = document.getElementById(elementId);
        if (this._src) {
            ctx.src = this._src;
            ctx.classList.add("pagination-centered");
            ctx.classList.add("text-center");
            ctx.classList.add("col-md-12");
            ctx.classList.add("col-lg-12");
            ctx.classList.add("col-xs-12");
            ctx.style.height = this._height;
            this.setPlaybackRate(this._playbackRate);
            ctx.onplay = function () {
                var currentTime = this.currentTime;
                zAu.send(new zk.Event(zk.Widget.$('#' + this.uuid + '-video'), 'setPlaying', {playing: 'true', currentTime: currentTime}, {toServer: true}));
            };
            ctx.onpause = function () {
                var currentTime = this.currentTime;
                zAu.send(new zk.Event(zk.Widget.$('#' + this.uuid + '-video'), 'setPlaying', {playing: 'false', currentTime: currentTime}, {toServer: true}));
            };
        }
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    _videoDomAttrs: function () {
        var a = '';
        this._muted && (a += ' muted');
        this._autoplay && (a += ' autoplay');
        this._controls && (a += ' controls');
        this._loop && (a += ' loop');
        this._playsinline && (a += ' playsinline');
        void 0 !== this._preload && (a += ' preload=' +
            this._preload);
        this._poster && (a += ' poster=' + this._poster);
        this._crossorigin && (a += ' crossorigin=' + this._crossorigin);
        this._style && (a += ' style=' + this._style);
        this._height && (a += ' height=' + this._height);
        return a
    },
});
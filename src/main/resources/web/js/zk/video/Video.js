zk.video.Video = zk.$extends(zk.Widget, {

    _src: '',
    _muted: false,
    _autoplay: false,
    _controls: false,
    _loop: false,
    _preload: 'none',
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
    getPlaybackRate: function () {
        return this._playbackRate;
    },
    setPlaybackRate: function (value) {
        try {
            var ctx = $('#'+this.uuid + '-video');
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
        var ctx = $('#'+this.uuid + '-video');
        if (ctx) {
       //     ctx.currentTime = value;
        }
    },
    setVolume: function (value) {
        var ctx = $('#'+this.uuid + '-video');
        if (ctx) {
            ctx.volume = value;
        }
    },
    setMuted: function (value) {
        var ctx = $('#'+this.uuid + '-video');
        if (ctx) {
            ctx.muted = value;
        }
    },
    setCurrentTime: function (value) {
        var ctx = $('#'+this.uuid + '-video');
        if (ctx) {
            ctx.currentTime = parseFloat(value);
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = '#'+this.uuid + '-video';
        var ctx = $('#'+this.uuid + '-video');

            ctx.attr('src', this._src);
            this.setPlaybackRate(this._playbackRate);
            ctx.on('play', function () {
                var currentTime = this.currentTime;
                var n = '#' + this.id + '-video';
                var widget = zk.Widget.$(n);
                zAu.send(new zk.Event(widget, 'setPlaying', {playing: 'true', currentTime: currentTime}, {toServer: true}));
            });
            ctx.on('pause', function () {
                var currentTime = this.currentTime;
                var n = '#' + this.id + '-video';
                var widget = zk.Widget.$(n);
                zAu.send(new zk.Event(widget, 'setPlaying', {playing: 'false', currentTime: currentTime}, {toServer: true}));
            });

    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domAttrs_: function () {
        var a = '';
        if(this._muted)
            a += ' muted ';
        if(this._autoplay )
            a += ' autoplay ';
        if(this._controls)
            a += ' controls ';
        if(this._loop)
            a += ' loop ';
        if(this._playsinline)
            a += ' playsinline ';
        if(this._preload)
            a += ' preload=\"' + this._preload + '\" ';
        if(this._poster)
            a+=' poster=\"' + this._poster + '\" ';
        if(this._crossorigin)
            a += ' crossorigin=\"' + this._crossorigin + '\" ';
        if(this._style)
            a += ' style=\"' + this._style + '\" ';
        if(this._height)
            a += ' height=\"' + this._height + '\" ';

        a += ' class=\"' + this._sclass + '\" '
        return a
    }
});
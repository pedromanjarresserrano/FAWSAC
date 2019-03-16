zk.carousel.Carousel = zk.$extends(zk.Widget, {

    _carouselItems: '',
    _carouselItemsJson: '',
    _slideBy: 1,
    _controls: false,
    _lazyload: true,
    getCarouselItems: function () {
        return this._carouselItems;
    },
    setCarouselItems: function (value) {
        if (this._carouselItems != value) {
            this._carouselItems = value;
        }
    },
    getLazyload: function () {
        return this._carouselItems;
    },
    setLazyload: function (value) {
        if (this._carouselItems != value) {
            this._carouselItems = value;
        }
    },
    getSlideBy: function () {
        return this._slideBy;
    },
    setSlideBy: function (value) {
        if (this._slideBy != value) {
            this._slideBy = value;
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
    getCarouselItemsJson: function () {
        return this._carouselItemsJson;
    },
    setCarouselItemsJson: function (value) {
        if (this._carouselItemsJson != value) {
            this._carouselItemsJson = value;
            this.reload();
        }
    },
    reload: function () {
        console.log(this.uuid+ '-carousel');
        var ctx = $(this.uuid + '-carousel');
            if (typeof ctx.get(0) !== "undefined" ) {
                ctx.empty();
                ctx.add(this.domCarouselContent_());
                console.log(ctx);
                console.log(ctx.get(0));
                var slider = tns({
                    container: ctx.get(),
                    items: 1,
                    slideBy: this.getSlideBy(),
                    /*1*/
                    nav: true,
                    navPosition: "bottom",
                    lazyload: this.getLazyload(),
                    controls: this.getControls(),
                    /*false*/
                    autoplay: true,
                    autoplayTimeout: 1000,
                    autoplayButton: false,
                    autoplayButtonOutput: false,
                    autoplayText: [
                        "▶",
                        "❚❚"
                    ]
                });
                slider.pause();
                ctx.addEventListener('mouseenter', function (ev) {
                    slider.play();
                });

                ctx.addEventListener('mouseleave', function (ev) {
                    slider.goTo('first');
                    slider.pause();
                });
            }

    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var ctx = document.getElementById(this.uuid + '-carousel');

    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domCarouselContent_: function () {
        var loadingImage = "data:image/gif;base64,R0lGODlhLQAtAPMPANTU1O3t7fJoRfv7++9OJdvb2+Lf3/SBZPixn/7v6/m+r/b29ubm5u0wAM3Nzf///yH/C05FVFNDQVBFMi4wAwEAAAAh+QQFCgAPACwAAAAALQAtAAAE//DJSesIBjg3WxMHklRkaUpBsa2c5L0fcs5VoLFrB7+ETJsDFY6l270Eox8lMBwWjS+fktnEPaEehVJiqBJd2NdhOul6ARNCuDFGnZiG8tAQGFQSioOx/egGSgsrcVwrDHYzCXoefGYOCyRCG4N9AI9bBgSMLAU1c1s0jSt/Ezc4k58VoStoKFWsqBWlOKOROJawFIFNnANVDLglDFUXw8AkvU0YTafGcnOyos0kVDjQK4fSE8heLK/ZpE3f4uPk5RVN3uLWXuXb1cnk1N2qkuT0DnTF3+4sdb7iwprYqcUCmzF+Kzg9kNct2zoHox6sY4brnjeG+MTRiyih1qQMBltpDADwcRMJXRkJbTAkMmDKPituLXmpiiTHCcpMybm5xJkrcF4m8Sxxz4oEbvW2YAx3FCnET0uNPnA6dMYCglK5FZCJykaVCa6qdsUKFkcBscAuZNhQ1mbIGREAACH5BAUKAA8ALBgAAAAVABUAAARg0Lliwng46y37DFuIeR4AihlJFheqqmf4wuLsGShgOzimhIOAQdV7HBoI1IDRKR4bjQTqsQA4oVDBdPPEIreYrpcAfhC83t/WgMZqwWLvotyGJuH1Q1lRf28TdQ1lZnURACH5BAUKAA8ALCIABwALAB8AAARe8EkZppXG1fuyc8PlfYU1fhqGroAErGu1wGj5MPQXPnna5QZKzjboTV40jnLJ5BAa0GhDkpBKJQorVCA5aBuHR/WLeHi/Cca3wX1+FeYvYXKWlulS7qWeUHrvSnAWEQAh+QQFCgAPACwYABgAFQAVAAAEZ/DJSSdwOLvK39BaVwUgVoiUUToGKn1r4D7M6gzuYp/uFc+qEmAmCWpkHQPhMDE6eJXEoUFlFjO4SUIxpXqtDxVSQvCav5Ox5MxugCtttqITNyNE9YYggYoT7i5sAnNEVAIHCHxEEhEAIfkEBQoADwAsBwAiAB8ACwAABFrwSXmImTjPBa6mTXh82cA4qJcdYdscSlIGBmo7KujuxATcQNyEtStOgsGLosg8IoGBB4K5cz5RUUlCQA1ZkYWBBkGgfm+ALEnBrUqCBTVpkkAc2s6CISD+RAAAIfkEBQoADwAsAAAYABUAFQAABF+wydnIuzhjSpP+j8BJCqgdY3OYGZI2Hvsk7yqHr3Err3UTKZvMxRHeRBOhAbBg0SRKh5TBckWlWENg8CldDNgwFmACi8+gwHltUq/DrEHhLb0FAO/bJWCG6y8DfHMOEQAh+QQFCgAPACwAAAcACwAfAAAEYPBJqaaVqJ0rU/vbJXyglZGlRKDkprAk8YxwY3j1dsLSkUs0VuVRayQkNRlnyWxeHNCoAzCRSgeSghUakBi2DoMkAHZgB2VGtrx4kMHUBwAsdm/r6yhegobuJ2R/Fl0WEQAh+QQFCgAPACwAAAAAFQAVAAAEYvDJSWtCR7RWu1TaJnoUQogoRyZhOnqI63qKPHuHjVbBlOsESsBh8LkOigRl4GgWJb/GgVRoOn2EZ2dovZIogK5VS+KKHYCvpHp2LNTMNkP9MIvpD0ObTG336G0OA3htaXgRADs=";
        var obj = JSON.parse(this.getCarouselItemsJson());
        for (var b = obj.length, c = "", d = 0; d < b; d++) {
            c += '<span><img class="' + ((this.getLazyload() == "false") ? 'tns-image' : 'tns-lazy-img') + ' img-responsive " style="height: ' + obj[d].enlargedHeight + ';"' + ((this.getLazyload() == "false") ? 'src={' + obj[d].enlargedSrc + '}  ' : 'src="' + loadingImage + '"') + ((this.getLazyload() == "false") ? '' : 'data-src=' + obj[d].enlargedSrc) + '></span>';
        }
        return c;

    },
    redraw:function (out) {
        out.push("<div id='", this.uuid, "-carousel'"," >" , this.domCarouselContent_(),"</div>");
    }
});
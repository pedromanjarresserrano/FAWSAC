zk.carousel.Carousel = zk.$extends(zk.Widget, {

    _carouselItems: '',
    _carouselItemsJson: '',
    _lazyload: '',
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
    getCarouselItemsJson: function () {
        return this._carouselItemsJson;
    },
    setCarouselItemsJson: function (value) {
        if (this._carouselItemsJson != value) {
            this._carouselItemsJson = value;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var ctx = document.getElementById(this.uuid + '-carousel');
        if (this.getCarouselItems()) {
            var slider = tns({
                container: '#' + this.uuid + '-carousel',
                items: 1,
                slideBy: 1,
                nav: true,
                navPosition: "bottom",
                lazyload: this.getLazyload(),
                controls: false,
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
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domCarouselContent_: function () {
        var loadingImage = "data:image/gif;base64,\R0lGODlhAQABAPAAAMzMzAAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==";


        var obj = JSON.parse(this.getCarouselItemsJson());
        for (var b = obj.length, c = "", d = 0; d < b; d++) {
            c += '<span><img class="' + ((this.getLazyload() == "false") ? 'tns-image' : 'tns-lazy-img') + ' img-responsive " style="height: ' + obj[d].enlargedHeight + ';" src={' + ((this.getLazyload() == "false") ? obj[d].enlargedSrc : loadingImage) + '}  ' + ((this.getLazyload() == "false") ?  '':'data-src=' + obj[d].enlargedSrc  ) + '></span>';
        }
        return c;

    },
});
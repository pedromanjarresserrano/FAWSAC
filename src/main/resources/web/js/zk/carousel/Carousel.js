zk.carousel.Carousel = zk.$extends(zk.Widget, {

    _carouselItems: '',
    _carouselItemsJson: '',
    getCarouselItems: function () {
        return this._carouselItems;
    },
    setCarouselItems: function (src) {
        if (this._carouselItems != src) {
            this._carouselItems = src;
        }
    },
    getCarouselItemsJson: function () {
        return this._carouselItemsJson;
    },
    setCarouselItemsJson: function (src) {
        if (this._carouselItemsJson != src) {
            this._carouselItemsJson = src;
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
        var obj = JSON.parse(this.getCarouselItemsJson());
        for (var b = obj.length, c = "", d = 0; d < b; d++)
            c += '<img class="img-responsive" src="' + obj[d].enlargedSrc + '">';
        return c;

    },
});
zk.simplecarousel.SimpleCarousel = zk.$extends(zk.Widget, {
    _value: '',
    getValue: function () {
        return this._value;
    },
    setValue: function (value) {
        if (this._value != value) {
            this._value = value;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = '#'+this.uuid + '-simple-carousel';
        var lazyLoadInstance = new LazyLoad({
            elements_selector: ".lazy-img"
        });
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domContent_: function () {
        var c = ''
        if(this._value){
            var list = JSON.parse(this._value);
            for (var i = 0; i <list.length ; i++){
                var test = list[i];
                c += '<span class="img-span"><img class="lazy-img" data-src="' + test + '"> </span>';
            }
        }
        return c
    }
});
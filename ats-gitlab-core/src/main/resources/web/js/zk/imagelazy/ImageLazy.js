zk.imagelazy.ImageLazy = zk.$extends(zk.Widget, {
    _datasrc: '',
    _src:'',
    getDatasrc: function () {
        return this._datasrc;
    },
    setDatasrc: function (value) {
        if (this._datasrc != value) {
            this._datasrc = value;
        }
    },
    getSrc: function () {
        return this._src;
    },
    setSrc: function (value) {
        if (this._src != value) {
            this._src = value;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = '#'+this.uuid + '-lazy-image';
        var element = $(elementId);
        element.attr("data-src", this._datasrc);
        element.attr("src", this._src);
        if(window.LazyLoadInstance === undefined){
            window.LazyLoadInstance = {};
            window.LazyLoadInstance = new LazyLoad({
               elements_selector: '.lazy-image'
            });
        }else{
            window.LazyLoadInstance.update();
        }
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    }
});
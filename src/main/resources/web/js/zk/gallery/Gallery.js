zk.gallery.Gallery = zk.$extends(zk.Widget, {

    _galleryType: '',
    _galleryItems: '',
    galleryItemsJson: '',
    getGalleryType: function () {
        return this._galleryType;
    },
    setGalleryType: function (src) {
        if (this._galleryType != src) {
            this._galleryType = src;
        }
    },
    getGalleryItems: function () {
        return this._galleryItems;
    },
    setGalleryItems: function (src) {
        if (this._galleryItems != src) {
            this._galleryItems = src;
        }
    },
    getGalleryItemsJson: function () {
        return this.galleryItemsJson;
    },
    setGalleryItemsJson: function (src) {
        if (this.galleryItemsJson != src) {
            this.galleryItemsJson = src;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var ctx = document.getElementById(this.uuid + '-gallery');
        if (this._galleryItems) {
            ctx.classList.add("galleria");
            var galleryItemsJson = this.getGalleryItemsJson();
            ctx.style.height =this.getHeight();
            Galleria.loadTheme('https://cdnjs.cloudflare.com/ajax/libs/galleria/1.5.7/themes/classic/galleria.classic.min.js');
            Galleria.run('.galleria');
        }
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domContent_: function () {
        var obj = JSON.parse(this.getGalleryItemsJson());
        for (var b = obj.length, c = "", d = 0; d < b; d++)
            c += '<img  src="' + obj[d].enlargedSrc + '">';
        return c
    },
});
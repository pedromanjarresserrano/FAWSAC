zk.imageupload.ImageUpload = zk.$extends(zk.Widget, {

    _rawvalue: '',
    _filename: '',
    getFilename: function () {
        return this._rawvalue;
    },
    setFilename: function (_filename) {
        if (this._filename != _filename) {
            this._filename = _filename;
            var span = $('#' + this.uuid + '-imageupload-span');
            span.ready().html(this._filename);
        }
    },
    getRawValue: function () {
        return this._rawvalue;
    },
    setRawValue: function (_rawvalue) {
        if (this._rawvalue != _rawvalue) {
            this._rawvalue = _rawvalue;
            var img = $('#' + this.uuid + '-imageupload-img');
            img.ready().attr('src', _rawvalue);
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = '#'+this.uuid ;
        var element = $(elementId + '-imageupload-file');
        element.change(function (e) {
            event.preventDefault();
            var filename = e.target.files[0].name
            $(elementId + '-imageupload-span').html(filename);
            if (e.target.files && e.target.files[0]) {
              var reader = new FileReader();
              reader.onload = function (e) {
                $(elementId + '-imageupload-img').attr('src', e.target.result);
                zk.Widget.$(elementId).fire("onChange",{ filename: filename ,value: e.target.result }, { toServer: !0 }, 90);
                // zAu.send(new zk.Event(zk.Widget.$(elementId), 'updateValue',{ filename: filename ,value: e.target.result }, {toServer: true}));
              }
              reader.readAsDataURL(e.target.files[0]);
            }
        });

        var img = $('#' + this.uuid + '-imageupload-img');
        img.ready().attr('src', this._rawvalue);
        var span = $('#' + this.uuid + '-imageupload-span');
        span.ready().html(this._filename);

    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    }
});
zk.colorpicker.ColorPicker = zk.$extends(zk.Widget, {

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
        var elementId = '#'+this.uuid + '-colorpicker';
        $(elementId).on("change", function (event) {
           event.preventDefault();
           zAu.send(new zk.Event(zk.Widget.$(elementId), 'updateValue',{ value: $(this).val() }, {toServer: true}));
        });
        jscolor.installByClassName("jscolor");
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    }
});
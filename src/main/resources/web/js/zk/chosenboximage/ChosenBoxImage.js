zk.chosenboximage.ChosenBoxImage = zk.$extends(zk.Widget, {
    _value: '',
    _model: '',
    getModel: function () {
        return this._model;
    },
    setModel: function (model) {
        if (this._model != model) {
            this._model = model;
        }
    },
    getValue: function () {
        return this._value;
    },
    setValue: function (value) {
        if (this._value != value) {
            this._value = value;
            var elementId = '#'+this.uuid + '-chosenbox-image';
            var array = JSON.parse(value);
            $(elementId).val(array).trigger("chosen:updated");
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = '#'+this.uuid + '-chosenbox-image';
        $(elementId).chosen({
            width: "100%"
        }).val("").change(
                  (event) => {
                     zAu.send(new zk.Event(zk.Widget.$(elementId), 'updateValue',{ value: $(event.target).val() }, {toServer: true}));

                    }
                  );;
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domContent_: function () {
        var c = ''
        if(this._model){
            var list = JSON.parse(this._model);
            for (var i = 0; i <list.length ; i++){
                var test = list[i];
                c += '<option value="'+ test["key"] +'" data-img-src="' + test["image"] + '"> ' +  test["label"]  + '</option>';
            }
        }
        return c
    }
});
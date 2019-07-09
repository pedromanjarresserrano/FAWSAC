zk.slider.Slider = zk.$extends(zk.Widget, {

    _value: null,
    getValue: function () {
        return this._value;
    },
    setValue: function (value) {
        if (this._value != value) {
            this._value = value;
        }
    },
    bind_: function () {
              var slider = $("#"+this.uuid+"-slider");

              var children = slider.find(".slides-wrapper");
              var added  = this.domContent_();
              var aux = children.children()                                ;
              if(added.length > 0 && aux.length == 0)
                children.append(added);
              slider.sliderResponsive({
                ratio: "keep",
                autoplay: "on",
                pause: 4000,
                onClick: function (slide) {
                  event.preventDefault();
                  zAu.send(new zk.Event(zk.Widget.$(elementId), 'updateValue',{ value: $(this).val() }, {toServer: true}));
                }
              });
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domContent_: function () {
        var c = ''
        if(this._value){
            for (var property in this._value)
                c += '<li> <span> <img  src="' + property + '"> </span> </li>';
        }
        return c
    }
});
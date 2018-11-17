zk.progresstask.ProgressTask = zk.$extends(zk.Widget, {

    _time: '',
    _maxValue: '',
    _type: '',
    _currentValue: '',
    _bar: '',
    _timeout: '',
    getMaxValue: function () {
        return this._maxValue;
    },
    setMaxValue: function (src) {
        if (this._maxValue != src) {
            this._maxValue = src;
        }
    },
    getCurrentValue: function () {
        return this._currentValue;
    },
    setCurrentValue: function (src) {
        if (this._currentValue != src) {
            this._currentValue = src;
        }
    },
    getType: function () {
        return this._type;
    },
    setType: function (src) {
        if (this._type != src) {
            this._type = src;
        }
    },
    getTime: function () {
        return this._time;
    },
    setTime: function (src) {
        if (this._time != src) {
            this._time = src;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);


        var _bar = new ldBar("#"+this.uuid + '-progresstask');
        /*console.log(_bar);
        function doSomething() {
            $(this.uuid + '-progresstask').fire('onAfterSize',{}, {toServer:true})
            _bar.set(this._currentValue);
        }*/

        if (_bar) {
            //this._timeout = setInterval(doSomething, this.getTime());
            _bar.set(this._currentValue);
            // this._bar.style = this.getStyle()
           this._bar.style.width = this.getWidth();
           this._bar.style.height = this.getHeight();
        }
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    domContent_: function () {
        return "";
    },
});
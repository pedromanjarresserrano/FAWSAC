zk.clock.Clock = zk.$extends(zk.Widget, {

    _location: '',
    getLocation: function () {
        return this._src;
    },
    setLocation: function (value) {
        if (this._location != value) {
            this._location = value;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = '#'+this.uuid + '-clock';
         clock( elementId, this._location);

    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    }
});
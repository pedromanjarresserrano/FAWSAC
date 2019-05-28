zk.chart.Chart = zk.$extends(zk.Widget, {

    _type: '',
    _data: '',
    _showLegend: false,
    _responsive: true,
    _maintainAspectRatio: false,
    getType: function () {
        return this._type;
    },
    setType: function (value) {
        if (this._type != value) {
            this._type = value;
        }
    },
    getShowLegend: function () {
        return this._type;
    },
    setShowLegend: function (value) {
        if (this._type != value) {
            this._type = value;
        }
    },
    getData: function () {
        return this._data;
    },
    setData: function (value) {
        if (this._data != value) {
            this._data = value;
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var ctx = document.getElementById(this.uuid + '-chart-js').getContext('2d');
            var myBarChart = new Chart(ctx, {
                type: this._type,
                data: JSON.parse(this._data),
                options: {
                    plugins:{
                        datalabels:{
                            backgroundColor: function(context) {
                                return context.dataset.backgroundColor;
                            },
                            borderRadius: 4,
                            color: 'white',
                            font: {
                                weight: 'bold'
                            },
                            formatter: Math.round,
                            display: this._showLegend
                        }
                    },
                    responsive: this._responsive,
                    maintainAspectRatio: this._maintainAspectRatio,
                    legend:{
                        display: this._showLegend,
                        labels:{
                            fontColor: 'rgb(255, 99, 132)'
                        }
                    }
                }
            });

    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    }
});
toastr.options = {
    "closeButton": false,
    "debug": false,
    "newestOnTop": false,
    "progressBar": false,
    "positionClass": "toast-bottom-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
}
zk.googleAPIkey = 'AIzaSyCzG1oHR2iMJR-9KT4YHVrTQ8Eh4fChrxk';
var stompClient = null;


function connect() {
    var socket = new SockJS('/jsa-stomp-endpoint');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log(socket._transport.url);
        stompClient.send("/login", {}, {});
        stompClient.subscribe('/topic/hi', function (hello) {
            var body = hello.body;
            toastr.warning(body);
            var notify = new Notification(body, {
                icon: 'http://cdn.sstatic.net/stackexchange/img/logos/so/so-icon.png',
                body: "Hey, " + body,
            });
        });
        stompClient.subscribe('/user/topic/hi', function (hello) {
            var body = hello.body;
            toastr.warning(body);
            var notify = new Notification(body, {
                icon: 'http://cdn.sstatic.net/stackexchange/img/logos/so/so-icon.png',
                body: "Hey, " + body,
            });
        });
    }, function (frame) {
        connect();
    });
}

Notification.requestPermission().then(function (result) {
    console.log(result);
});

function connectAdminTopic() {
    stompClient.subscribe('/topic/hi', function (hello) {
        var body = hello.body;
        toastr.warning(body);
        var notify = new Notification(body, {
            icon: 'http://cdn.sstatic.net/stackexchange/img/logos/so/so-icon.png',
            body: "Hey, " + body,
        });
    });
}

charts = new Map();

function loadChart(id, data) {
    var chart = charts.get(id);
    var ctx = document.getElementById(id).getContext('2d');
    if (chart == null) {
        var myChart = new Chart(ctx, data);
        ctx.fillStyle = "#FFF";
        charts.set(id, myChart);
    } else {
       /* chart.data.labels.pop();
        chart.data.datasets.forEach((dataset) => {
            dataset.data.pop();
        });
        chart.update();
       /* chart.data.labels.push(data.data.labels);
            chart.data.datasets.forEach((dataset) => {
                dataset.data.push(data.data.datasets.data);
        });*/
        chart = new Chart(ctx, data);
    }

}

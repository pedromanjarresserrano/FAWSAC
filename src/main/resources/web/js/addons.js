zk.googleAPIkey = 'AIzaSyCzG1oHR2iMJR-9KT4YHVrTQ8Eh4fChrxk';

charts = new Map();

function loadChart(id, data) {
    var chart = charts.get(id);
    var ctx = document.getElementById(id).getContext('2d');
    if (chart == null) {
        var myChart = new Chart(ctx, data);
        ctx.fillStyle = "#FFF";
        charts.set(id, myChart);
    } else {
        chart = new Chart(ctx, data);
    }

}

$(document).ready(function () {
    $("body").on("click", ".container-v", function (event) {
        $(".container-v").removeClass("container-v-selected")
        $(this).addClass("container-v-selected")
    })
    $("body").on("click", ".img-container", function (event) {
        $(".img-container").removeClass("img-container-selected")
        $(this).addClass("img-container-selected")
    })
});
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
        function selectItem() {
          var padre = $(".container-v");
          padre.each(function() {
            $(this).on("click", function() {
              padre.each(function() {
                var p = $(this).find(".preview-v")[0];
                p.style.background = "white";
                p.style.color = "black";
                var l = $(this).find(".label-v")[0];
                l.style.background = "white";
                l.style.color = "black";
              });
              var p = $(this).find(".preview-v")[0];
              p.style.background = "black";
              p.style.color = "white";
              var l = $(this).find(".label-v")[0];
              l.style.background = "black";
              l.style.color = "white";
              $(document).add(padre.clone());
            });
          });
        }
      var uuid = "";
      var checkExist = setInterval(function () {
        var list = $(".container-v");
        if (list.length) {
          if (list.get(0).id != uuid) {
            selectItem();
            uuid = list.get(0).id;
          }
        }
      }, 1000);


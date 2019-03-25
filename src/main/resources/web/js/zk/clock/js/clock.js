      function clock(element, location) {
        function clocked() {
          var childerns = $(element).children();
          var hour = childerns.find(".hour-value");
          var minute = childerns.find(".minute-value");
          var second = childerns.find(".seconds-value");
          var ampmo = childerns.find(".ampm-value");
          var date = luxon.DateTime.local().setZone(location);

          var ampm = date.hour < 12 ?
            'AM' :
            'PM';

          var hours = date.hour == 0 ?
            12 :
            date.hour > 12 ?
            date.hour - 12 :
            date.hour;

          var minutes = date.minute < 10 ?
            '0' + date.minute :
            date.minute;

          var seconds = date.second < 10 ?
            '0' + date.second :
            date.second;
          hour[0].innerText = hours;
          minute[0].innerText = minutes;
          second[0].innerText = seconds;
          ampmo[0].innerText = ampm;
        }
        clocked()
        setInterval(clocked, 1000);

      }
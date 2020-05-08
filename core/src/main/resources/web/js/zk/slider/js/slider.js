 (function ($) {
      "use strict";

      $.fn.sliderResponsive = function (settings) {
        var set = $.extend({
            anim: 300,
            pause: null,
            autoplay: null,
            ratio: null,
            ulClass: "slides-wrapper",
            nextClass: "sl-next",
            prevClass: "sl-prev",
            onClick: null,
          },
          settings
        );

        var $slider = $(this),
          //components
          $next = $slider.find("." + set.nextClass + ""),
          $prev = $slider.find("." + set.prevClass + ""),
          $wrapper = $slider.find("." + set.ulClass + ""),
          $items = $wrapper.find(">li"),
          //dimensions
          n = $items.length,
          ratio = $slider.height() / $slider.width(),
          w,
          h;

        //required styles
        $slider.css("overflow", "hidden");
        $wrapper.find(">li:last-child").prependTo($wrapper);
        $wrapper.css({
          height: $slider.height(),
          position: "relative"
        });
        $items.css({
          height: $slider.height(),
          float: "left"
        });


        //dimensions
        function sliderSize() {
          w = $slider.width();
          $items.width(w);
          $wrapper.width(n * w);
          $wrapper.css("left", -w);
          if (set.ratio === "keep") {
            h = Math.round(w * ratio); //keep aspect ratio
            $slider.height(h);
          }
        }
        sliderSize();
        $(window).resize(sliderSize);

        $items.each(function (index) {
          //do stuff
          var item = $($items.get(index));
          item.on("click", function () {
            set.onClick(index);
          })
          var $imgs = item.find("img");
          $imgs.css({
            height: $slider.height()
          });
        });

        //moving slides
        function next() {
          $wrapper.animate({
              "margin-left": "-=" + w
            },
            set.anim,
            function () {
              var $this = $(this);
              $this.find(">li:first-child").appendTo($this);
              $this.css("margin-left", 0);
            }
          );
        } //end slNext

        function prev() {
          $wrapper.animate({
              "margin-left": "+=" + w
            },
            set.anim,
            function () {
              var $this = $(this);
              $this.find(">li:last-child").prependTo($this);
              $this.css("margin-left", 0);
            }
          );
        } //end slPrev

        //autoplay
        if (set.autoplay === "on") {
          setInterval(function () {
            next();
          }, set.pause);
        }

        //slide navigation
        $next.on("click", next);
        $prev.on("click", prev);

        return $slider;
      };
    })(jQuery);
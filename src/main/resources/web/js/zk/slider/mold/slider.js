function (out) {
    out.push('  <div id="' + this.uuid + '-slider" class="slider">'+
                  '<ul class="slides-wrapper">'+
                    this.domContent_()+
                  '</ul>'+
                  '<ul class="slider-nav">'+
                    '<li class="sl-prev">&larr;</li>'+
                    '<li class="sl-next">&rarr;</li>'+
                  '</ul>'+
                '</div>');
}
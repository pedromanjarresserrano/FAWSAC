var myIndex = 0;
carousel();
var timeout;

function carousel() {
    var back = document.getElementById('back');
    var next = document.getElementById('next');

    if (back != null && next != null) {

        back.addEventListener('click', function () {
            plusDivs(-1);
        }, false);

        next.addEventListener('click', function () {
            plusDivs(1);
        }, false);
        var i;
        var x = document.getElementsByClassName("mySlides");
        for (i = x.length - 1; i > -1; i--) {
            x[i].style.display = "none";
        }
        myIndex++;
        if (myIndex > x.length) {
            myIndex = 1
        }
        if (x.length > 0) {
            x[myIndex - 1].style.display = "block";
            slideIndex = myIndex;
        }
    }
    timeout = setTimeout(carousel, 5000); // Change image every 2 seconds
}

function stopCarousel() {
    clearTimeout(timeout);
}

var slideIndex = 1;
showDivs(slideIndex);

function plusDivs(n) {
    showDivs(slideIndex += n);
}

function showDivs(n) {
    var i;
    var x = document.getElementsByClassName("mySlides");
    if (n > x.length) {
        slideIndex = 1
    }
    if (n == 0) {
        slideIndex = x.length
    }
    for (i = x.length - 1; i > -1; i--) {
        x[i].style.display = "none";
    }
    x[slideIndex - 1].style.display = "block";
}
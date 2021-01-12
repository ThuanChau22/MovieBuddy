// Collapse navbar when click away
$(document).ready(function () {
    $(document).click(function (event) {
        var clickover = $(event.target);
        if ($(".navbar-collapse").hasClass("show") && !clickover.hasClass("nav-element")) {
            $(".navbar-toggler").click();
        }
    });
});

// Load previous selected option on reload
function loadSelectedOption(defaultId, selectId, optionValue) {
    if (optionValue != "") {
        $(defaultId).removeAttr("selected");
        $(selectId).val(optionValue);
    }
}

// Submit a form
function submitForm(formId) {
    $(formId).submit();
}

// Load list of dates with JCarousel
function loadDates(selectedDateIndex) {
    let a = $("#dateItem-" + selectedDateIndex).children("a.date-picker-link");
    a.removeClass("date-picker-link");
    a.removeAttr("href");
    a.addClass("selected-link");
    a.children("span.dayOfWeek").addClass("selected-day");

    let range = 1;
    let jcarousel = $(".jcarousel");
    jcarousel
        .on("jcarousel:reload jcarousel:create", function () {
            let carousel = $(this);
            let width = carousel.innerWidth();

            if (width >= 600) {
                range = 7;
            } else if (width >= 450) {
                range = 6;
            } else if (width >= 350) {
                range = 5;
            } else if (width >= 250) {
                range = 4;
            } else {
                range = 3;
            }

            width = width / range;
            carousel.jcarousel("items").css("width", Math.ceil(width) + "px");

            $(".prev")
                .on("jcarouselcontrol:inactive", function () {
                    $(this).addClass("inactive");
                })
                .on("jcarouselcontrol:active", function () {
                    $(this).removeClass("inactive");
                })
                .jcarouselControl({
                    target: "-=" + range
                });

            $(".next")
                .on("jcarouselcontrol:inactive", function () {
                    $(this).addClass("inactive");
                })
                .on("jcarouselcontrol:active", function () {
                    $(this).removeClass("inactive");
                })
                .jcarouselControl({
                    target: "+=" + range
                });

        }).jcarousel({ animation: "slow" }).jcarouselSwipe();

    jcarousel.jcarousel("scroll", selectedDateIndex, true);
}
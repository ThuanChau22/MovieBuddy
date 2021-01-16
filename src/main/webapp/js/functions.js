// Element IDs
const SEARCH_INPUT = "#searchInput";
const SEARCH_SPINNER = "#searchSpinner";
const SEARCH_RESULT_MENU = "#searchResultMenu";
const CLOSE_SEARCH_RESULT = "#closeSearchResult";
const SEARCH_RESULT = "#searchResult";

// Ajax URLs
const FIND_MOVIE = "FindMovie";

$(document).ready(function () {
    $(document).click(function (event) {
        var clickover = $(event.target);

        // Collapse navbar when click away
        if ($(".navbar-collapse").hasClass("show") && !clickover.parents().hasClass("nav-element")) {
            $(".navbar-toggler").click();
        }

        // Collapse search result when click away
        $(SEARCH_RESULT_MENU).removeClass("show");
    });

    // Show search result
    $(SEARCH_INPUT).focus(function () {
        if ($(SEARCH_INPUT).val().trim() != "") {
            $(SEARCH_RESULT_MENU).addClass("show");
        }
    });

    // Close search result
    $(CLOSE_SEARCH_RESULT).click(function () {
        $(SEARCH_RESULT_MENU).removeClass("show");
    });

    // Search on typing
    $(SEARCH_INPUT).keyup(function () {
        searchByTitle();
    });
});

// Load previous selected option on reload
function loadSelectedOption(defaultId, selectId, optionValue) {
    if (optionValue != "") {
        $(defaultId).removeAttr("selected");
        $(selectId).val(optionValue);
    }
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

// Search movie by title
function searchByTitle() {
    let input = $(SEARCH_INPUT).val().trim();

    if (input == "") {
        $(SEARCH_RESULT_MENU).removeClass("show");
        return false;
    }

    $(SEARCH_SPINNER).addClass("spinner-border text-info");
    $.post(
        FIND_MOVIE,
        { title: input },
        function (movies) {
            $(SEARCH_RESULT).empty();
            if (movies.length > 0) {
                movies.forEach(movie => {
                    let a = $("<a></a>");
                    a.addClass("dropdown-item text-wrap");
                    a.attr("href", "#");
                    a.html(replaceWithBold(movie.title, input));
                    $(SEARCH_RESULT).append(a);
                });
            } else {
                let message = $("<h6></h6>");
                message.addClass("dropdown-header font-weight-bold");
                message.text("Movie not found!");
                $(SEARCH_RESULT).append(message);
            }
        }
    ).done(function () {
        $(SEARCH_SPINNER).removeClass("spinner-border text-info");
        if ($(SEARCH_INPUT).val().trim() != "") {
            $(SEARCH_RESULT_MENU).addClass("show");
        }
    });

    return false;
}

// Wrap <b> around k from str
function replaceWithBold(str, k) {
    k = k.toLowerCase();
    let k_new = str.substr(str.toLowerCase().indexOf(k), k.length);
    return str.replace(k_new, "<b>" + k_new + "</b>");
}
// Element IDs
const DATE_ITEM = "#dateItem-";
const SEARCH_INPUT = "#searchInput";
const SEARCH_SPINNER = "#searchSpinner";
const SEARCH_RESULT_MENU = "#searchResultMenu";
const CLOSE_SEARCH_RESULT = "#closeSearchResult";
const SEARCH_RESULT = "#searchResult";
const SHOWTIME_SPINNER = "#showtimeSpinner";
const SHOWTIME_RESULT = "#showtime";

// Ajax URLs
const FIND_MOVIE = "FindMovie";
const SHOWTIME = "showtime";

// Input params
const MOVIE_ID_PARAM = "movie_id";
const DATE_PARAM = "date";
const TITLE_PARAM = "title";

const TIME_FORMAT = { hour12: false, hour: "2-digit", minute: "2-digit" };

// Load previous selected option on reload
function loadSelectedOption(defaultId, selectId, optionValue) {
    if (optionValue != "") {
        $(defaultId).removeAttr("selected");
        $(selectId).val(optionValue);
    }
}

$(document).ready(function () {

    // Click handler
    $(document).click(function (event) {
        let clickover = $(event.target);

        // Collapse navbar when click away
        if ($(".navbar-collapse").hasClass("show") && !clickover.parents().hasClass("nav-element")) {
            $(".navbar-toggler").click();
        }

        // Collapse search result when click away
        collapseSearchResult();

        // Load home
        if (clickover.parents().hasClass("jcarousel home")) {
            event.preventDefault();
            console.log("home");
        }

        // Load showtimes
        if (clickover.parents().hasClass("jcarousel showtime")) {
            event.preventDefault();
            listShowtimes(clickover.parents().filter(".jcarousel-item"));
        }
    });

    $(document).keydown(function (event) {
        // Search shortcut (ctrl + shift + S)
        if (event.ctrlKey && event.shiftKey && event.keyCode == "83") {
            $(SEARCH_INPUT).focus();
        }
    });

    // Search handler
    let previousInput = "";
    $(SEARCH_INPUT).focus(function () {
        // Search input
        $(SEARCH_INPUT).keydown(function (event) {
            // Navigate search result
            if ($(SEARCH_RESULT).children().length > 0) {
                if (event.keyCode == 40) {
                    $(SEARCH_RESULT).children().first().focus();
                } else if (event.keyCode == 38) {
                    $(SEARCH_RESULT).children().last().focus();
                }
            }

            if (event.keyCode == 27) {
                // Close on esc
                $(CLOSE_SEARCH_RESULT).click();
            }

            // Get previous input
            previousInput = $(SEARCH_INPUT).val().trim();
        });
        $(SEARCH_INPUT).keyup(function () {
            // Get current input
            let currentInput = $(SEARCH_INPUT).val().trim();

            // Search when input changes
            if (currentInput != previousInput) {
                searchByTitle();
            } else if (currentInput == "") {
                collapseSearchResult();
            }
        });

        // Show search result
        if ($(SEARCH_INPUT).val().trim() != "" && $(SEARCH_RESULT).children().length > 0) {
            showSearchResult();
        } else {
            searchByTitle();
        }

        // Close search result
        $(CLOSE_SEARCH_RESULT).click(function () {
            $(SEARCH_INPUT).blur();
            collapseSearchResult();
        });
    });

});

// Show search result
function showSearchResult() {
    $(SEARCH_RESULT_MENU).addClass("show");
    $(SEARCH_RESULT_MENU).parent().addClass("show");
}

// Collapse search result
function collapseSearchResult() {
    $(SEARCH_RESULT_MENU).removeClass("show");
    $(SEARCH_RESULT_MENU).parent().removeClass("show");
}

// Load list of dates with JCarousel
function loadDates(selectedDateIndex) {
    // Set selected date
    selectDate(selectedDateIndex);

    // Initialize
    let range = 1;
    let jcarousel = $(".jcarousel");
    jcarousel
        .on("jcarousel:reload jcarousel:create", function () {
            let carousel = $(this);
            let width = carousel.innerWidth();

            // Set range
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

            // Set width
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

    // Set scroll
    jcarousel.jcarousel("scroll", selectedDateIndex, true);
}

// List schedules for movies (home)
function listSchedules(element) {
    // Check if date is currently selected
    if (element.find("a").first().hasClass("selected-link")) {
        return;
    }

    // Remove previous selected date and highlight current date
    let selectedDateIndex = ("#" + element.attr("id")).replace(DATE_ITEM, "");
    let currentIndex;
    element.siblings("li").each(function () {
        if ($(this).find("a").first().hasClass("selected-link")) {
            currentIndex = ("#" + $(this).attr("id")).replace(DATE_ITEM, "");
        }
    });
    selectDate(selectedDateIndex, currentIndex);

    // Send request
    $.post();
}

// List showtimes for a movie (showtime)
function listShowtimes(element) {
    // Check if date is currently selected
    if (element.find("a").first().hasClass("selected-link")) {
        return;
    }

    // Remove previous selected date and highlight current date
    let selectedDateIndex = ("#" + element.attr("id")).replace(DATE_ITEM, "");
    let currentIndex;
    element.siblings("li").each(function () {
        if ($(this).find("a").first().hasClass("selected-link")) {
            currentIndex = ("#" + $(this).attr("id")).replace(DATE_ITEM, "");
        }
    });
    selectDate(selectedDateIndex, currentIndex);

    // Send request
    addSpinner(SHOWTIME_SPINNER, "spinner-showtime");
    $(SHOWTIME_RESULT).empty();
    let link = element.find("a").first().attr("href");
    $.post(SHOWTIME, link.substr(link.indexOf("?") + 1),
        function (schedules) {
            $(SHOWTIME_RESULT).empty();
            if (schedules.length > 0) {
                schedules.forEach(schedule => {
                    // return result as button
                    let a = $("<a></a>");
                    a.addClass("list-button");
                    a.attr("href", "#" + schedule.scheduleId);
                    let button = $("<button></button>");
                    button.attr("type", "button");
                    button.addClass("btn btn-outline-info");
                    let time = new Date();
                    time.setHours(schedule.startTime.hour);
                    time.setMinutes(schedule.startTime.minute);
                    button.text(time.toLocaleTimeString("en-US", TIME_FORMAT));
                    a.append(button);
                    $(SHOWTIME_RESULT).append(a);
                });
            } else {
                // No showtimes
                let div = $("<div></div>");
                div.addClass("text-center");
                div.append("<h5>No showtimes</h5>");
                div.append("<span>Please pick a differrent date!</span>");
                $(SHOWTIME_RESULT).append(div);
            }
        }
    ).done(function () {
        removeSpinner(SHOWTIME_SPINNER);
    });
}

// Set selected date
function selectDate(selectedIndex, currentIndex) {
    // Selected index
    let a = $(DATE_ITEM + selectedIndex).children("a.date-picker-link");
    a.removeClass("date-picker-link");
    a.addClass("selected-link");
    a.children("span.dayOfWeek").addClass("selected-day");

    // Remove current index
    if (currentIndex != undefined) {
        let current = $(DATE_ITEM + currentIndex).children("a.selected-link");
        current.addClass("date-picker-link");
        current.removeClass("selected-link");
        current.children("span.dayOfWeek").removeClass("selected-day");
    }
}

// Search movie by title
function searchByTitle() {
    // Get input
    let titleInput = $(SEARCH_INPUT).val().trim();

    // Empty input
    if (titleInput == "") {
        collapseSearchResult();
        return false;
    }

    // Send request
    addSpinner(SEARCH_SPINNER, "spinner-search");
    $.post(FIND_MOVIE, TITLE_PARAM + "=" + titleInput,
        function (movies) {
            $(SEARCH_RESULT).empty();
            if (movies.length > 0) {
                movies.forEach(movie => {
                    // Return result as link
                    let a = $("<a></a>");
                    a.addClass("dropdown-item text-wrap");
                    a.attr("href", "./" + SHOWTIME + "?" + MOVIE_ID_PARAM + "=" + movie.id);
                    a.html(highlight(movie.title, titleInput));
                    $(SEARCH_RESULT).append(a);
                });
            } else {
                // Not found
                let message = $("<h6></h6>");
                message.addClass("dropdown-header font-weight-bold");
                message.text("Movie not found!");
                $(SEARCH_RESULT).append(message);
            }
        }
    ).done(function () {
        if ($(SEARCH_INPUT).val().trim() != "") {
            showSearchResult();
        }
        removeSpinner(SEARCH_SPINNER);
    });

    return false;
}

// Highlight result from input
function highlight(result, input) {
    input = input.toLowerCase();
    let highlightedInput = result.substr(result.toLowerCase().indexOf(input), input.length);
    return result.replace(highlightedInput, "<b>" + highlightedInput + "</b>");
}

function addSpinner(spinnerId, style){
    removeSpinner(spinnerId); // reset
    $(spinnerId).addClass("text-center element-center");
    let span = $("<span></span>");
    span.addClass("spinner-border text-info " + style);
    $(spinnerId).append(span);
}

function removeSpinner(spinnerId){
    $(spinnerId).empty();
    $(spinnerId).removeClass("text-center element-center");
}
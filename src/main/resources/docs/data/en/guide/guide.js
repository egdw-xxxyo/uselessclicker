const sidemenu = {
    "../index.html": ["Main page", "icon", "glyphicon glyphicon-home"],
    "guide.html": ["User's guide", "icon", "glyphicon glyphicon-list"],
    "loops.html": ["Loops", "text", ""],
    "functions.html": ["Functions", "text", ""],
    "classes.html": ["Classes and objects", "text", ""],
    "keyboard.html": ["Keyboard", "text", ""],
    "mouseWheelAndButtons.html": ["Mouse buttons and wheel", "text", ""],
    "cursorMoving.html": ["Mouse movement", "text", ""],
    "delays.html": ["Delays", "text", ""],
    "eventsRecording.html": ["Events recording", "text", ""],
    "delayMultiplier.html": ["Speed and delay multiplier", "text", ""],
    "delaySystem.html": ["Delays system", "text", ""],
    "delaysInCombined.html": ["Delays in combined.run method", "text", ""]
};


$(document).ready(function () {
    $(".btn-expand-collapse").click(function (e) {
        $('.navbar-primary').toggleClass('collapsed');

        e.preventDefault();
    });

    $.each(sidemenu, function (page, title) {
        let $a = $("<a>")
            .attr("href", page);

        let $span1 = $("<span>");
        if (title[1] === "icon") {
            $span1.addClass(title[2]);
            $a.addClass('list-level-1');

        } else {
            $span1.text(title[2]);
            $a.addClass('list-level-2');
        }

        let $span2 = $("<span>")
            .addClass('nav-label')
            .text(title[0]);

        $a.append($span1);
        $a.append($span2);
        $(".navbar-primary-menu li").append($a);
    })
});

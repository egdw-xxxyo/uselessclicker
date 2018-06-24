const sidemenu = {
    "../index.html": ["Main page", "icon", "glyphicon glyphicon-home"],
    "../guide/guide.html": ["User's guide", "icon", "glyphicon glyphicon-list"],
    "examples.html": ["Examples", "icon", "glyphicon glyphicon-file"],
    "files.html": ["Files", "text", ""],
    "math.html": ["Math", "text", ""],
    "doubleClick.html": ["Double clicks", "text", ""],
    "threads.html": ["Threads", "text", ""]
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

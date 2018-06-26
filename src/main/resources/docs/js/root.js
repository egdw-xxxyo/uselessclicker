$(document).ready(function () {
    $.each(sidemenu, function (page, data) {
        let $a = $("<a>")
            .attr("href", page);

        let $icon = $("<span>");
        let $text = $("<span>")
            .addClass('nav-label')
            .text(data[0]);

        if (data[1] === "icon") {
            $icon.addClass(data[2]);
            $a.addClass('list-level-1');
        } else {
            $a.addClass('list-level-2');
        }


        if (current_page === page) {
            $a.addClass('current-page-item-menu');
        }

        $a.append($icon);
        $a.append($text);
        $(".navbar-primary-menu li").append($a);
    });

    // add copy buttons to "codebox" elements
    let $codeButtonBar = $("<ul>")
        .addClass("code-bar");
    let $li = $("<li>")
        .addClass("code-button");
    let $a = $("<a>")
        .attr("href", "#");

    let $copyButton = $li.clone().append(
        $a.clone()
            .addClass("glyphicon glyphicon-duplicate")
            .attr("title", "Copy to clipboard")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.copy(text);
            }));

    let $setButton = $li.clone().append(
        $a.clone()
            .addClass("glyphicon glyphicon-save-file")
            .attr("title", "Set text to the script editor field")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.set(text);
            }));

    $codeButtonBar
        .append($copyButton)
        .append($setButton);
    $(".codebox").append($codeButtonBar);
});
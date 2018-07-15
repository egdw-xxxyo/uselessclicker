$(document).ready(function () {
    $.each(sidemenu, function (page, data) {
        let $a = $("<a>")
            .attr("href", page);

        let $icon = $("<span>");
        let $text = $("<span>")
            .addClass('nav-label')
            .text(data[0]);

        if (data[1] === "1") {
            $icon.addClass(data[2]);
            $a.addClass('list-level-1');
        } else  if(data[1]==="2"){
            $a.addClass('list-level-2');
        }else {
            $a.addClass('list-level-3');
        }

        if (current_page === page) {
            $a.addClass('current-page-item-menu');
        }

        $a.append($icon);
        $a.append($text);
        $(".navbar-primary-menu li").append($a);
    });

    // add copy buttons to "codebox" elements
    let $li = $("<li>")
        .addClass("code-button");
    let $a = $("<a>")
        .attr("href", "#");

    // copy button
    let $copyButton = $li.clone().append(
        $a.clone()
            .addClass("glyphicon glyphicon-duplicate")
            .attr("title", "Copy to clipboard")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.copy(text);
            }));

    //set button
    let $setButton = $li.clone().append(
        $a.clone()
            .addClass("glyphicon glyphicon-save-file")
            .attr("title", "Set to the script editor field")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.set(text);
            }));

    let $codeButtonBar = $("<ul>")
        .addClass("code-bar")
        .append($copyButton)
        .append($setButton);
    $(".codebox").append($codeButtonBar);
});
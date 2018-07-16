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
        } else if (data[1] === "2") {
            $a.addClass('list-level-2');
        } else {
            $a.addClass('list-level-3');
        }

        if (current_page === page) {
            $a.addClass('current-page-item-menu');
        }

        $a.append($icon);
        $a.append($text);
        $(".navbar-primary-menu li").append($a);
    });

    //////////////////////////////////////////
    // code area

    // add copy buttons to "codebox" elements
    let $li = $("<li>")
        .addClass("code-button");

    // copy button
    let $copyButton = $li.clone().append(
        $("<a>")
            .addClass("glyphicon glyphicon-duplicate")
            .attr("title", "Copy to clipboard")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.copy(text);
            }));

    //set button
    let $setButton = $li.clone().append(
        $("<a>")
            .addClass("glyphicon glyphicon-save-file")
            .attr("title", "Set to the script editor field")
            .click(function (e) {
                console.log("set");
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.set(text);
            }));

    //show more button


    let getMoreButton = function(){
        return $li.clone().append($('<a />', {
            class: "glyphicon glyphicon-chevron-down",
            title: "Show more"
        }).click(function (e) {
            console.log("show more");
            $(e.target).toggleClass("glyphicon-chevron-down")
                .toggleClass("glyphicon-chevron-up");

            $(e.target).parent().parent().parent().find("pre").toggleClass("show-more");
        }));
    };
    // code buttons in code area
    let $codeButtonBar = $("<ul>")
        .addClass("code-bar")
        .append($copyButton)
        .append($setButton);
    let $codeboxes = $(".codebox");

    $codeboxes.append($codeButtonBar);
    // add show more button
    $codeboxes.each(function () {
        if ($(this).height() >= 200) {
            $(this).find('.code-bar').append(getMoreButton());
        }
    })

});


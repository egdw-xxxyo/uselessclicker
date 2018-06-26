$(document).ready(function () {
    $(".btn-expand-collapse").click(function (e) {
        $('.navbar-primary').toggleClass('collapsed');

        e.preventDefault();
    });

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


        if(current_page === page){
            $a.addClass('current-page-item-menu');
        }

        $a.append($icon);
        $a.append($text);
        $(".navbar-primary-menu li").append($a);
    });
    // for test in browser
    /*
    function Sys(){
      this.hello = function (text) {
          alert('Hello' + text);
      }  
    }

    var sys = new Sys();
    */


    // add copy buttons to "codebox" elements
    let $codeButtonBar = $("<ul>")
        .addClass("code-bar");
    let $li = $("<li>")
        .addClass("code-button");
    let $a = $("<a>")
        .attr("href", "#");

    let $copyButton = $li.clone().append(
        $a.clone()
            .text("Copy")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.hello(text);
            }));
    
    let $setButton = $li.clone().append(
        $a.clone()
            .text("Set")
            .click(function (e) {
                let text = $(e.target).parent().parent().parent().find("pre code").text();
                sys.set(text);
            }));

    $codeButtonBar
        .append($copyButton)
        .append($setButton);
    $(".codebox").append($codeButtonBar);
});
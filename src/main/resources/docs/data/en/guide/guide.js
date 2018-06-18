/*
const sidemenu = {
    "../index.html": "Главная страница",
    "classes.html": "Классы и объекты",
    "cursorMoving.html": "Перемещение курсора",
    "delayMultiplier.html": "Скорость и множитель задержек",
    "delays.html": "Задержки",
    "delaysInCombined.html": "Задержки в combined.run",
    "delaySystem.html": "Система задержек",
    "eventsRecording.html": "Захват событий",
    "functions.html": "Функции",
    "loops.html": "Циклы",
    "mouseWheelAndButtons.html": "Кнопки и колесико мыши"
};


$( document ).ready(function() {
    $(".btn-expand-collapse").click(function(e) {
      $('.navbar-primary').toggleClass('collapsed');
      
        e.preventDefault();
    });

    $.each(sidemenu, function (page, title) {
        let $a = $("<a>")
            .attr("href", page);

        let $span1 = $("<span>");
        let $span2 = $("<span>")
            .addClass('nav-label')
            .text(title);

        $a.append($span1);
      $a.append($span2);
        $(".navbar-primary-menu li").append($a);
    })
});
*/
const sidemenu = {
    "../index.html": ["Главная страница", "icon", "glyphicon glyphicon-home"],
    "guide.html": ["Руководство пользователя", "icon", "glyphicon glyphicon-list"],
    "classes.html": ["Классы и объекты", "text", ""],
    "cursorMoving.html": ["Перемещение курсора", "text", ""],
    "delayMultiplier.html": ["Скорость и множитель задержек", "text", ""],
    "delays.html": ["Задержки", "text", ""],
    "delaysInCombined.html": ["Задержки в combined.run", "text", ""],
    "delaySystem.html": ["Система задержек", "text", ""],
    "eventsRecording.html": ["Захват событий", "text", ""],
    "functions.html": ["Функции", "text", ""],
    "loops.html": ["Циклы", "text", ""],
    "mouseWheelAndButtons.html": ["Кнопки и колесико мыши", "text", ""]
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

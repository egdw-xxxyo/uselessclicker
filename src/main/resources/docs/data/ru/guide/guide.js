const sidemenu = {
    "../index.html": ["Главная страница", "icon", "glyphicon glyphicon-home"],
    "guide.html": ["Руководство пользователя", "icon", "glyphicon glyphicon-list"],
    "loops.html": ["Циклы", "text", ""],
    "functions.html": ["Функции", "text", ""],
    "classes.html": ["Классы и объекты", "text", ""],
    "keyboard.html": ["Клавиатура", "text", ""],
    "mouseWheelAndButtons.html": ["Кнопки и колесико мыши", "text", ""],
    "cursorMoving.html": ["Перемещение курсора", "text", ""],
    "delays.html": ["Задержки", "text", ""],
    "eventsRecording.html": ["Захват событий", "text", ""],
    "delayMultiplier.html": ["Скорость и множитель задержек", "text", ""],
    "delaySystem.html": ["Система задержек", "text", ""],
    "delaysInCombined.html": ["Задержки в combined.run", "text", ""]
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

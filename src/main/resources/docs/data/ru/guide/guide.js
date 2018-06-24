const sidemenu = {
    "../index.html": ["Главная страница", "icon", "glyphicon glyphicon-home"],
    "guide.html": ["Руководство пользователя", "icon", "glyphicon glyphicon-list"],
    "eventsRecording.html": ["Захват событий", "text", ""],
    "loops.html": ["Циклы", "text", ""],
    "functions.html": ["Функции", "text", ""],
    "classes.html": ["Классы и объекты", "text", ""],
    "keyboard.html": ["Клавиатура", "text", ""],
    "mouseWheelAndButtons.html": ["Кнопки и колесико мыши", "text", ""],
    "cursorMoving.html": ["Перемещение курсора", "text", ""],
    "delays.html": ["Задержки", "text", ""],
    "delaySystem.html": ["Система задержек", "text", ""],
    "delayMultiplier.html": ["Скорость и множитель задержек", "text", ""],
    "delaysInCombined.html": ["Задержки в combined.run", "text", ""],
    "../examples/examples.html": ["Примеры", "icon", "glyphicon glyphicon-file"]
};


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
    })
});
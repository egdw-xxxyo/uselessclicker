//PARAMETER TEMPLATE
//$parameterName:
//parameter body
//parameter body
//
//$nextParameter...
//At the end of a parameter must be 
//empty line
///THIS IS MAX SIZE FOR EACH LINE////

//Toggles
//Keyboard
$btnInsertKeyName:
//Вставляет имена нажатых клавишь
//
//Пример:
CONTROL ALT DELETE
//будет вставлено при нажатии
//соответствующих клавишь

$btnInsertKeyCode:
//Вставляет код для событий
//нажатия и отпускания клавишь
//
//Пример:
key.press('CONTROL');
key.press('С');
key.release('С');
key.release('CONTROL');
//будет вставлено при нажатии и
//отпускании двух клавишь
//"CONTROL C"

$btnInsertKeyCodeWithDelay:
//Вставляет код для событий нажатия и
//отпускания клавишь с задержками
//между событиями
//
//Пример:
system.sleep(200);
key.press('CONTROL');
system.sleep(100);
key.press('С');
system.sleep(100);
key.release('С');
system.sleep(200);
key.release('CONTROL');
//будет вставлено при нажатии и
//отпускании двух клавишь 
//"CONTROL C"

//Mouse basics
$btnInsertMouseName:
//Вставляет имена нажатых клавишь
//мыши
//
//Пример:
LEFT MIDDLE RIGHT
//будет вставлено при нажатии
//соответсвующих клавишь

$btnInsertMouseClick:
//Вставляет код для клика
//соответсвующией клавиши мыши
//
//Пример:
mouse.click('LEFT');
mouse.click('RIGHT');
//будет вставлено при клике левой и
//правой клавишей миши

$btnInsertMouseClickAt:
//Вставляет код для клика
//соответсвующей клавиши мыши в
//определенной точке
//
//Пример:
mouse.clickAt('LEFT',500,500);
mouse.clickAt('RIGHT',600,600);
//будет вставлено при клике левой и
//правой клавишей миши

$btnInsertMouseMove:
//Вставляет код для относительного
//движения курсора
//
//Пример:
mouse.move(100,-50);
//будет вставлено при перемещении
//курсора

$btnInsertMouseMoveAt:
//Вставляет код для перемещения
//курсора в указанную точку
//
//Пример:
mouse.moveAt(200,200);
//будет вставлено при клике в
//указанной точке

$btnInsertMousePress:
//Вставляет код для нажатия
//соответствующей клавиши мыши
//
//Пример:
mouse.press('LEFT');
mouse.press('RIGHT');
//будет вставлено при нажатии левой
//и правой клавиши миши

$btnInsertMousePressAt:
//Вставляет код для нажатия
//соответствующей клавиши мыши в
//указанной точке
//
//Пример:
mouse.pressAt('LEFT',400,400);
//будет всталено при нажатии левой
//клавиши мыши в указанной точке

$btnInsertMouseRelease:
//Вставляет код для отпускания
//соответствующей клавиши мыши
//
//Пример:
mouse.release('LEFT');
mouse.release('RIGHT');
//будет вставлено при отпускании
//лувой и правой клавиши мыши

$btnInsertMouseReleaseAt:
//Вставляет код для отпускания
//соответствующей клавиши мыши в
//указанной точке
//
//Пример:
mouse.releaseAt('LEFT',400,400);
//будет всталено при отпускании левой
//клавиши мыши в указанной точке

/////////////////////////////////////
//Mouse code
$btnInsertMouseCode:
//Вставляет код для нажатия и
//отпускания клавишь мыши
//
//Пример:
mouse.pressAt('LEFT',100,0);
mouse.releaseAt('LEFT',200,0);
//будет всталено при нажатии левой
//клавиши мыши перемещении курсора
//и отпускании клавиши

$btnInsertMouseCodeWithDelay:
//Вставляет код для нажатия и
//отпускания клавишь мыши с
//задержками между событиями
//
//Пример:
system.sleep(200);
mouse.pressAt('LEFT',100,0);
system.sleep(200);
mouse.releaseAt('LEFT',200,0);
system.sleep(500);
//будет всталено при нажатии левой
//клавиши мыши перемещении курсора
//и отпускании клавиши

$btnInsertMouseRelativeCode:
//Вставляет код для нажатия и
//отпускания клавишь мыши
//относительно прошлого события
//
//Пример:
mouse.moveAndPress('LEFT',50,0);
mouse.moveAndRelease('LEFT',0,0);
//будет вставлено при перемещении
//курсора вправо на 50 пикселей,
//нажатия и отпускания левой
//клавиши мыши в той же точке

/////////////////////////////////////
//click
$btnInsertMouseCodeClick:
//Вставляет код для нажатия и
//отпускания клавишь мыши с
//распознаванием кликов
//
//Пример:
mouse.clickAt('LEFT',200,200);
//будет вставлено при клике левой
//клавишей мыши

/////////////////////////////////////
//Movement
$btnInsertAbsolutePath:
//Вставляет код для движения курсора
//по абсолютным координатам
//
//Пример:
mouse.moveAbsolute('010100100100100'+
'1101000101011101010010110101111010'+
'000111100011010101');

$btnInsertRelativePath:
//Вставляет код для движения курсора
//по относительным координатам
//
//Пример:
mouse.moveRelative('010100100100100'+
'1101000101011101010010110101110010'+
'000111100011010101');

$btnInsertAbsolutePathWithDelays:
//Вставляет код для движения курсора
//по абсолютным координатам с
//задержками между событиями
//
//Пример:
mouse.moveAbsolute_D('0101001010100'+
'1101000101011101010010110101110010'+
'000111100011010101');

$btnInsertRelativePathWithDelays:
//Вставляет код для движения курсора
//по относительным координатам с
//задержками между событиями
//
//Пример:
mouse.moveRelative_D('0101001010100'+
'1101000101011101010010110010010010'+
'000111100011010101');

///THIS IS MAX SIZE FOR EACH LINE////
////////////TEMPLATES////////////////
/////////////////////////////////////
//MOUSE
$btnTemplateMouseClick:
mouse.click('');
//Клик клавишей мыши
//
//mouse.click('BTN');
//   BTN - имя клавиши
//
//Метод состоит из двух вызовов
//нажатия и отпускания клавиши мыши:
//  press('BTN');
//    sleep(pressDelay);
//  release('BTN');
//    sleep(releaseDelay);

$btnTemplateMouseClickCode:
mouse.click('');

$btnTemplateMouseClickAt:
mouse.clickAt('',,);
//Клик клавишей мыши в укзанной точке
//
//mouse.clickAt('BTN',x,y);
//  BTN - имя клавиши
//  x,y - координаты
//
//Метод состоит из двух вызовов
//нажатия и отпускания клавиши мыши:
//  pressAt('BTN',x,y);
//    sleep(pressDelay);
//  releaseAt('BTN',x,y);
//    sleep(releaseDelay);

$btnTemplateMouseClickAtCode:
mouse.clickAt('',,);

$btnTemplateMouseGetMoveDelay:
mouse.getMoveDelay();
//Получить задержку передвижения
//курсора мыши в мс. (moveDelay)
//
//mouse.getMoveDelay():int

$btnTemplateMouseGetMoveDelayCode:
mouse.getMoveDelay();

$btnTemplateMouseGetPressDelay:
mouse.getPressDelay();
//Получить задержку нажатия клавиши
//мыши в мс. (pressDelay)
//
//mouse.getPressDelay():int
//

$btnTemplateMouseGetPressDelayCode:
mouse.getPressDelay();

$btnTemplateMouseGetReleaseDelay:
mouse.getReleaseDelay();
//Получить задержку отпускания
//клавиши мыши в мс. (releaseDelay)
//
//mouse.getReleaseDelay():int
//

$btnTemplateMouseGetReleaseDelayCode:
mouse.getReleaseDelay();

$btnTemplateMouseGetX:
mouse.getX();
//Получить координату 'x' последнего
//события мыши.
//
//mouse.getX():int
//
//Если после запуска программы курсор
//не перемещался и клавиши не
//нажимались возвращает: 0

$btnTemplateMouseGetXCode:
mouse.getX();

$btnTemplateMouseGetY:
mouse.getY();
//Получить координату 'y' последнего
//события мыши
//
//mouse.getY():int
//
//Если после запуска программы курсор
//не перемещался и клавиши не
//нажимались возвращает: 0

$btnTemplateMouseGetYCode:
mouse.getY();

$btnTemplateMouseMove:
mouse.move(,);
//Передвинуть курсор относительно
//координат последнего события
//
//mouse.move(dx,dy);
//  dx, dy - смещения
//
//Метод состоит из:
//  move(dx,dy);
//    sleep(moveDelay);

$btnTemplateMouseMoveCode:
mouse.move(,);

$btnTemplateMouseMoveAbsolute:
mouse.moveAbsolute('');

$btnTemplateMouseMoveAbsoluteCode:
mouse.moveAbsolute('');

$btnTemplateMouseMoveAbsolute_D:
mouse.moveAbsolute_D('');

$btnTemplateMouseMoveAbsolute_DCode:
mouse.moveAbsolute_D('');

$btnTemplateMouseMoveAndClick:
mouse.moveAndClick('',,);
//Передвинуть курсор относительно
//координат последнего события
//и кликнуть
//
//mouse.moveAndClick('BTN',dx,dy);
//  BTN - имя клавиши
//  dx, dy - смещения
//
//Метод состоит из:
//  move(dx,dy);
//    sleep(moveDelay);
//  press('BTN');
//    sleep(pressDelay);
//  release('BTN');
//    sleep(releaseDelay);


$btnTemplateMouseMoveAndClickCode:
mouse.moveAndClick('',,);

$btnTemplateMouseMoveAndPress:
mouse.moveAndPress('',,);
//Передвинуть курсор относительно
//координат последнего события
//и нажать клавишу мыши
//
//mouse.moveAndPress('BTN',dx,dy);
//  BTN - имя клавиши
//  dx, dy - смещения
//
//Метод состоит из:
//  move(dx,dy);
//    sleep(moveDelay);
//  press('BTN');
//    sleep(pressDelay);

$btnTemplateMouseMoveAndPressCode:
mouse.moveAndPress('',,);

$btnTemplateMouseMoveAndRelease:
mouse.moveAndRelease('',,);
//Передвинуть курсор относительно
//координат последнего события
//и отпустить клавишу мыши
//
//mouse.moveAndPress('BTN',dx,dy);
//  BTN - имя клавиши
//  dx, dy - смещения
//
//Метод состоит из:
//  move(dx,dy);
//    sleep(moveDelay);
//  release('BTN');
//    sleep(releaseDelay);

$btnTemplateMouseMoveAndReleaseCode:
mouse.moveAndRelease('',,);

$btnTemplateMouseMoveRelative:
mouse.moveRelative('');

$btnTemplateMouseMoveRelativeCode:
mouse.moveRelative('');

$btnTemplateMouseMoveRelative_D:
mouse.moveRelative_D('');

$btnTemplateMouseMoveRelative_DCode:
mouse.moveRelative_D('');

$btnTemplateMouseMoveTo:
mouse.moveTo(,);
//Передвинуть курсор в указанную
//точку
//
//mouse.moveTo(x,y);
//  x, y - координаты
//
//Метод состоит из:
//  moveTo(x,y);
//    sleep(moveDelay);

$btnTemplateMouseMoveToCode:
mouse.moveTo(,);

$btnTemplateMousePress:
mouse.press('');
//Нажать клавишу мыши
//
//mouse.press('BTN');
//  BTN - имя клавиши
//
//Метод состоит из:
//  press('BTN');
//    sleep(pressDelay);

$btnTemplateMousePressCode:
mouse.press('');

$btnTemplateMousePressAt:
mouse.pressAt('',,);
//Передвинуть курсор в указанную
//точку и нажать клавишу мыши
//
//mouse.pressAt('BTN',x,y);
//  BTN - имя клавиши
//  x, y - координаты
//
//Метод состоит из:
//  moveTo(x,y);
//    sleep(moveDelay);
//  press('BTN');
//    sleep(pressDelay);

$btnTemplateMousePressAtCode:
mouse.pressAt('',,);

$btnTemplateMouseRelease:
mouse.release('');
//Отпустить клавишу мыши
//
//mouse.release('BTN');
//  BTN - имя клавиши
//
//Метод состоит из:
//  press('BTN');
//    sleep(pressDelay);

$btnTemplateMouseReleaseCode:
mouse.release('');

$btnTemplateMouseReleaseAt:
mouse.releaseAt('',,);
//Передвинуть курсор в указанную
//точку и отпустить клавишу мыши
//
//mouse.releaseAt('BTN',x,y);
//  BTN - имя клавиши
//  x, y - координаты
//
//Метод состоит из:
//  moveTo(x,y);
//    sleep(moveDelay);
//  release('BTN');
//    sleep(releaseDelay);

$btnTemplateMouseReleaseAtCode:
mouse.releaseAt('',,);

$btnTemplateMouseSetMoveDelay:
mouse.setMoveDelay();
//Установить задержку передвижения
//курсора мыши в мс.
//
//mouse.setMoveDelay(moveDelay)
//  moveDelay - задержка

$btnTemplateMouseSetMoveDelayCode:
mouse.setMoveDelay();

$btnTemplateMouseSetPressDelay:
mouse.setPressDelay();
//Установить задержку нажатия
//клавиши мыши в мс.
//
//mouse.setPressDelay(pressDelay)
//  pressDelay - задержка

$btnTemplateMouseSetPressDelayCode:
mouse.setPressDelay();

$btnTemplateMouseSetReleaseDelay:
mouse.setReleaseDelay();
//Установить задержку отпускания
//клавиши мыши в мс.
//
//mouse.setReleaseDelay(releaseDelay)
//  releaseDelay - задержка

$btnTemplateMouseSetReleaseDelayCode:
mouse.setReleaseDelay();

$btnTemplateMouseSetX:
mouse.setX();
//Передвинуть курсор мыши по оси 'x'
//в указанную координату
//
//mouse.setX(x);
//  x - координата
//
//Метод состоит из:
//  moveTo(x,getY());
//    sleep(moveDelay);

$btnTemplateMouseSetXCode:
mouse.setX();

$btnTemplateMouseSetY:
mouse.setY();
//Передвинуть курсор мыши по оси 'y'
//в указанную координату
//
//mouse.setY(y);
//  y - координата
//
//Метод состоит из:
//  moveTo(x,getY());
//    sleep(moveDelay);

$btnTemplateMouseSetYCode:
mouse.setY();

$btnTemplateMouseWheel:
mouse.wheel('',);
//Прокрутить колесо мыши на указанное
//количество строк
//
//mouse.wheel("DIRECTION", amount);
//  DIRECTION - направление UP/DOWN
//  amount - количество строк
//
//Метод состоит из:
//  wheel(amount);
//    sleep(wheelDelay);

$btnTemplateMouseWheelCode:
mouse.wheel('',);

/////////////////////////////////////
///THIS IS MAX SIZE FOR EACH LINE////
//KEYBOARD
$btnTemplateKeyGetPressDelay:
key.getPressDelay();
//Получить задержку нажатия клавиши
//клавиатуры в мс. (pressDelay)
//
//key.getPressDelay():int

$btnTemplateKeyGetPressDelayCode:
key.getPressDelay();

$btnTemplateKeyGetReleaseDelay:
key.getReleaseDelay();
//Получить задержку отпускания
//клавиши клавиатуры в мс.
//(releaseDelay)
//
//key.getReleaseDelay():int

$btnTemplateKeyGetReleaseDelayCode:
key.getReleaseDelay();

$btnTemplateKeyIsPressed:
key.isPressed('');
//Узнать зажаты ли указанные клавиши
//клавиатуры
//
//key.isPressed('KEY1 KEY2'):boolean
//  KEY1, KEY2 - имена клавишь через
//    пробел

$btnTemplateKeyIsPressedCode:
key.isPressed('');

$btnTemplateKeyPress:
key.press('');
//Нажать клавишу клавиатуры
//
//key.press('KEY');
//  KEY - имя клавиши
//
//Метод состоит из:
//  press('KEY');
//    sleep(pressDelay);

$btnTemplateKeyPressCode:
key.press('');

$btnTemplateKeyRelease:
key.release('');
//Отпустить клавишу клавиатуры
//
//key.release('KEY');
//  KEY - имя клавиши
//
//Метод состоит из:
//  release('KEY');
//    sleep(releaseDelay);

$btnTemplateKeyReleaseCode:
key.release('');

$btnTemplateKeyType:
key.type('');
//Последовательно нажать и отпустить
//каждую укзанную клавишу
//
//key.release('KEY1 KEY2');
//  KEY1, KEY2 - имена клавишь через
//    пробел
//
//Метод выполняет для каждой клавиши:
//  press('KEY');
//    sleep(pressDelay);
//  release('KEY');
//    sleep(releaseDelay);

$btnTemplateKeyTypeCode:
key.type('');

$btnTemplateKeySetPressDelay:
key.setPressDelay();
//Установить задержку нажатия
//клавиши клавиатуры в мс.
//
//key.setPressDelay(pressDelay)
//  pressDelay - задержка

$btnTemplateKeySetPressDelayCode:
key.setPressDelay();

$btnTemplateKeySetReleaseDelay:
key.setReleaseDelay();
//Установить задержку отпускания
//клавиши клавиатуры в мс.
//
//key.setReleaseDelay(releaseDelay)
//  releaseDelay - задержка

$btnTemplateKeySetReleaseDelayCode:
key.setReleaseDelay();

/////////////////////////////////////
///THIS IS MAX SIZE FOR EACH LINE////
//SYSTEM
$btnTemplateSystemSleep:
system.sleep();
//Приостановить скрипт на заданное
//время в мс.
//
//system.sleep(ms);
//  ms - время в мс.

$btnTemplateSystemSleepCode:
system.sleep();

$btnTemplateSystemPrint:
system.print('');
//Вывести сообщение в поле вывода
//
//system.print('TEXT');

$btnTemplateSystemPrintCode:
system.print('');

$btnTemplateSystemPrintln:
system.println('');
//Вывести сообщение в поле вывода
//и перейти на новую строку
//
//system.println('TEXT');

$btnTemplateSystemPrintlnCode:
system.println('');

$btnTemplateSystemRegisterShortcut:
system.registerShortcut('','');
//Зарегистрировать сочетание клавишь
//для вызова процедуры
//
//system.registerShortcut
//    ('BTN2 BTN2','FUNCT');
//  BTN1, BTN2 - имена клавишь через
//    пробел
//  FUNCT - имя вызываемого метода

$btnTemplateSystemRegisterShortcutCode:
system.registerShortcut('','');

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

//Mouse code
$btnInsertMouseCode:
mouse.pressAt('LEFT',100,0);
mouse.releaseAt('LEFT',200,0);
mouse.pressAt('RIGHT',300,200);
mouse.releaseAt('RIGHT',300,200);

$btnInsertMouseCodeWithDelay:
mouse.pressAt('LEFT',100,0);
system.sleep(200);
mouse.releaseAt('LEFT',200,0);
system.sleep(500);
mouse.pressAt('RIGHT',300,200);
system.sleep(50);
mouse.releaseAt('RIGHT',300,200);

$btnInsertMouseRelativeCode:
mouse.moveAndPress('LEFT',50,0);
mouse.moveAndRelease('LEFT',0,50);
mouse.moveAndPress('RIGHT',-100,0);
mouse.moveAndRelease('RIGHT',0,-50);
mouse.move(50,0);

//click
$btnInsertMouseCodeClick:
mouse.clickAt('LEFT',200,200);
mouse.pressAt('LEFT',200,200);
mouse.releaseAt('LEFT',300,300);
mouse.clickAt('RIGHT',300,300);

//Movement
$btnInsertAbsolutePath:
mouse.moveAbsolute('010100100100100'+
'1101000101011101010010110101111010'+
'000111100011010101');

$btnInsertRelativePath:
mouse.moveRelative('010100100100100'+
'1101000101011101010010110101110010'+
'000111100011010101');

$btnInsertAbsolutePathWithDelays:
mouse.moveAbsolute_D('0101001010100'+
'1101000101011101010010110101110010'+
'000111100011010101');

$btnInsertRelativePathWithDelays:
mouse.moveRelative_D('0101001010100'+
'1101000101011101010010110010010010'+
'000111100011010101');

///THIS IS MAX SIZE FOR EACH LINE////
////////////TEMPLATES////////////////
/////////////////////////////////////
//MOUSE
$btnTemplateMouseClick:
//This button will past this code 
//  to carret position:
mouse.click('',,);

$btnTemplateMouseClickCode:
mouse.click('',,);

$btnTemplateMouseClickAt:
mouse.clickAt('',,);

$btnTemplateMouseClickAtCode:
mouse.clickAt('',,);

$btnTemplateMouseGetMoveDelay:
mouse.getMoveDelay();

$btnTemplateMouseGetMoveDelayCode:
mouse.getMoveDelay();

$btnTemplateMouseGetPressDelay:
mouse.getPressDelay();

$btnTemplateMouseGetPressDelayCode:
mouse.getPressDelay();

$btnTemplateMouseGetReleaseDelay:
mouse.getReleaseDelay();

$btnTemplateMouseGetReleaseDelayCode:
mouse.getReleaseDelay();

$btnTemplateMouseGetX:
mouse.getX();

$btnTemplateMouseGetXCode:
mouse.getX();

$btnTemplateMouseGetY:
mouse.getY();

$btnTemplateMouseGetYCode:
mouse.getY();

$btnTemplateMouseMove:
mouse.move(,);

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

$btnTemplateMouseMoveAndClickCode:
mouse.moveAndClick('',,);

$btnTemplateMouseMoveAndPress:
mouse.moveAndPress('',,);

$btnTemplateMouseMoveAndPressCode:
mouse.moveAndPress('',,);

$btnTemplateMouseMoveAndRelease:
mouse.moveAndRelease('',,);

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

$btnTemplateMouseMoveToCode:
mouse.moveTo(,);

$btnTemplateMousePress:
mouse.press('');

$btnTemplateMousePressCode:
mouse.press('');

$btnTemplateMousePressAt:
mouse.pressAt('',,);

$btnTemplateMousePressAtCode:
mouse.pressAt('',,);

$btnTemplateMouseRelease:
mouse.release('');

$btnTemplateMouseReleaseCode:
mouse.release('');

$btnTemplateMouseReleaseAt:
mouse.releaseAt('',,);

$btnTemplateMouseReleaseAtCode:
mouse.releaseAt('',,);

$btnTemplateMouseSetMoveDelay:
mouse.setMoveDelay();

$btnTemplateMouseSetMoveDelayCode:
mouse.setMoveDelay();

$btnTemplateMouseSetPressDelay:
mouse.setPressDelay();

$btnTemplateMouseSetPressDelayCode:
mouse.setPressDelay();

$btnTemplateMouseSetReleaseDelay:
mouse.setReleaseDelay();

$btnTemplateMouseSetReleaseDelayCode:
mouse.setReleaseDelay();

$btnTemplateMouseSetX:
mouse.setX();

$btnTemplateMouseSetXCode:
mouse.setX();

$btnTemplateMouseSetY:
mouse.setY();

$btnTemplateMouseSetYCode:
mouse.setY();

///THIS IS MAX SIZE FOR EACH LINE////
//KEYBOARD
$btnTemplateKeyGetPressDelay:
key.getPressDelay();

$btnTemplateKeyGetPressDelayCode:
key.getPressDelay();

$btnTemplateKeyGetReleaseDelay:
key.getReleaseDelay();

$btnTemplateKeyGetReleaseDelayCode:
key.getReleaseDelay();

$btnTemplateKeyIsPressed:
key.isPressed('');

$btnTemplateKeyIsPressedCode:
key.isPressed('');

$btnTemplateKeyPress:
key.press('');

$btnTemplateKeyPressCode:
key.press('');

$btnTemplateKeyRelease:
key.release('');

$btnTemplateKeyReleaseCode:
key.release('');

$btnTemplateKeyType:
key.type('');

$btnTemplateKeyTypeCode:
key.type('');

$btnTemplateKeySetPressDelay:
key.setPressDelay();

$btnTemplateKeySetPressDelayCode:
key.setPressDelay();

$btnTemplateKeySetReleaseDelay:
key.setReleaseDelay();

$btnTemplateKeySetReleaseDelayCode:
key.setReleaseDelay();

///THIS IS MAX SIZE FOR EACH LINE////
//SYSTEM
$btnTemplateSystemSleep:
system.sleep();

$btnTemplateSystemSleepCode:
system.sleep();

$btnTemplateSystemPrint:
system.print('');

$btnTemplateSystemPrintCode:
system.print('');

$btnTemplateSystemPrintln:
system.println('');

$btnTemplateSystemPrintlnCode:
system.println('');

$btnTemplateSystemRegisterShortcut:
system.registerShortcut('','');

$btnTemplateSystemRegisterShortcutCode:
system.registerShortcut('','');

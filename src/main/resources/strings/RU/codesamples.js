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
CONTROL ALT DELETE

$btnInsertKeyCode:
key.press('CONTROL');
key.release('CONTROL');
key.press('ALT');
key.release('ALT');

$btnInsertKeyCodeWithDelay:
system.sleep(6088);
key.press('CONTROL');
system.sleep(768);
key.press('C');
system.sleep(80);
key.release('C');
system.sleep(40);
key.release('CONTROL');

//Mouse basics
$btnInsertMouseName:
LEFT MIDDLE RIGHT 

$btnInsertMouseClick:
mouse.click('LEFT');
mouse.click('RIGHT');

$btnInsertMouseClickAt:
mouse.clickAt('LEFT',428,354);
mouse.clickAt('RIGHT',443,369);

$btnInsertMouseMove:
mouse.move(191,23);

$btnInsertMouseMoveAt:
//Click to paste
mouse.moveAt(570,359);
mouse.moveAt(533,497);

$btnInsertMousePress:
mouse.press('LEFT');

$btnInsertMousePressAt:
mouse.pressAt('LEFT',437,523);

$btnInsertMouseRelease:
mouse.release('LEFT');

$btnInsertMouseReleaseAt:
mouse.releaseAt('LEFT',539,479);

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
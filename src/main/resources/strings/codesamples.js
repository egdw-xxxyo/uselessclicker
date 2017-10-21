$insertKeyName:
CONTROL ALT DELETE

$insertKeyCode:
key.press('CONTROL');
key.press('ALT');
key.press('DELETE');
key.release('CONTROL');
key.release('ALT');
key.release('DELETE');

$insertKeyCodeWithDelay:
key.press('CONTROL');
system.sleep(200);
key.press('ALT');
system.sleep(200);
key.press('DELETE');
system.sleep(500);
key.release('DELETE');
system.sleep(50);
key.release('ALT');
system.sleep(50);
key.release('CONTROL');

$insertMouseName:
LEFT MIDDLE RIGHT

$insertMouseCode:
mouse.pressAt('LEFT',100,0);
mouse.releaseAt('LEFT',200,0);
mouse.pressAt('RIGHT',300,200);
mouse.releaseAt('RIGHT',300,200);

$insertMouseCodeWithDelay:
mouse.pressAt('LEFT',100,0);
system.sleep(200);
mouse.releaseAt('LEFT',200,0);
system.sleep(500);
mouse.pressAt('RIGHT',300,200);
system.sleep(50);
mouse.releaseAt('RIGHT',300,200);

$insertMouseRelativeCode:
mouse.moveAndPress('LEFT',50,0);
mouse.moveAndRelease('LEFT',0,50);
mouse.moveAndPress('RIGHT',-100,0);
mouse.moveAndRelease('RIGHT',0,-50);
mouse.move(50,0);

$insertMouseCodeClick:
mouse.clickAt('LEFT',200,200);
mouse.pressAt('LEFT',200,200);
mouse.releaseAt('LEFT',300,300);
mouse.clickAt('RIGHT',300,300);

$insertAbsolutePath:
mouse.moveAbsolute('㚐㙰㚎㙰㚉㙰㚇㙱㚅㙲㚁㙴㙾㙶㙽㙸㙺㙼㙹㙾㙶㚄㙵㚇㙳㚋㙲㚒㙱㚖㙱㚙㙱㚠㙱㚤㙲㚧㙴㚭㙷㚳㙹㚵㙼㚷㚁㚹㚄㚺㚇'+
'㚺㚚㚸㚞㚶㚧㚱㚰㚫㚷㚦㛂㚜㛈㚘㛍㚓㛑㚎㛒㚋㛒㚈㛒㚆㛑㚅㛏㚃㛍㚃㛈㚂㛇㚂㛄㚂㛄㚃㛂㚅㛀㚋㚿㚐㚾㚕㚽㚠㚺㚬㚹㚳㚷㚿㚵㛇㚴㛎㚲㛕㚱㛙㚫㛢㚪㛣㚫㛣㚬'+
'㛥㚡㛥㚞㛤㚙㛢㚕㛟㚑㛛㚉㛒㚁㛊㙻㛃㙵㚺㙱㚴㙰㚮㙰㚬㙱㚫㙴㚪㙶㚩㙼㚩㚀㚩㚅㚪㚎㚬㚗㚰㚨㚸㚱㚼㚺㚽㛍㛀㛘㛀㛠㛂㛰㛂㛴㛀㛿㚾㜄㚺㜇㚶㜋㚰㜍㚪㜎㚦㜏'+
'㚄㜉㙿㜉㙸㜉㙵㜇㙲㜆㙱㜄㙰㜂㙰㜀㙰㛿㙰㛽㙴㛼㙸㛺㚄㛷㚈㛳㚔㛯㚛㛫㚢㛤㚴㛜㚽㛏㛌㛇㛒㛂㛖㚺㛙㚴㛛㚯㛜㚪㛞㚤㛞㚡㛟㚠㛟㚜㛝㚙㛜㚔㛘㚒㛖㚌㛑㚇㛌㚅'+
'㛉㚂㛇㚁㛂㚁㛁㚂㚿㚅㚾㚆㚽㚇㚽㚉㚼㚍㚻㚏㚻㚒㚻㚕㚻㚖㚻㚙㚻㚚㚻㚛㚻');

$insertRelativePath:
mouse.moveRelative('㚐㙰㚎㙰㚉㙰㚇㙱㚅㙲㚁㙴㙾㙶㙽㙸㙺㙼㙹㙾㙶㚄㙵㚇㙳㚋㙲㚒㙱㚖㙱㚙㙱㚠㙱㚤㙲㚧㙴㚭㙷㚳㙹㚵㙼㚷㚁㚹㚄㚺㚇'+
'㚺㚚㚸㚞㚶㚧㚱㚰㚫㚷㚦㛂㚜㛈㚘㛍㚓㛑㚎㛒㚋㛒㚈㛒㚆㛑㚅㛏㚃㛍㚃㛈㚂㛇㚂㛄㚂㛄㚃㛂㚅㛀㚋㚿㚐㚾㚕㚽㚠㚺㚬㚹㚳㚷㚿㚵㛇㚴㛎㚲㛕㚱㛙㚫㛢㚪㛣㚫㛣㚬'+
'㛣㚪㛥㚞㛤㚙㛢㚕㛟㚑㛛㚉㛒㚁㛊㙻㛃㙵㚺㙱㚴㙰㚮㙰㚬㙱㚫㙴㚪㙶㚩㙼㚩㚀㚩㚅㚪㚎㚬㚗㚰㚨㚸㚱㚼㚺㚽㛍㛀㛘㛀㛠㛂㛰㛂㛴㛀㛿㚾㜄㚺㜇㚶㜋㚰㜍㚪㜎㚦㜏'+
'㚜㜏㙿㜉㙸㜉㙵㜇㙲㜆㙱㜄㙰㜂㙰㜀㙰㛿㙰㛽㙴㛼㙸㛺㚄㛷㚈㛳㚔㛯㚛㛫㚢㛤㚴㛜㚽㛏㛌㛇㛒㛂㛖㚺㛙㚴㛛㚯㛜㚪㛞㚤㛞㚡㛟㚠㛟㚜㛝㚙㛜㚔㛘㚒㛖㚌㛑㚇㛌㚅'+
'㛉㚂㛇㚁㛂㚁㛁㚂㚿㚅㚾㚆㚽㚇㚽㚉㚼㚍㚻㚏㚻㚒㚻㚕㚻㚖㚻㚙㚻㚚㚻㚛㚻');

$insertAbsolutePathWithDelays:
mouse.moveAbsolute_D('㚛㙰㚎㙰㚉㙰㚇㙱㚅㙲㚁㙴㙾㙶㙽㙸㙺㙼㙹㙾㙶㚄㙵㚇㙳㚋㙲㚒㙱㚖㙱㚙㙱㚠㙱㚤㙲㚧㙴㚭㙷㚳㙹㚵㙼㚷㚁㚹㚄'+
'㚻㚍㚼㚐㚼㚗㚺㚚㚸㚞㚶㚧㚱㚰㚫㛍㚓㛑㚎㛒㚋㛒㚈㛒㚆㛑㚅㛏㚃㛍㚃㛈㚂㛇㚂㛄㚂㛄㚃㛂㚅㛀㚋㚿㚐㚾㚕㚽㚠㚺㚬㚹㚳㚷㚿㚵㛇㚴㛎㚲㛕㚱㛙㚫㛢㚪㛣㚫㛣㚬'+
'㛣㚪㛤㚨㛥㚦㛥㚡㛥㚞㛤㚙㛢㚕㛊㙻㛃㙵㚺㙱㚴㙰㚮㙰㚬㙱㚫㙴㚪㙶㚩㙼㚩㚀㚩㚅㚪㚎㚬㚗㚰㚨㚸㚱㚼㚺㚽㛍㛀㛘㛀㛠㛂㛰㛂㛴㛀㛿㚾㜄㚺㜇㚶㜋㚰㜍㚪㜎㚦㜏'+
'㚜㜏㚖㜎㚌㜋㚄㜉㙿㜉㙸㜉㙵㜂㙰㜀㙰㛿㙰㛽㙴㛼㙸㛺㚄㛷㚈㛳㚔㛯㚛㛫㚢㛤㚴㛜㚽㛏㛌㛇㛒㛂㛖㚺㛙㚴㛛㚯㛜㚪㛞㚤㛞㚡㛟㚠㛟㚜㛝㚙㛜㚔㛘㚒㛖㚌㛑㚇㛌㚅'+
'㛉㚂㛇㚁㛂㚁㛁㚂㚿㚅㚾㚆㚽㚇㚽㚉㚼㚍㚻㚏㚻㚒㚻㚕㚻㚖㚻㚙㚻㚚㚻㚛㚻');

$insertRelativePathWithDelays:
mouse.moveRelative_D('㚛㙰㚎㙰㚉㙰㚇㙱㚅㙲㚁㙴㙾㙶㙽㙸㙺㙼㙹㙾㙶㚄㙵㚇㙳㚋㙲㚒㙱㚖㙱㚙㙱㚠㙱㚤㙲㚧㙴㚭㙷㚳㙹㚵㙼㚷㚁㚹㚄'+
'㚻㚍㚼㚐㚼㚗㚺㚚㚸㚞㚶㚧㚱㚰㚘㛍㚓㛑㚎㛒㚋㛒㚈㛒㚆㛑㚅㛏㚃㛍㚃㛈㚂㛇㚂㛄㚂㛄㚃㛂㚅㛀㚋㚿㚐㚾㚕㚽㚠㚺㚬㚹㚳㚷㚿㚵㛇㚴㛎㚲㛕㚱㛙㚫㛢㚪㛣㚫㛣㚬'+
'㛣㚪㛤㚨㛥㚦㛥㚡㛥㚞㛤㚙㛢㚕㛊㙻㛃㙵㚺㙱㚴㙰㚮㙰㚬㙱㚫㙴㚪㙶㚩㙼㚩㚀㚩㚅㚪㚎㚬㚗㚰㚨㚸㚱㚼㚺㚽㛍㛀㛘㛀㛠㛂㛰㛂㛴㛀㛿㚾㜄㚺㜇㚶㜋㚰㜍㚪㜎㚦㜏'+
'㚜㜏㚖㜎㚌㜋㚄㜉㙿㜉㙸㜉㙵㜂㙰㜀㙰㛿㙰㛽㙴㛼㙸㛺㚄㛷㚈㛳㚔㛯㚛㛫㚢㛤㚴㛜㚽㛏㛌㛇㛒㛂㛖㚺㛙㚴㛛㚯㛜㚪㛞㚤㛞㚡㛟㚠㛟㚜㛝㚙㛜㚔㛘㚒㛖㚌㛑㚇㛌㚅'+
'㛉㚂㛇㚁㛂㚁㛁㚂㚿㚅㚾㚆㚽㚇㚽㚉㚼㚍㚻㚏㚻㚒㚻㚕㚻㚖㚻㚙㚻㚚㚻㚛㚻');

$templateMouseClick:
mouse.click('',,);

$templateMouseClickAt:
mouse.clickAt('',,);
	
$templateMouseGetMoveDelay:
mouse.getMoveDelay();
	
$templateMouseGetPressDelay:
mouse.getPressDelay();

$templateMouseGetReleaseDelay:
mouse.getReleaseDelay();

$templateMouseGetX:
mouse.getX();

$templateMouseGetY:
mouse.getY();

$templateMouseMove:
mouse.move(,);

$templateMouseMoveAbsolute:
mouse.moveAbsolute('');

$templateMouseMoveAbsolute_D:
mouse.moveAbsolute_D('');

$templateMouseMoveAndClick:
mouse.moveAndClick('',,);

$templateMouseMoveAndPress:
mouse.moveAndPress('',,);

$templateMouseMoveAndRelease:
mouse.moveAndRelease('',,);

$templateMouseMoveRelative:
mouse.moveRelative('');

$templateMouseMoveRelative_D:
mouse.moveRelative_D('');

$templateMouseMoveTo:
mouse.moveTo(,);

$templateMousePress:
mouse.press('');

$templateMousePressAt:
mouse.pressAt('',,);

$templateMouseRelease:
mouse.release('');

$templateMouseReleaseAt:
mouse.releaseAt('',,);

$templateMouseSetMoveDelay:
mouse.setMoveDelay();

$templateMouseSetPressDelay:
mouse.setPressDelay();

$templateMouseSetReleaseDelay:
mouse.setReleaseDelay();

$templateMouseSetX:
mouse.setX();

$templateMouseSetY:
mouse.SetY();

$templateKeyboardGetPressDelay:
key.getPressDelay();

$templateKeyboardGetReleaseDelay:
key.getReleaseDelay();

$templateKeyboardPress:
key.press('');

$templateKeyboardRelease:
key.release('');

$templateKeyboardType:
key.type('');

$templateKeyboardSetPressDelay:
key.setPressDelay();

$templateKeyboardSetReleaseDelay:
key.setReleaseDelay();

$templateSystemSleep:
system.sleep();

$templateSystemPrint:
system.print('');

$templateSystemPrintln:
system.println('');

$templateSystemRegisterShortcut:
system.registerShortcut('','');

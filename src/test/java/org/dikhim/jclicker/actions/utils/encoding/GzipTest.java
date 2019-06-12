package org.dikhim.jclicker.actions.utils.encoding;

import org.dikhim.jclicker.util.Gzip;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Base64;

import static org.junit.Assert.*;

@Ignore
public class GzipTest {

    String string = "A㝙㘠D㕹A㝘㘠D㓄A㝘㘢D㓄A㝖㘦D㓄A㝕㘩" +
            "D㓅A㝒㘭D㓃A㝐㘴D㓄A㝍㘸D㓄A㝌㘼D㓄A㝉㙅D㓄A㝇㙈D㓄A㝆㙌D㓄A㝄㙕D㓄A㝃㙘D" +
            "㓄A㝀㙟D㓄A㝀㙤D㓄A㝀㙧D㓄A㝀㙯D㓄A㝀㙳D㓄A㜿㙸D㓄A㜿㚃D㓄A㜿㚅D㓄A㝀㚎D㓄" +
            "A㝂㚐D㓄A㝃㚓D㓄A㝆㚖D㓄A㝇㚗D㓄A㝊㚚D㓄A㝋㚜D㓄A㝍㚝D㓄A㝐㚝D㓄A㝒㚞D㓄A" +
            "㝕㚞D㓄A㝖㚞D㓄A㝘㚞D㓄A㝚㚝D㓄A㝜㚜D㓄A㝝㚛D㓄A㝟㚖D㓄A㝠㚔D㓄A㝠㚒D㓄A㝡" +
            "㚊D㓄A㝢㚇D㓄A㝢㚀D㓄A㝢㙾D㓄A㝢㙻D㓄A㝠㙲D㓄A㝞㙯D㓄A㝗㙥D㓄A㝔㙡D㓄A㝑㙝" +
            "D㓄A㝊㙕D㓄A㝇㙑D㓄A㝆㙏D㓄A㝅㙍D㓌A㝅㙌D㓔A㝅㙋D㓄A㝆㙋D㓄A㝇㙌D㓄A㝌㙌D" +
            "㓄A㝎㙍D㓄A㝑㙍D㓄A㝘㙎D㓄A㝛㙎D㓄A㝡㙏D㓄A㝣㙐D㓄A㝦㙐D㓄A㝧㙒D㓆A㝨㙒D㓂" +
            "A㝩㙕D㓄A㝩㙖D㓄A㝩㙗D㓄A㝩㙛D㓄A㝩㙝D㓄A㝨㙡D㓄A㝨㙣D㓄A㝨㙤D㓄A㝨㙨D㓄A" +
            "㝩㙩D㓄A㝪㙪D㓄A㝬㙬D㓄A㝮㙮D㓄A㝯㙯D㓄A㝲㙰D㓄A㝳㙰D㓄A㝷㙱D㓄A㝸㙱D㓄A㝹" +
            "㙱D㓄A㝹㙲D㓌A㝹㙳D㓄A㝸㙵D㓄A㝷㙶D㓄A㝶㙷D㓄A㝵㙺D㓄A㝴㙻D㓄A㝳㙿D㓄A㝳㚁" +
            "D㓄A㝳㚂D㓄A㝴㚄D㓄A㝵㚄D㓄A㝻㚆D㓄A㝽㚆D㓄A㞁㚆D㓄A㞋㚆D㓄A㞐㚇D㓄A㞚㚇D" +
            "㓄A㞞㚇D㓄A㞟㚇D㓄A㞡㚆D㓌A㞡㚅D㓄A㞠㚄D㓄A㞞㚃D㓄A㞜㚂D㓄A㞚㚁D㓄A㞙㚀D㓄" +
            "A㞙㙿D㓄A㞙㙾D㓅A㞚㙽D㓃A㞝㙺D㓄A㞞㙹D㓄A㞡㙷D㓄A㞧㙳D㓄A㞩㙱D㓄A㞮㙮D㓄A" +
            "㞰㙬D㓄A㞲㙩D㓄A㞴㙤D㓄A㞵㙢D㓄A㞵㙞D㓄A㞵㙛D㓄A㞶㙘D㓄A㞷㙕D㓄A㞸㙓D㓄A㞸" +
            "㙒D㓄A㞻㙒D㓄A㞽㙒D㓄A㞾㙒D㓄A㟁㙓D㓄A㟄㙕D㓄A㟋㙙D㓄A㟎㙜D㓄A㟑㙞D㓄A㟗㙠" +
            "D㓄A㟚㙡D㓄A㟟㙤D㓄A㟡㙥D㓄A㟤㙦D㓄A㟧㙨D㓄A㟨㙨D㓄A㟫㙪D㓄A㟫㙬D㓌A㟭㙮D" +
            "㓄A㟯㙯D㓄A㟰㙲D㓄A㟳㙶D㓄A㟴㙹D㓄A㟶㙻D㓄A㟸㚀D㓄A㟺㚃D㓄A㟽㚉D㓄A㟾㚋D㓄" +
            "A㟿㚎D㓄A㠁㚒D㓄A㠂㚔D㓄A㠅㚖D㓄A㠉㚛D㓄A㠌㚝D㓄A㠏㚟D㓄A㠖㚢D㓄A㠙㚢D㓄A" +
            "㠠㚣D㓄A㠤㚣D㓄A㠧㚣D㓄A㠯㚣D㓄A㠳㚢D㓄A㠶㚡D㓄A㠼㚞D㓄A㠾㚝D㓄A㡆㚙D㓄A㡉" +
            "㚖D㓄A㡎㚓D㓄A㡗㚍D㓄A㡜㚈D㓄A㡣㚀D㓄A㡦㙼D㓄A㡪㙷D㓄A㡮㙰D㓄A㡯㙭D㓄A㡰㙩" +
            "D㓄A㡲㙡D㓅A㡳㙝D㓃A㡵㙗D㓄A㡷㙓D㓄A㡷㙏D㓄A㡹㙊D㓄A㡻㙆D㓄A㡽㙂D㓄A㢁㘹D" +
            "㓄A㢃㘶D㓄A㢉㘬D㓄A㢍㘧D㓄A㢑㘢D㓄A㢔㘚D㓄A㢗㘖D㓄A㢛㘏D㓄A㢝㘋D㓄A㢡㘇D㓄" +
            "A㢧㗿D㓄A㢭㗻D㓄A㢱㗷D㓄A㢹㗯D㓄A㢻㗫D㓄A㣁㗥D㓄A㣅㗢D㓅A㣇㗞D㓃A㣎㗙D㓄A" +
            "㣑㗔D㓄A㣕㗐D㓄A㣞㗇D㓄A㣣㗂D㓄A㣩㖾D㓄A㣵㖳D㓄A㣺㖮D㓄A㤄㖤D㓄A㤊㖡D㓄A㤍" +
            "㖛D㓄A㤒㖔D㓄A㤔㖐D㓄A㤖㖋D㓄A㤖㖈D㓄A㤖㖆D㓄A㤖㖂D㓄A㤖㖁D㓄A㤖㕼D㓄A㤕㕺" +
            "D㓄A㤔㕹D㓄A㤔㕵D㓄A㤓㕳D㓄A㤒㕱D㓄A㤑㕯D㓄A㤑㕮D㓄A㤐㕭D㓄A㤐㕬D㓄A㤏㕫D" +
            "㓄A㤏㕪D㔌A㤏㕩D㓄A㤏㕨D㓄A㤏㕧D㓍A㤎㕥D㓋A㤎㕤D㓄A㤎㕡D㓄A㤎㕠D㓄A㤎㕞D㓄" +
            "A㤎㕜D㓄A㤎㕛D㓄A㤏㕙D㓄A㤏㕗D㓄A㤐㕕D㓌A㤐㕔D㔬A㤏㕔D㓔A㤏㕒D㓄A㤎㕒D㓄A" +
            "㤎㕔D㓄A㤍㕕D㓅A㤌㕕D㒼lD㓃A㤋㕗D㓄A㤊㕙D㓄A㤉㕚D㓅A㤆㕝D㓆A㤅㕝D㓁A㤃㕟D" +
            "㓄A㤀㕢D㓅A㣿㕤D㓄A㣻㕧D㓄A㣺㕨D㓄A㣷㕪D㓄A㣴㕭D㓄A㣳㕯D㓄A㣱㕱D㓄A㣭㕶D㓄" +
            "A㣪㕸D㓄A㣥㕽D㓄A㣣㖀D㓄A㣠㖃D㓄A㣜㖇D㓄A㣙㖊D㓄A㣖㖎D㓄A㣕㖏D㓅A㣓㖐D㓃A" +
            "㣐㖔D㓄A㣎㖕D㓄A㣇㖚D㓄A㣃㖝D㓄A㢽㖟D㓃A㢪㖥D㓅A㢞㖦D㓄A㢑㖨D㓃A㡮㖪D㓅A㡚" +
            "㖬D㓄A㡊㖬D㓃A㠧㖬D㓅A㠗㖬D㓄A㠁㖨D㓃A㟼㖤D㓅A㟶㖣D㓄A㟵㖞D㓄A㟵㖜D㓄A㟵㖙" +
            "D㓄A㟵㖗D㓄A㟵㖕D㓄A㟵㖒D㓄A㟶㖑D㓄A㟷㖐D㓄A㟹㖏D㓃A㟺㖏D㓅A㟿㖏D㓄A㠂㖐D" +
            "㓄A㠆㖑D㓄A㠎㖗D㓄A㠓㖛D㓃A㠘㖟D㓅A㠥㖩D㓄A㠬㖰D㓄A㠵㖸D㓄A㡊㗉D㓄A㡔㗎D㓃" +
            "A㡯㗜D㓅A㡽㗢D㓃A㢊㗩D㓅A㢪㗲D㓄A㢻㗶D㓄A㣗㗺D㓄A㣣㗺D㓄A㣱㗺D㓄A㤇㗶D㓄A" +
            "㤓㗲D㓄A㤧㗤D㓄A㤮㗝D㓄A㤴㗖D㓃A㥀㗈D㓅A㥆㖾D㓄A㥋㖶D㓄A㥕㖢D㓃A㥘㖚D㓅A㥜" +
            "㖋D㓄A㥜㖄D㓄A㥜㖀D㓄A㥜㕷D㓄A㥚㕴D㓄A㥙㕱D㓄A㥔㕮D㓄A㥑㕭D㓄A㥋㕬D㓄A㥇㕪" +
            "D㓄A㥃㕪D㓄A㤼㕭D㓄A㤸㕯D㓄A㤮㕳D㓄A㤩㕵D㓄A㤛㕻D㓄A㤔㕿D㓄A㤅㖉D㓄A㣽㖏D" +
            "㓄A㣶㖓D㓄A㣨㖟D㓄A㣡㖣D㓄A㣜㖦D㓄A㣕㖩D㓄A㣐㖫D㓄A㣌㖫D㓄A㣈㖨D㓄A㢻㖢D㓄" +
            "A㢵㖞D㓄A㢬㖚D㓄A㢞㖏D㓄A㢕㖋D㓃A㢎㖆D㓅A㢆㕻D㓄A㢆㕶D㓄A㢅㕯D㓄A㢆㕫D㓄A" +
            "㢋㕧D㓄A㢘㕢D㓄A㢣㕠D㓄A㢯㕟D㓄A㣒㕝D㓄A㣢㕝D㓄A㤈㕟D㓄A㤚㕡D㓄A㤪㕥D㓄A㥈" +
            "㕫D㓄A㥜㕱D㓄A㥤㕳D㓄A㥦㕺D㓄A㥩㖁D㓄A㥪㖅D㓄A㥭㖉D㓄A㥱㖏D㓄A㥴㖒D㓄A㥼㖘" +
            "D㓄A㦀㖛D㓄A㦇㖜D㓄A㦔㖟D㓃A㦝㖟D㓅A㦧㖟D㓄A㦼㖜D㓄A㧅㖚D㓄A㧗㖖D㓃A㧟㖑D" +
            "㓅A㧧㖎D㓄A㧰㖆D㓄A㧳㖁D㓄A㧶㕿D㓅A㧸㕹D㓃A㧷㕴D㓄A㧵㕲D㓄A㧰㕯D㓄A㧬㕮D㓄" +
            "A㧣㕪D㓄A㧞㕨D㓃A㧚㕧D㓅A㧔㕥D㕻LD㓼A㧒㕥D㓄A㧍㕨D㓄A㧊㕪D㓄A㧆㕬D㓄A㦾㕲" +
            "D㓅A㦲㕹D㓃A㦭㕼D㓄A㦩㕿D㓄A㦚㖈D㓄A㦒㖍D㓄A㥺㖚D㓄A㥩㖥D㓄A㥙㖰D㓄A㤶㗉D" +
            "㓄A㤢㗗D㓅A㤏㗥D㓃A㣭㗽D㓄A㣟㘉D㓄A㣑㘓D㓄A㢽㘩D㓄A㢷㘳D㓄A㢭㙃D㓄A㢩㙉D㓄" +
            "A㢧㙓D㓄A㢨㙘D㓄A㢪㙚D㓄A㢬㙞D㓄A㢯㙠D㓄A㢴㙣D㓄A㢷㙥D㓄A㢼㙨D㓄A㣊㙬D㓄A" +
            "㣓㙮D㓄A㣨㙱D㓄A㣳㙴D㓄A㣽㙸D㓄A㤍㙽D㓄A㤞㙾D㓄A㤢㚀D㓄A㤧㚀D㓌A㤣㙿D㓄A㤠" +
            "㙽D㓄A㤔㙹D㓄A㤉㙶D㓄A㣻㙳D㓄A㣘㙫D㓈A㣆㙩D㓀A㢜㙥D㓄A㢊㙡D㓄A㡺㙟D㓄A㡦㙝" +
            "D㓌A㡭㙚D㓄A㡳㙘D㓄A㢀㙘D㓄A㢇㙘D㓄A㢖㙘D㓄A㢠㙖D㓄A㢴㙕D㓄A㢾㙕D㓄A㣆㙕D" +
            "㓄A㣑㙕D㓌A㣐㙕D㓄A㣍㙕D㓄A㣉㙕D㓄A㣀㙕D㓄A㢯㙕D㓄A㢥㙕D㓄A㢈㙕D㓄A㡺㙕D㓄" +
            "A㡮㙕D㓄A㡝㙕D㓄A㡘㙕D㓄A㡚㙕D㓄A㡡㙖D㓄A㡨㙗D㓄A㡰㙙D㓄A㢅㙜D㓄A㢔㙝D㓄A" +
            "㢡㙟D㓄A㢹㙤D㓄A㣂㙦D㓄A㣉㙩D㓆A㣉㙬D㓂A㣅㙰D㓄A㣁㙶D㓄A㢽㙼D㓄A㢱㚉D㓄A㢫" +
            "㚔D㓄A㢨㚛D㓄A㢣㚢D㓄A㢛㚲D㓄A㢗㚹D㓄A㢒㛈D㓄A㢐㛏D㓄A㢐㛙D㓄A㢐㛨D㓄A㢐㛯" +
            "D㓄A㢒㛷D㓄A㢖㜇D㓄A㢘㜏D㓄A㢛㜔D㓄A㢟㜟D㓄A㢢㜣D㓄A㢤㜣D㓬A㢤㜢D㓄A㢢㜞D" +
            "㓄A㢚㜙D㓄A㢔㜓D㓄A㢅㜆D㓅A㡽㛾D㓃A㡳㛴D㓄A㡟㛣D㓄A㡔㛖D㓄A㡄㛂D㓄A㠼㚷D㓄" +
            "A㠷㚰D㓄A㠱㚢D㓄A㠱㚟D㓄A㠬㚘D㓄A㠫㚔D㓄A㠭㚓D㓄A㠮㚒D㓄A㠴㚒D㓄A㠸㚒D㓄A" +
            "㡃㚗D㓄A㡊㚚D㓄A㡔㚝D㓄A㡚㚠D㓄A㡨㚨D㓄A㡯㚬D㓄A㡸㚱D㓄A㢈㚹D㓄A㢑㚾D㓄A㢘" +
            "㛁D㓄A㢦㛅D㓄A㢭㛇D㓄A㢶㛇D㓄A㢽㛇D㓄A㣁㛇D㓄A㣆㛄D㓄A㣇㛂D㓄A㣈㛀D㓄A㣋㚼" +
            "D㓄A㣋㚺D㓄A㣍㚶D㓄A㣎㚲D㓄A㣎㚯D㓄A㣏㚫D㓄A㣐㚧D㓄A㣑㚢D㓄A㣑㚟D㓄A㣑㚛D" +
            "㓄A㣑㚙D㓄A㣐㚗D㓄A㣎㚖D㓄A㣍㚖D㓄A㣌㚖D㓄A㣋㚘D㓄A㣋㚛D㓄A㣋㚝D㓄A㣋㚣D㓄" +
            "A㣍㚬D㓄A㣏㚲D㓄A㣑㚹D㓄A㣖㛈D㓄A㣙㛏D㓄A㣡㛛D㓄A㣦㛣D㓄A㣪㛧D㓄A㣵㛯D㓄A" +
            "㣹㛲D㓄A㤅㛶D㓄A㤊㛶D㓄A㤏㛶D㓄A㤘㛶D㓄A㤜㛵D㓄A㤠㛲D㓄A㤩㛪D㓄A㤭㛥D㓄A㤱" +
            "㛟D㓄A㤷㛖D㓄A㤹㛒D㓄A㤽㛌D㓄A㤽㛊D㓄A㤿㛉D㓬A㤿㛊D㓄A㥀㛏D㓄A㥃㛔D㓄A㥆㛕" +
            "D㓄A㥈㛗D㓄A㥍㛘D㓄A㥐㛘D㓄A㥓㛘D㓄A㥕㛗D㓄A㥖㛕D㓄A㥗㛓D㓄A㥗㛒D㓆A㥘㛏D" +
            "㓂A㥘㛎D㓌A㥘㛍D㓬A㥘㛏D㓄A㥙㛘D㓄A㥛㛥D㓄A㥝㛯D㓄A㥞㛷D㓄A㥡㛽D㓄A㥩㜁D㓄" +
            "A㥭㜂D㓄A㥱㜅D㓄A㥻㜆D㓄A㦁㜆D㓄A㦆㜆D㓄A㦑㜆D㓄A㦖㜃D㓄A㦛㜂D㓄A㦡㜀D㓄A" +
            "㦡㛼D㓄A㦡㛺D㓄A㦡㛹D㓌A㦠㛹D㓄A㦟㛹D㓄A㦝㛻D㓄A㦜㛾D㓄A㦚㜁D㓄A㦘㜊D㓄A㦗" +
            "㜑D㓄A㦕㜝D㓄A㦕㜢D㓄A㦕㜦D㓄A㦔㜬D㓄A㦔㜰D㓄A㦖㜱D㓄A㦚㜱D㓄A㦝㜱D㓄A㦡㜱" +
            "D㓄A㦧㜰D㓄A㦩㜯D㓄A㦬㜭D㓄A㦭㜬D㓌A㦭㜫D㓌A㦬㜫D㓄A㦪㜫D㓄A㦩㜫D㓄A㦧㜰D" +
            "㓄A㦥㜲D㓄A㦡㜻D㓄A㦟㜿D㓄A㦛㝅D㓄A㦓㝑D㓄A㦍㝕D㓄A㦂㝠D㓄A㥼㝤D㓄A㥰㝪D㓄" +
            "A㥨㝭D㓄A㥟㝭D㓄A㥊㝭D㓄A㥀㝭D㓄A㤸㝭D㓄A㤨㝭D㓄A㤠㝫D㓄A㤕㝫D㓌A㤕㝪D㓄A" +
            "㤔㝪D㓄A㤔㝩D㓄A㤓㝩D㓌A㤒㝩D㓄A㤑㝩D㓄A㤐㝩D㓄A㤍㝩D㓄A㤋㝪D㓄A㤅㝬D㓄A㤁" +
            "㝭D㓄A㣽㝮D㓄A㣴㝱D㓄A㣰㝲D㓄A㣫㝴D㓄A㣢㝶D㓄A㣜㝶D㓄A㣒㝶D㓄A㣋㝶D㓄A㣅㝶" +
            "D㓄A㢶㝶D㓄A㢯㝳D㓄A㢥㝳D㓄A㢕㝱D㓄A㢌㝯D㓄A㢃㝬D㓄A㡴㝨D㓄A㡬㝦D㓄A㡡㝤D" +
            "㓄A㡝㝤D㓄A㡘㝤D㓄A㡒㝤D㓄A㡏㝤D㓄A㡈㝩D㓄A㡆㝫D㓄A㡄㝬D㓄A㡀㝱D㓄A㠽㝲D㓄" +
            "A㠻㝵D㓄A㠹㝷D㓄A㠸㝷D㓄A㠵㝸D㓄A㠴㝸D㓄A㠳㝸D㓄A㠲㝸D㓄A㠱㝷D㓄A㠱㝶D㓄A" +
            "㠮㝳D㓄A㠬㝲D㓄A㠩㝮D㓆A㠨㝬D㓂A㠦㝪D㓄A㠢㝥D㓄A㠠㝣D㓄A㠞㝠D㓄A㠛㝚D㓄A㠚" +
            "㝘D㓄A㠚㝒D㓄A㠚㝎D㓄A㠛㝌D㓄A㠝㝅D㓄A㠞㝂D㓄A㠡㜻D㓄A㠥㜸D㓄A㠦㜴";

    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void compress_Decompress_Equality() {
        try {
            byte[] compresed = Gzip.compressString(string);
            StringBuilder sb = new StringBuilder();

            String out = Gzip.decompressString(compresed);
            assertEquals(out,string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
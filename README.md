# Useless Auto Clicker
Main repo for the Useless Auto Clicker  
The java app that helps you to automate work with mouse, keyboard, clipboard and other things. Works on Mac, Unix-like, Windows operating systems.

Download the latest version on SourceForge [Useless Clicker](https://sourceforge.net/projects/uselessclicker/)

If you want to help me with translation or spellcheck, here the links  
[Documentation](https://github.com/dikhim/uselessclicker/tree/master/src/main/resources/docs/data)  
[Internationalization](https://github.com/dikhim/uselessclicker/tree/master/src/main/resources/i18n)  

If you find a bug, want to bring out new idea or need help, open a new issue on GitHub [Isuues](https://github.com/dikhim/uselessclicker/issues) or send an email directly to me dikhims@gmail.com with the theme "uselessclicker". 

# For interested Android/IOS developers and someone who know JavaScript O_o

If you want (and can) create open source, free, no ads aplication for your portfolio or just for fun, I have what to offer to you.  
In this application there are two built in servers HTTP-server and SOCKET-server. I created them so someone can create your own application and control the computer via HTTP requests or directly via sockets.  
For android developers I managed to create time application, that shows the possibilities [Useless Remote](https://github.com/dikhim/uselessremote). 
That app have 3 buttons, each for the corresponding mouse button and wheel, and some areas to fill the ip addres of the socket server. Just open it, set the IP address of the server and you can controll you computer with you phone. And it works pretty fine. 
So, use it as an example how to create your own application.  

*If are you the One who want to deal with sockets, I'll sent you detailed API documentation (not complete yet :P)*

If you know JavaScript or any other language, you can create JS library to send HTTP requests and controll the computer from anywere. I am no sure if JS can work with sockets, so I didn not included here that idea

# Application Core
The core for the application is [JCLickAuto](https://github.com/dikhim/jclickauto). Java library to control PC from java code. Contains the same features as Useless Clicker, except GUI. So, no recording, no servers, no built indocumentation, no keyboard or mouse events capturing. Just and engine and pair of objects that help you to write your... integration tests?  

<sup>The developer don't take any responsibilies for how you use that application or how it may end up. Use it on your own risk. May cause strong addiction to automate everithing you can, even if it'd be much quicker just to do it mannually. I warned you<sup>

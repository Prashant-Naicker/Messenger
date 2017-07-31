# Messenger

## Description 
Allow multple users to join a chat room with HTTPS and send messages to one another over TCP.
First client will be an android app,  but will expand to other clients. 

## Setup
### Server:
1.  Install Go.
2.  Make an enviroment variable called GOROOT and set that to the folder you installed go in.
3.  Make another environment variable called GOPATH and set that to the Messenger folder of this repository.
4.  Open CMD.
5.  Move to the "../Messenger/bin" directory in CMD.
6.  run golang.exe in CMD to start the server.

### Android Application:
1.  Connect your android phone to your computer via USB.
2.  In file explorer move to "../Messenger/src/golang/crt".
3.  Copy "root.crt" over onto your phone into a folder you can access from your phone.
4.  In file explorer move to "../Messenger/src/android/Messenger/app/build/outputs/apk".
5.  Copy the "app_debug.apk" file on to your phone, into any folder you can access.
6.  On your phone, find the "root.crt" file and open it to install the certificate.
7.  Go through the install process for the certificate.
8.  Once the certificate is installed, find the "app_debug.apk" file on your phone.
9.  Open the file to install the application, ignore any warnings.
10. Make sure the server is running from your PC.
11. Open the Application.

### .NET Applciation:
(Comming Soon)

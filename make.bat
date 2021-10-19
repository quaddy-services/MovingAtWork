echo on
echo %date% >src/version.txt
call mvn clean install
if %errorlevel% == 1 goto ende
if not exist target\MovingAtWork-SNAPSHOT.jar goto ende
echo on
copy target\MovingAtWork-SNAPSHOT.jar deploy\MovingAtWork.jar /y
cd deploy
"C:\Program Files\Java\jdk1.6.0_07\bin\jarsigner.exe" -keystore myKeystore -storepass quaddy MovingAtWork.jar Quaddy-Services.de
cd ..
pause
:ende
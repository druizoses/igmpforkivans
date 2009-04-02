REM Adapte este archivo a la confugración de su equipo
@echo off
cls
del *.class /s >NUL

set CLASSPATH=.;Simulador.jar;%CLASSPATH%;

echo Compilando Visual...
"C:\Archivos de programa\Java\jdk1.6.0\bin\javac" visual.java
pause
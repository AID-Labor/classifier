@echo off
rem ---- Benoetigte Informationen in Variablen speichern ----------------------------------------------
rem Mit den folgeneden Variablen koennen die Grundlegenden Daten fuer das Projekt eingestellt werden:
set NAME=Classifier
set DESCRIPTION=UML-Klassenmodellierung
rem Version muss groesser gleich 1.0.0 sein!
set VERSION=1.1.2
set VENDOR="Tim M�hle"
set COPYRIGHT="Copyright � 2022 - Tim M�hle"
set LICENSE_FILE=LICENSE.txt

rem Einstellungen fuer jpackage:
set INPUT="auslieferung\%VERSION%\Windows\input"
set OUT="auslieferung\%VERSION%\Windows\app"
set MODULE_PATH="%INPUT%\lib"
set MAIN_JAR="%INPUT%\classifier-%VERSION%.jar"
set MAIN_MODULE=classifier
set MAIN_CLASS=io.github.aid_labor.classifier.main.Hauptfenster
set JAVA_OPTIONEN="--add-opens javafx.graphics/javafx.scene=org.controlsfx.controls"

rem Weitere Befehle fuer jpackage:
rem App Icon aendern: --icon "path/to/icon.png"
set ICON=src\app\ressourcen\Icon.ico

rem Systemspezifische Optionen
set WIN_MENU_GROUP="Hochschule Bochum"

rem ---- Eingabeordner leeren ---------------------------------------------------------------------------
if exist %INPUT% del /Q %INPUT%
if exist %OUT% del /Q %OUT%

rem ---- Maven build ----------------------------------------------------------------------------------
echo.
echo Maven build durchfuehren
echo.
call mvn clean install

rem ---- Installer erzeugen ---------------------------------------------------------------------------
rem ---- app-image ----
echo.
echo App-Image wird erstellt
echo.
@echo on
jpackage ^
--type app-image ^
--name %NAME% ^
--description %DESCRIPTION% ^
--app-version %VERSION% ^
--vendor %VENDOR% ^
--copyright %COPYRIGHT% ^
--icon %ICON% ^
--input %INPUT% ^
--dest %OUT% ^
--module-path %MODULE_PATH% ^
--module-path %MAIN_JAR% ^
--module %MAIN_MODULE%/%MAIN_CLASS% ^
--java-options %JAVA_OPTIONEN%
@echo off
echo.
echo Installer fuer Windows werden erzeugt.
echo.

rem ---- exe ----
echo %NAME%-%VERSION%-win-install.exe wird erstellt
echo.
@echo on
jpackage ^
--type exe ^
--name %NAME% ^
--description %DESCRIPTION% ^
--app-version %VERSION% ^
--vendor %VENDOR% ^
--copyright %COPYRIGHT% ^
--license-file %LICENSE_FILE% ^
--file-associations classifier_windows.association ^
--icon %ICON% ^
--input %INPUT% ^
--dest %OUT% ^
--module-path %MODULE_PATH% ^
--module-path %MAIN_JAR% ^
--module %MAIN_MODULE%/%MAIN_CLASS% ^
--java-options %JAVA_OPTIONEN% ^
--win-dir-chooser ^
--win-shortcut ^
--win-menu ^
--win-menu-group %WIN_MENU_GROUP%
@echo off

ren %OUT%\%NAME%-%VERSION%.exe %NAME%-%VERSION%-windows-install.exe
echo.
echo.

rem ---- msi ----
echo %NAME%-%VERSION%-win-install.msi wird erstellt
echo.
@echo on
jpackage ^
--type msi ^
--name %NAME% ^
--description %DESCRIPTION% ^
--app-version %VERSION% ^
--vendor %VENDOR% ^
--copyright %COPYRIGHT% ^
--license-file %LICENSE_FILE% ^
--file-associations classifier_windows.association ^
--icon %ICON% ^
--input %INPUT% ^
--dest %OUT% ^
--module-path %MODULE_PATH% ^
--module-path %MAIN_JAR% ^
--module %MAIN_MODULE%/%MAIN_CLASS% ^
--java-options %JAVA_OPTIONEN% ^
--win-dir-chooser ^
--win-shortcut ^
--win-menu ^
--win-menu-group %WIN_MENU_GROUP%
@echo off

ren %OUT%\%NAME%-%VERSION%.msi %NAME%-%VERSION%-windows-install.msi
echo.
echo.

rem ---- Auf Bestaetigung von Benutzer warten ---------------------------------------------------------
echo "Zum Abschliessen eine beliebige Taste druecken"
pause
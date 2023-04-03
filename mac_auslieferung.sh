#!/bin/sh
cd "$(dirname "$0")" # zum Pfad dieses Skriptes wechseln

# ---- Benoetigte Informationen in Variablen speichern ------------------------------------------------
# Mit den folgeneden Variablen koennen die Grundlegenden Daten fuer das Projekt # eingestellt werden:
NAME="Classifier"
DESCRIPTION="UML-Klassenmodellierung"
# Version muss groesser gleich 1.0.0 sein!
VERSION="1.3.1"
VENDOR="Tim Mühle"
COPYRIGHT="Copyright © 2022 - Tim Mühle"
LICENSE_FILE="LICENSE.txt"

# Einstellungen fuer jpackage:
INPUT="auslieferung/${VERSION}/macOS/input"
OUT="auslieferung/${VERSION}/macOS/app"
MODULE_PATH="${INPUT}/lib"
MAIN_MODULE="classifier"
MAIN_CLASS="io.github.aid_labor.classifier.main.Hauptfenster"
JAVA_OPTIONEN="--add-opens javafx.graphics/javafx.scene=org.controlsfx.controls"

# Weitere Befehle fuer jpackage:
# App Icon aendern: --icon "path/to/icon.png"
ICON="src/app/ressourcen/Icon.icns"

# Systemspezifische Optionen
# Paketname darf maximal 16 Zeichen haben!
MAC_PACKAGE_NAME="Classifier"
# weltweit eindeutiger Paketidentifier
MAC_PACKAGE_ID="io.github.aid-labor.classifier"

# ---- Eingabeordner leeren ---------------------------------------------------------------------------
mkdir -p ${INPUT}
rm -rf ${INPUT}/*
mkdir -p ${OUT}
rm -rf ${OUT}/*

# ---- Maven build ------------------------------------------------------------------------------------
echo ""
echo "Maven build durchfuehren" 
echo ""
mvn clean install

# ---- Installer erzeugen -----------------------------------------------------------------------------
echo ""
echo "Installer fuer macOS werden erzeugt."
echo ""

echo "AppImage wird erstellt"
echo ""

jpackage \
--type "app-image" \
--name "${NAME}" \
--description "${DESCRIPTION}" \
--app-version "${VERSION}" \
--vendor "${VENDOR}" \
--copyright "${COPYRIGHT}" \
--icon "${ICON}" \
--input "${INPUT}" \
--dest "${OUT}" \
--module-path "${MODULE_PATH}" \
--module-path "${INPUT}" \
--module "${MAIN_MODULE}/${MAIN_CLASS}" \
--java-options "${JAVA_OPTIONEN}" \
--mac-package-name "${MAC_PACKAGE_NAME}" \
--mac-package-identifier "${MAC_PACKAGE_ID}"

echo ""

echo "${NAME}-${VERSION}.pkg wird erstellt"
echo ""

jpackage \
--type "pkg" \
--name "${NAME}" \
--description "${DESCRIPTION}" \
--app-version "${VERSION}" \
--vendor "${VENDOR}" \
--copyright "${COPYRIGHT}" \
--license-file "${LICENSE_FILE}" \
--file-associations classifier_mac.association \
--icon "${ICON}" \
--input "${INPUT}" \
--dest "${OUT}" \
--module-path "${MODULE_PATH}" \
--module-path "${INPUT}/${MAIN_JAR}" \
--module "${MAIN_MODULE}/${MAIN_CLASS}" \
--java-options "${JAVA_OPTIONEN}" \
--mac-package-name "${MAC_PACKAGE_NAME}" \
--mac-package-identifier "${MAC_PACKAGE_ID}"

echo ""

mv "./${OUT}/${NAME}-${VERSION}.pkg" "./${OUT}/${NAME}-${VERSION}-mac-install.pkg"
echo ""
echo ""
echo "Der Prozess wurde beendet."
echo ""
#!/bin/sh
cd "$(dirname "$0")" # zum Pfad dieses Skriptes wechseln

# ---- Benoetigte Informationen in Variablen speichern ------------------------------------------------
# Mit den folgeneden Variablen koennen die Grundlegenden Daten fuer das Projekt # eingestellt werden:
NAME="Classifier"
DESCRIPTION="UML-Klassenmodellierung"
# Version muss groesser gleich 1.0.0 sein!
VERSION="1.0.0"
VENDOR="Tim Mühle"
COPYRIGHT="Copyright © 2022 - Tim Mühle"
LICENSE_FILE="LICENSE.txt"

# Einstellungen fuer jpackage:
INPUT="auslieferung/${VERSION}/Linux/input"
OUT="auslieferung/${VERSION}/Linux/app/deb"
MODULE_PATH="${INPUT}/lib"
MAIN_MODULE="classifier"
MAIN_CLASS="io.github.aid_labor.classifier.main.Hauptfenster"

# Weitere Befehle fuer jpackage:
# App Icon aendern: --icon "path/to/icon.png"
ICON="src/app/ressourcen/Icon.png"

# Systemspezifische Optionen
LINUX_MENU_GROUP="Development;Education;"
LINUX_RPM_LICENSE_TYPE="MIT License"

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
echo "Installer fuer Linux werden erzeugt."
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
--module "${MAIN_MODULE}/${MAIN_CLASS}"

echo ""

echo "${NAME}-${VERSION}.deb wird erstellt"
echo ""

jpackage \
--type "deb" \
--name "${NAME}" \
--description "${DESCRIPTION}" \
--app-version "${VERSION}" \
--vendor "${VENDOR}" \
--copyright "${COPYRIGHT}" \
--license-file "${LICENSE_FILE}" \
--icon "${ICON}" \
--input "${INPUT}" \
--dest "${OUT}" \
--module-path "${MODULE_PATH}" \
--module-path "${INPUT}/${MAIN_JAR}" \
--module "${MAIN_MODULE}/${MAIN_CLASS}" \
--linux-menu-group "${LINUX_MENU_GROUP}" \
--linux-shortcut \
--linux-rpm-license-type "${LINUX_RPM_LICENSE_TYPE}"

echo ""

mv "./${OUT}/classifier-${VERSION}-1_amd64.deb" "./${OUT}/${NAME}-${VERSION}-linux-deb.deb"
echo ""
echo ""
echo "Der Prozess wurde beendet."
echo ""
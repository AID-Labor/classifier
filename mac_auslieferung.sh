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
INPUT="auslieferung/${VERSION}/Mac OS X/input"
OUT="auslieferung/${VERSION}/Mac OS X"
MODULE_PATH="${INPUT}/lib"
MAIN_JAR=""
MAIN_MODULE="classifier"
MAIN_CLASS="io.github.aid_labor.classifier.main.Hauptfenster"

# Weitere Befehle fuer jpackage:
# App Icon aendern: --icon "path/to/icon.png"
ICON="src/app/ressourcen/Icon.icns"

# Systemspezifische Optionen
# Paketname darf maximal 16 Zeichen haben!
MAC_PACKAGE_NAME="Classifier"
# weltweit eindeutiger Paketidentifier
MAC_PACKAGE_ID="io.github.aid-labor.classifier"

# ---- Eingabeordner leeren ---------------------------------------------------------------------------
mkdir -p $INPUT
rm -r -f $INPUT/*

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
--module-path "${INPUT}/${MAIN_JAR}" \
--module "${MAIN_MODULE}/${MAIN_CLASS}" \
--mac-package-name "${MAC_PACKAGE_NAME}" \
--mac-package-identifier "${MAC_PACKAGE_ID}"

echo ""

echo "${NAME-$VERSION}.pkg wird erstellt"
echo ""

jpackage \
--type "pkg" \
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
--mac-package-name "${MAC_PACKAGE_NAME}" \
--mac-package-identifier "${MAC_PACKAGE_ID}"

echo ""

mv "./${OUT}/${NAME}-${VERSION}.pkg" "./${OUT}/${NAME}-${VERSION}-mac-install.pkg"
echo ""
echo ""
echo "Der Prozess wurde beendet."
echo ""
#!/bin/sh
cd "$(dirname "$0")" # zum Pfad dieses Skriptes wechseln
PROJEKT_PFAD=$(dirname "$0")

# ---- Benoetigte Informationen in Variablen speichern ------------------------------------------------
# Mit den folgeneden Variablen koennen die Grundlegenden Daten fuer das Projekt # eingestellt werden:
NAME="Classifier"
NAME_KLEIN="classifier"
DESCRIPTION="UML-Klassenmodellierung"
# Version muss groesser gleich 1.0.0 sein!
VERSION="1.0.3"
VENDOR="Tim Mühle"
COPYRIGHT="Copyright © 2022 - Tim Mühle"
LICENSE_FILE="LICENSE.txt"

# Einstellungen fuer jpackage:
INPUT="auslieferung/${VERSION}/Linux/input"
OUT="auslieferung/${VERSION}/Linux/rpm"
MODULE_PATH="${INPUT}/lib"
MAIN_MODULE="classifier"
MAIN_CLASS="io.github.aid_labor.classifier.main.Hauptfenster"
JAVA_OPTIONEN="--add-opens javafx.graphics/javafx.scene=org.controlsfx.controls"

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
--module "${MAIN_MODULE}/${MAIN_CLASS}" \
--java-options "${JAVA_OPTIONEN}"

echo ""

echo "${NAME}-${VERSION}.rpm wird erstellt"

jpackage \
--type "rpm" \
--name "${NAME}" \
--description "${DESCRIPTION}" \
--app-version "${VERSION}" \
--vendor "${VENDOR}" \
--copyright "${COPYRIGHT}" \
--license-file "${LICENSE_FILE}" \
--file-associations classifier_linux.association \
--icon "${ICON}" \
--input "${INPUT}" \
--dest "${OUT}" \
--module-path "${MODULE_PATH}" \
--module-path "${INPUT}/${MAIN_JAR}" \
--module "${MAIN_MODULE}/${MAIN_CLASS}" \
--java-options "${JAVA_OPTIONEN}" \
--linux-menu-group "${LINUX_MENU_GROUP}" \
--linux-shortcut \
--linux-rpm-license-type "${LINUX_RPM_LICENSE_TYPE}"

echo ""
mv "./${OUT}/classifier-${VERSION}-1.x86_64.rpm" "./${OUT}/${NAME}-${VERSION}-linux.rpm"

echo "Entpacke App nach ./${OUT}/${NAME}-${VERSION}"
cd "./${OUT}"
mkdir "${NAME}-${VERSION}"
rpm2cpio "${NAME}-${VERSION}-linux.rpm" | (cd "${NAME}-${VERSION}" && cpio -idm)
echo ""
echo "Erzeuge Installer-Skript"
echo "#!/bin/sh
if [ \$(/usr/bin/id -u) -ne 0 ]; then
    echo \"root permissions required, please retry wiht sudo or as root\"
    exit
fi

chmod +x ./opt/*/bin/*
cp -r --copy-contents ./opt/* /opt/
mkdir --parents /usr/share/applications
cp --copy-contents ./opt/*/lib/*.desktop /usr/share/applications/
" > "${NAME}-${VERSION}/install.sh"
chmod 555 "${NAME}-${VERSION}/install.sh"
echo "Erzeuge Uninstaller-Skript"
echo "#!/bin/sh
if [ \$(/usr/bin/id -u) -ne 0 ]; then
    echo \"root permissions required, please retry wiht sudo or as root\"
    exit
fi

rm -rf /opt/${NAME_KLEIN}
rm /usr/share/applications/${NAME_KLEIN}-${NAME}.desktop
rm -rf ~/.config/${NAME}
" > "${NAME}-${VERSION}/uninstall.sh"

chmod 555 "${NAME}-${VERSION}/uninstall.sh"

echo "Erzeuge Archiv ${OUT}/${NAME}-${VERSION}-linux-install.tar.gz"
tar -czf "${NAME}-${VERSION}-linux-install.tar.gz" "${NAME}-${VERSION}"

echo ""
echo ""
echo "Der Prozess wurde beendet."
echo ""
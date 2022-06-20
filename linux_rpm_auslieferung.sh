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
    exit 1
fi

EXIT_CODE=0
chmod +x ./opt/*/bin/*
echo ''
echo '1) -----------------------'
echo ''
echo copy program to /opt/
cp -rv --copy-contents ./opt/* /opt/
EXIT_CODE=\$(expr $EXIT_CODE + \$?)

echo ''
echo '2) -----------------------'
echo ''
echo copy desktop-file for Application-Menu to /usr/share/applications/
mkdir --parents /usr/share/applications
cp -v --copy-contents ./opt/*/lib/*.desktop /usr/share/applications/
EXIT_CODE=\$(expr $EXIT_CODE + \$?)

echo ''
echo '--------------------------'
echo ''
if [ \$EXIT_CODE -gt 0 ]; then
    echo Something went wrong.
    EXIT_CODE=2
else
    echo Installation successfully completed.
fi
echo ''
exit \$EXIT_CODE
" > "${NAME}-${VERSION}/install.sh"
chmod 555 "${NAME}-${VERSION}/install.sh"
echo "Erzeuge Uninstaller-Skript"
echo "#!/bin/sh
if [ \$(/usr/bin/id -u) -ne 0 ]; then
    echo \"root permissions required, please retry wiht sudo or as root\"
    exit 1
fi

EXIT_CODE=0
echo ''
echo '1) -----------------------'
echo ''
echo delete program path
rm -rfv /opt/${NAME_KLEIN}
EXIT_CODE=\$(expr $EXIT_CODE + \$?)

echo ''
echo '2) -----------------------'
echo ''
echo delete desktop-file
rm -v /usr/share/applications/${NAME_KLEIN}-${NAME}.desktop
EXIT_CODE=\$(expr $EXIT_CODE + \$?)

echo ''
echo '--------------------------'
if [ \$EXIT_CODE -gt 0 ]; then
    echo Something went wrong. Please see output.
    EXIT_CODE=2
else
    echo Uninstallation successfully completed.
fi
echo '--------------------------'
echo ''

echo ''
echo '--------- Additional Steps ---------'
echo ''
echo 'run the following command to remove all remaining configuration files:'
echo ''
echo '>>'
echo '    rm -rfv ~/.config/${NAME}'
echo '<<'
echo ''

exit \$EXIT_CODE
" > "${NAME}-${VERSION}/uninstall.sh"

chmod 555 "${NAME}-${VERSION}/uninstall.sh"

echo "Erzeuge Archiv ${OUT}/${NAME}-${VERSION}-linux-install.tar.gz"
tar -czf "${NAME}-${VERSION}-linux-install.tar.gz" "${NAME}-${VERSION}"

echo ""
echo ""
echo "Der Prozess wurde beendet."
echo ""
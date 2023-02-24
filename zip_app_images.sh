VERSION="1.2.1"
NAME="Classifier"
MODULE="classifier"
MAIN_CLASS="io.github.aid_labor.classifier.main.Hauptfenster"
OUT="auslieferung/${VERSION}"
MAC="${OUT}/macOS/app"
LINUX="${OUT}/Linux/rpm"
WIN="${OUT}/Windows/app"
ROOT=$(pwd)

echo "Zip Mac"
cd "${ROOT}/${MAC}"
zip -rq "${NAME}-${VERSION}-App-Image-Mac.zip" -xi "./${NAME}.app"
mkdir "${ROOT}/${MAC}/${NAME}-${VERSION}-mac"
cp -r "${ROOT}/${OUT}/macOS/input" "${ROOT}/${MAC}/${NAME}-${VERSION}-mac/${NAME}"
echo "#!/bin/sh
cd \"\$(dirname \"\$0\")\" # zum Pfad dieses Skriptes wechseln
java --module-path ./${NAME}:./${NAME}/lib --add-modules ALL-MODULE-PATH -jar ./${NAME}/${MODULE}-${VERSION}.jar ${MODULE}/${MAIN_CLASS} $@" > "${ROOT}/${MAC}/${NAME}-${VERSION}-mac/${MODULE}.command"
chmod 555 "${ROOT}/${MAC}/${NAME}-${VERSION}-mac/${MODULE}.command"
zip -rq "${NAME}-${VERSION}-Mac.zip" -xi "./${NAME}-${VERSION}-mac"
rm -rf "./${NAME}-${VERSION}-mac"
mkdir "${ROOT}/${MAC}/jlink"
cp -R "${ROOT}/${OUT}/macOS/input/" "${ROOT}/${MAC}/jlink/"
rm -rf "${ROOT}/${MAC}/jlink/lib"
cp -R "${ROOT}/${OUT}/macOS/input/lib/" "${ROOT}/${MAC}/jlink/"
jlink --module-path jlink --add-modules ${MODULE} --output ${MODULE}-${VERSION}-with-jre --launcher ${MODULE}=${MODULE}/${MAIN_CLASS}
rm -rf "${ROOT}/${MAC}/jlink"
zip -rq "${NAME}-${VERSION}-Mac-with-jre.zip" -xi "./${MODULE}-${VERSION}-with-jre"
rm -rf "./${MODULE}-${VERSION}-with-jre"

echo "Zip Linux"
cd "${ROOT}/${LINUX}"
zip -rq "${NAME}-${VERSION}-App-Image-Linux.zip" -xi "./${NAME}"

echo "Zip Windows"
cd "${ROOT}/${WIN}"
zip -rq "${NAME}-${VERSION}-App-Image-Windows.zip" -xi "./${NAME}"

cd "${ROOT}"
mkdir "${OUT}/RELEASE"
mv "${MAC}/${NAME}-${VERSION}-App-Image-Mac.zip" "${OUT}/RELEASE/"
mv "${LINUX}/${NAME}-${VERSION}-App-Image-Linux.zip" "${OUT}/RELEASE/"
mv "${WIN}/${NAME}-${VERSION}-App-Image-Windows.zip" "${OUT}/RELEASE/"

cp "${MAC}/${NAME}-${VERSION}-mac-install.pkg" "${OUT}/RELEASE/"
cp "${LINUX}/${NAME}-${VERSION}-linux-install.tar.gz" "${OUT}/RELEASE/"
cp "${WIN}/${NAME}-${VERSION}-windows-install.exe" "${OUT}/RELEASE/"
cp "${WIN}/${NAME}-${VERSION}-windows-install.msi" "${OUT}/RELEASE/"

mv "${MAC}/${NAME}-${VERSION}-Mac.zip" "${OUT}/RELEASE/"
mv "${MAC}/${NAME}-${VERSION}-Mac-with-jre.zip" "${OUT}/RELEASE/"

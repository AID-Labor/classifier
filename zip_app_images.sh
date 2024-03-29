VERSION="1.3.1"
NAME="Classifier"
NAME_LOW="classifier"
MODULE="classifier"
MAIN_CLASS="io.github.aid_labor.classifier.main.Hauptfenster"
OUT="auslieferung/${VERSION}"
MAC="${OUT}/macOS/app"
LINUX="${OUT}/Linux/rpm"
WIN="${OUT}/Windows/app"
ROOT=$(pwd)

echo "Zip Mac"
cd "${ROOT}/${MAC}"
echo " ├─ Zip App-Image"
zip -rq "${NAME}-${VERSION}-App-Image-Mac.zip" -xi "./${NAME}.app"
echo " ├─ create ${NAME}-${VERSION}-Mac.zip"
mkdir "${ROOT}/${MAC}/${NAME}-${VERSION}-mac"
cp -r "${ROOT}/${OUT}/macOS/input" "${ROOT}/${MAC}/${NAME}-${VERSION}-mac/${NAME}"
echo "#!/bin/sh
cd \"\$(dirname \"\$0\")\" # change to path of this script
java --module-path ./${NAME}:./${NAME}/lib --add-modules ALL-MODULE-PATH -jar ./${NAME}/${MODULE}-${VERSION}.jar ${MODULE}/${MAIN_CLASS} $@" > "${ROOT}/${MAC}/${NAME}-${VERSION}-mac/${NAME_LOW}.command"
chmod 555 "${ROOT}/${MAC}/${NAME}-${VERSION}-mac/${NAME_LOW}.command"
zip -rq "${NAME}-${VERSION}-Mac.zip" -xi "./${NAME}-${VERSION}-mac"
rm -rf "./${NAME}-${VERSION}-mac"
echo " └─ create jlink executable ${NAME}-${VERSION}-Mac-with-jre.zip"
mkdir "${ROOT}/${MAC}/jlink"
cp -R "${ROOT}/${OUT}/macOS/input/" "${ROOT}/${MAC}/jlink/"
rm -rf "${ROOT}/${MAC}/jlink/lib"
cp -R "${ROOT}/${OUT}/macOS/input/lib/" "${ROOT}/${MAC}/jlink/"
jlink --module-path jlink --add-modules ${MODULE} --output ${MODULE}-${VERSION}-with-jre --launcher ${MODULE}=${MODULE}/${MAIN_CLASS}
rm -rf "${ROOT}/${MAC}/jlink"
echo "#!/bin/sh
cd \"\$(dirname \"\$0\")\" # change to path of this script
bin/${NAME_LOW}" > "./${MODULE}-${VERSION}-with-jre/${NAME_LOW}.command"
chmod 555 "./${MODULE}-${VERSION}-with-jre/${NAME_LOW}.command"
zip -rq "${NAME}-${VERSION}-Mac-with-jre.zip" -xi "./${MODULE}-${VERSION}-with-jre"
rm -rf "./${MODULE}-${VERSION}-with-jre"

echo ""
echo "Tar Linux"
echo " └─ Tar App-Image"
cd "${ROOT}/${LINUX}"
#zip -rq "${NAME}-${VERSION}-App-Image-Linux.zip" -xi "./${NAME}"
tar -czf "${NAME}-${VERSION}-App-Image-Linux.tar.gz" "./${NAME}"

echo ""
echo "Zip Windows"
echo " └─ Zip App-Image"
cd "${ROOT}/${WIN}"
zip -rq "${NAME}-${VERSION}-App-Image-Windows.zip" -xi "./${NAME}"

cd "${ROOT}"
echo ""
echo "Move/Copy files to ${OUT}/RELEASE"
if [ -d "${OUT}/RELEASE" ]; then
    echo " ├─ clean ${OUT}/RELEASE"
    rm -rf "${OUT}/RELEASE/*"
else
    echo " ├─ create ${OUT}/RELEASE"
    mkdir "${OUT}/RELEASE"
fi
echo " ├─ move ${MAC}/${NAME}-${VERSION}-App-Image-Mac.zip"
mv "${MAC}/${NAME}-${VERSION}-App-Image-Mac.zip" "${OUT}/RELEASE/"
echo " ├─ move ${LINUX}/${NAME}-${VERSION}-App-Image-Linux.tar.gz"
mv "${LINUX}/${NAME}-${VERSION}-App-Image-Linux.tar.gz" "${OUT}/RELEASE/"
echo " ├─ move ${WIN}/${NAME}-${VERSION}-App-Image-Windows.zip"
mv "${WIN}/${NAME}-${VERSION}-App-Image-Windows.zip" "${OUT}/RELEASE/"

echo " ├─ copy ${MAC}/${NAME}-${VERSION}-mac-install.pkg"
cp "${MAC}/${NAME}-${VERSION}-mac-install.pkg" "${OUT}/RELEASE/"
echo " ├─ copy ${LINUX}/${NAME}-${VERSION}-linux-install.tar.gz"
cp "${LINUX}/${NAME}-${VERSION}-linux-install.tar.gz" "${OUT}/RELEASE/"
echo " ├─ copy ${WIN}/${NAME}-${VERSION}-windows-install.exe"
cp "${WIN}/${NAME}-${VERSION}-windows-install.exe" "${OUT}/RELEASE/"
echo " ├─ copy ${WIN}/${NAME}-${VERSION}-windows-install.msi"
cp "${WIN}/${NAME}-${VERSION}-windows-install.msi" "${OUT}/RELEASE/"

echo " ├─ move ${MAC}/${NAME}-${VERSION}-Mac.zip"
mv "${MAC}/${NAME}-${VERSION}-Mac.zip" "${OUT}/RELEASE/"
echo " └─ move ${MAC}/${NAME}-${VERSION}-Mac-with-jre.zip"
mv "${MAC}/${NAME}-${VERSION}-Mac-with-jre.zip" "${OUT}/RELEASE/"

VERSION="1.0.4"
NAME="Classifier"
OUT="auslieferung/${VERSION}"
MAC="${OUT}/macOS/app"
LINUX="${OUT}/Linux/rpm"
WIN="${OUT}/Windows/app"
ROOT=$(pwd)

echo "Zip Mac"
cd "${ROOT}/${MAC}"
zip -rq "${NAME}-${VERSION}-App-Image-Mac.zip" -xi "./${NAME}.app"

echo "Zip Linux"
cd "${ROOT}/${LINUX}"
zip -rq "${NAME}-${VERSION}-App-Image-Linux.zip" -xi "./${NAME}"

echo "Zip Windows"
cd "${ROOT}/${WIN}"
zip -rq "${NAME}-${VERSION}-App-Image-Windows.zip" -xi "./${NAME}"

cd "${ROOT}"
mkdir "${OUT}/RELEASE"
cp "${MAC}/${NAME}-${VERSION}-App-Image-Mac.zip" "${OUT}/RELEASE/"
cp "${LINUX}/${NAME}-${VERSION}-App-Image-Linux.zip" "${OUT}/RELEASE/"
cp "${WIN}/${NAME}-${VERSION}-App-Image-Windows.zip" "${OUT}/RELEASE/"

cp "${MAC}/${NAME}-${VERSION}-mac-install.pkg" "${OUT}/RELEASE/"
cp "${LINUX}/${NAME}-${VERSION}-linux-install.tar.gz" "${OUT}/RELEASE/"
cp "${WIN}/${NAME}-${VERSION}-windows-install.exe" "${OUT}/RELEASE/"
cp "${WIN}/${NAME}-${VERSION}-windows-install.msi" "${OUT}/RELEASE/"

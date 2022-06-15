VERSION="1.0.1"
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
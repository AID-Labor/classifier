VERSION=1.2.1
OS=$(uname -o)
if [[ "$OS" == "Darwin" ]]; then
    DIR=deploy/${VERSION}/macOS/input
else
    DIR=deploy/${VERSION}/Linux/input
fi
java --module-path ${DIR}/lib:${DIR} --add-modules ALL-MODULE-PATH -jar ${DIR}/classifier-${VERSION}.jar classifier/io.github.aid_labor.classifier.basis.Ressourcen $@
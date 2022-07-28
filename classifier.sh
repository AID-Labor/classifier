VERSION=1.1.1
DIR=auslieferung/${VERSION}/macOS/input
java --module-path ${DIR}/lib:${DIR} --add-modules ALL-MODULE-PATH -jar ${DIR}/classifier-${VERSION}.jar classifier/is.github.aid_labor.classifier.basis.Ressourcen $@
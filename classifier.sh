VERSION=1.0.2
DIR=auslieferung/${VERSION}/macOS/input
java --module-path ${DIR}/lib --add-modules ALL-MODULE-PATH -jar ${DIR}/classifier-${VERSION}.jar classifier/is.github.aid_labor.classifier.basis.Ressourcen $@
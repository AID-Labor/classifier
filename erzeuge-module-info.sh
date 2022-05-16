MODULE_PATH=$1
JAR=$2
jdeps --module-path $MODULE_PATH --generate-module-info module-info --multi-release 17 --ignore-missing-deps $JAR
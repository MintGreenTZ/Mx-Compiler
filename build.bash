# this script is called when the judge is building your compiler.
# no argument will be passed in.
set -e
cd "$(dirname "$0")"
mkdir -p bin
find ./src -name *.java | javac -d bin -classpath "lib/antlr-4.7.1-complete.jar" @/dev/stdin

#find ./src/Compiler -name *.java | javac -cp "./lib/commons-text-1.6.jar:./lib/antlr-4.7.1-complete.jar:./lib/antlr-runtime-4.7.1.jar" #src/Compiler/Main.java @/dev/stdin -d bin
set -e
cd "$(dirname "$0")"
export CCHK="java -classpath ./lib/antlr-runtime-4.7.1.jar:./lib/commons-text-1.6.jar:./lib/antlr-4.7.1-complete.jar:./bin Compiler.Main -codegen"
cat > code.mx   # save everything in stdin to program.txt
$CCHK

#java -cp .:antlr-4.7.1-complete.jar:antlr-runtime-4.7.1.jar:commons-text-1.6.jar Compiler.Main
#cat > code.mx   # save everything in stdin to program.txt


#java -cp .:antlr-4.7.1-complete.jar:antlr-runtime-4.7.1.jar:commons-text-1.6.jar Compiler.Main > out.txt
#cat out.txt
# sed ':t;N;s/\n//;b t' out.txt

# res=`java -cp .:antlr-4.7.1-complete.jar:antlr-runtime-4.7.1.jar:commons-text-1.6.jar Compiler.Main < in.txt`

# echo -n $res
# echo $res"\c" | sed 's/ /\n/g ' # since echo will replace '\n' with 'space'

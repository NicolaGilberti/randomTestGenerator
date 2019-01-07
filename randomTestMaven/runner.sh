#!/bin/bash
#number of the iteration the program has to do
ITERATIONS=$1
#true or false, to redirect the output (no stderr) or not into a file
FILE=$2
#to set a specific file for the output
OUTPUT=$3
#set the testsuite names given by the program
reportDir=./webrandomgenerator-report
fileTest=Tester.java
covFileTmp=./covFile.txt
#extra variables
covFilesDir=$reportDir/coveragefiles/
covFileName=covFile-
testSuiteDir=$reportDir/testsuites/
testSuiteName=TestSuite
#Set the path to the Program
jar=/home/foobar/.m2/repository/randomTestMaven/randomTestMaven/0.0.1/randomTestMaven-0.0.1.jar
#the path of the project to test (if different from . -> current dir)
dir_under_test=.
#the program need its proper properties file, so set the path to it
prop=/home/foobar/workspace/randomTestGenerator/randomTestMaven/program____________.properties
cp $prop $dir_under_test/program.properties
#evosuite/selenium/idk generate a .ser file. Set the path of that file
serFile=/home/foobar/Desktop/*.ser
#classpath of the dependencies of the program (fisrt elem inserted)
classpath=/home/foobar/.m2/repository/fr/inria/gforge/spoon/spoon-core/6.2.0/spoon-core-6.2.0-jar-with-dependencies.jar:/home/foobar/.m2/repository/org/eclipse/tycho/org.eclipse.jdt.core/3.13.50.v20171007-0855/org.eclipse.jdt.core-3.13.50.v20171007-0855.jar:/home/foobar/.m2/repository/com/martiansoftware/jsap/2.1/jsap-2.1.jar:/home/foobar/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/home/foobar/.m2/repository/commons-io/commons-io/2.5/commons-io-2.5.jar:/home/foobar/.m2/repository/org/apache/maven/maven-model/3.3.9/maven-model-3.3.9.jar:/home/foobar/.m2/repository/org/codehaus/plexus/plexus-utils/3.0.22/plexus-utils-3.0.22.jar:/home/foobar/.m2/repository/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:/home/foobar/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.9.2/jackson-databind-2.9.2.jar:/home/foobar/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.9.0/jackson-annotations-2.9.0.jar:/home/foobar/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.9.2/jackson-core-2.9.2.jar:/home/foobar/.m2/repository/com/sun/tools/1.8.0/tools-1.8.0.jar:/home/foobar/.m2/repository/junit/junit/4.12/junit-4.12.jar:/home/foobar/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/home/foobar/.m2/repository/navigation-graph/navigation-graph/1.0/navigation-graph-1.0.jar:/home/foobar/.m2/repository/org/jgrapht/jgrapht-core/1.0.0/jgrapht-core-1.0.0.jar:/home/foobar/.m2/repository/org/jgrapht/jgrapht-ext/1.0.0/jgrapht-ext-1.0.0.jar:/home/foobar/.m2/repository/org/tinyjee/jgraphx/jgraphx/2.0.0.1/jgraphx-2.0.0.1.jar:/home/foobar/.m2/repository/jgraph/jgraph/5.13.0.0/jgraph-5.13.0.0.jar:/home/foobar/.m2/repository/org/antlr/antlr4-runtime/4.5.3/antlr4-runtime-4.5.3.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-java/3.3.1/selenium-java-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-chrome-driver/3.3.1/selenium-chrome-driver-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-edge-driver/3.3.1/selenium-edge-driver-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-firefox-driver/3.3.1/selenium-firefox-driver-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-ie-driver/3.3.1/selenium-ie-driver-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-opera-driver/3.3.1/selenium-opera-driver-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-safari-driver/3.3.1/selenium-safari-driver-3.3.1.jar:/home/foobar/.m2/repository/com/codeborne/phantomjsdriver/1.4.0/phantomjsdriver-1.4.0.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/htmlunit-driver/2.24/htmlunit-driver-2.24.jar:/home/foobar/.m2/repository/net/sourceforge/htmlunit/htmlunit/2.24/htmlunit-2.24.jar:/home/foobar/.m2/repository/xalan/xalan/2.7.2/xalan-2.7.2.jar:/home/foobar/.m2/repository/xalan/serializer/2.7.2/serializer-2.7.2.jar:/home/foobar/.m2/repository/org/apache/httpcomponents/httpmime/4.5.2/httpmime-4.5.2.jar:/home/foobar/.m2/repository/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:/home/foobar/.m2/repository/net/sourceforge/htmlunit/htmlunit-core-js/2.23/htmlunit-core-js-2.23.jar:/home/foobar/.m2/repository/net/sourceforge/htmlunit/neko-htmlunit/2.24/neko-htmlunit-2.24.jar:/home/foobar/.m2/repository/xerces/xercesImpl/2.11.0/xercesImpl-2.11.0.jar:/home/foobar/.m2/repository/xml-apis/xml-apis/1.4.01/xml-apis-1.4.01.jar:/home/foobar/.m2/repository/net/sourceforge/cssparser/cssparser/0.9.21/cssparser-0.9.21.jar:/home/foobar/.m2/repository/org/w3c/css/sac/1.3/sac-1.3.jar:/home/foobar/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar:/home/foobar/.m2/repository/org/eclipse/jetty/websocket/websocket-client/9.2.20.v20161216/websocket-client-9.2.20.v20161216.jar:/home/foobar/.m2/repository/org/eclipse/jetty/jetty-util/9.2.20.v20161216/jetty-util-9.2.20.v20161216.jar:/home/foobar/.m2/repository/org/eclipse/jetty/jetty-io/9.2.20.v20161216/jetty-io-9.2.20.v20161216.jar:/home/foobar/.m2/repository/org/eclipse/jetty/websocket/websocket-common/9.2.20.v20161216/websocket-common-9.2.20.v20161216.jar:/home/foobar/.m2/repository/org/eclipse/jetty/websocket/websocket-api/9.2.20.v20161216/websocket-api-9.2.20.v20161216.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-support/3.3.1/selenium-support-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-remote-driver/3.3.1/selenium-remote-driver-3.3.1.jar:/home/foobar/.m2/repository/org/seleniumhq/selenium/selenium-api/3.3.1/selenium-api-3.3.1.jar:/home/foobar/.m2/repository/cglib/cglib-nodep/3.2.4/cglib-nodep-3.2.4.jar:/home/foobar/.m2/repository/org/apache/commons/commons-exec/1.3/commons-exec-1.3.jar:/home/foobar/.m2/repository/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:/home/foobar/.m2/repository/com/google/guava/guava/21.0/guava-21.0.jar:/home/foobar/.m2/repository/org/apache/httpcomponents/httpclient/4.5.2/httpclient-4.5.2.jar:/home/foobar/.m2/repository/org/apache/httpcomponents/httpcore/4.4.4/httpcore-4.4.4.jar:/home/foobar/.m2/repository/net/java/dev/jna/jna-platform/4.1.0/jna-platform-4.1.0.jar:/home/foobar/.m2/repository/net/java/dev/jna/jna/4.1.0/jna-4.1.0.jar:/home/foobar/.m2/repository/org/hamcrest/hamcrest-library/1.3/hamcrest-library-1.3.jar:target/classes
#setting default values in case of they are not given
if [[ -z $ITERATIONS ]]; then
	ITERATIONS=1
fi
if [[ -z $FILE ]]; then
	FILE=false
fi
if [[ -z $OUTPUT ]]; then
	OUTPUT=./file.txt
fi
#clean the file, to be sure that we are not collecting extra wrong data
cp /dev/null $OUTPUT
rm -rf $reportDir
mkdir -p $covFilesDir
mkdir -p $testSuiteDir
#loop for the iterations required
COUNT=0
while [ $COUNT -lt $ITERATIONS ]
do	
	#the program generate a .ser file that must be removed to let the program to go on
	rm -f $serFile
	((COUNT++))
	echo iteration number $COUNT
	java  -cp $jar:$classpath primary.MainProgram $* >> $OUTPUT
	sleep 5
	#only to close the chrome tab, to avoid the opening of n tabs
	#killall chrome
	TestExecutedCounterH=$(wc -l < $covFileTmp)
	echo Test cases executed: $TestExecutedCounterH >> $covFileTmp
	cp $covFileTmp $covFilesDir$covFileName$COUNT.txt
	cp $reportDir/$fileTest $testSuiteDir$testSuiteName$COUNT.java
	sed -i -e 's/public class Tester/public class '$testSuiteName$COUNT'/g' $testSuiteDir$testSuiteName$COUNT.java
	rm $reportDir/$fileTest
	rm $covFileTmp
done

echo $'\n'The results of the $ITERATIONS tests:
cat $OUTPUT
RESAVG=$(grep -o [0-9]* $OUTPUT | awk '{ SUM += $1} END { printf "\nThe AVG is=> %f\n",SUM/'$ITERATIONS' }')
RESMIN=$(grep -o [0-9]* $OUTPUT | awk 'BEGIN{min=1000}{if ($1<0+min) min=$1} END { printf "\nThe MIN is=> %f\n",min }')
RESMAX=$(grep -o [0-9]* $OUTPUT | awk 'BEGIN{max=0}{if ($1>0+max) max=$1} END { printf "\nThe MAX is=> %f\n",max }')
echo $RESAVG
echo $RESMIN
echo $RESMAX
if $FILE; then
	echo $RESAVG >> $OUTPUT
	echo $RESMIN >> $OUTPUT
	echo $RESMAX >> $OUTPUT
else
	rm $OUTPUT
fi
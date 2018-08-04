# Project

# To compile the Maven project
1. mvn clean
2. mvn compile assembly:single
#### Requirements
1. if the class analysed before is inside the project root directory, delete it.
2. if exist, remove the older test file.
3. Check the POM.xml file, to ensure the correctness of the path and the version of the tools.jar lib
    - tools is a library belonging to JDK. The POM manage 'default' path (JAVA_HOME environment variable setted to JDK folder) for 'mac' and 'windows' devices.
    - 'dos', 'netware', 'os/2', 'tandem', 'unix', 'win9x', 'z/os', 'os/400' and 'openvms' are not assessed.
    - the library now is defined with the 1.8.0 version number, but it depends on the version installed on the device.
* remove/move etc -> they must not exist inside the project dir before the execution of the Maven commands

# To run the project
In general:	mvn exec:java -Dexec.mainClass="primary.MainProgram" \
On Windows:	mvn exec:java -D"exec.mainClass"="primary.MainProgram" \
Another option:	java -jar target/fileName.jar
#### Requirements
1. fill the .properties file with the proper values 
    - Mandatory 
        - fileName -> name of the java file to use as input for the algorithm
        - filePath -> path to the directory of the java file cited above
        - ExecutionTestTimer -> The time that the algorithm has to elaborate tests. It is in millisecond
        - MaxNumberOfMethodXTest -> Each test perform X method calls. This value is the upper bound of X
    - Optional
        - Classpath -> Extra classpath that the compiler need to compile the file stated before
        - OutputDir -> The path for the output JUnit class

# Project

# To compile the Maven project
1. mvn clean
2. mvn compile
3. mvn install if required
#### Requirements
1. if the class analysed before is inside the project root directory, delete it.
2. if exist, remove the older test file.
3. Check the POM.xml file, to ensure the correctness of the path and the version of the tools.jar lib
    - tools is a library belonging to JDK. The POM manage 'default' path (JAVA_HOME environment variable setted to JDK folder) for 'mac', 'windows' and 'unix' devices.
    - 'dos', 'netware', 'os/2', 'tandem', 'win9x', 'z/os', 'os/400' and 'openvms' are not assessed.
    - the library now is defined with the 1.8.0 version number, but it depends on the version installed on the device.
* remove/move etc -> they may create mistakes during the execution of the Maven commands

# To run the project
In general:	mvn exec:java -Dexec.mainClass="primary.MainProgram" \
On Windows:	mvn exec:java -D"exec.mainClass"="primary.MainProgram"\

runner.sh contains a simple script to run the project\

#### Requirements
1. fill the .properties/.sh files with the proper values
2. check all the paths 
3. the classpath can be found using the command 'mvn dependency:build-classpath' in the project dir. The classpath required in the .sh file is different from the classpath required in the .properties file.

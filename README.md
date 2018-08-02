# Project

# To compile the Maven project
1. mvn clean
2. mvn compile assembly:single
#### Requirements
1. if exist, remove the 'spoon' directory
2. if the class analysed before is inside the project directory, delete it
3. if exist, remove the older test file
4. remove/move etc -> they must not exist inside the project dir before the execution of the Maven commands

# To run the project
In general:	mvn exec:java -Dexec.mainClass="primary.MainProgram"
On Windows:	mvn exec:java -D"exec.mainClass"="primary.MainProgram"
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
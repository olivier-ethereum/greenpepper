USE CASES
~~~~~~~~~~

The Demonstration pack

  This pack contains :
  
  * greenpepper-client-4.0-beta2-complete.jar : the command line client for
    greenpepper.

  * greenpepper-confluence-demo-2.11-beta1-fixtures.jar : the demonstration
    fixture classes.

  * project/src/fixture/specs : the demo HTML specification.

  * project : the demonstration maven project.

Command line runner

+--------------------------------------------+
java -cp \
greenpepper-client-4.0-beta2-complete.jar:greenpepper-confluence-demo-2.11-beta1-fixtures.jar \
com.greenpepper.runner.Main
+--------------------------------------------+

* Launch a Test

+---------------------------------------------------+
./run.sh project/src/fixture/specs/Demo/Bank.html 
+---------------------------------------------------+

  The result will be in the current directory.
  The run.sh is just a shortcut for the 'java' command with the class path.

Maven Plugin

  Go into the <<project>> folder and launch <<mvn integration-test>>

+---------------------------------------------------+
cd project
mvn integration-test
+---------------------------------------------------+

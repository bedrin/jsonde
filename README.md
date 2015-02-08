jSonde
============
jSonde allows you to generate sequence diagrams directly from your application runtime! This gives you a lot of advantages:
 * Understand the code created by your colleagues/partners in a short time
 * Rapidly generate documentation for your partners or users.
 * Easily investigate what's happening in large Java applications
 * Excellent companion for a common debugger

Manual
============
# Overview
jSonde allows you to analyze existing Java applications.

It can generate sequence diagrams, measure performance (both cpu and memory) and build different reports, like jar dependencies and others.

jSonde doesn't require any specific knowledge for using it and supports various operation systems and Java environments.

jSonde consists of two main components:
* An agent which is attached to java processes and collects information
* A rich GUI for visualising the results collected by the agent
 
# Attaching jSonde agent to java applications
There're two main ways to attach jSonde agent to java applications - you can either specify additional parameters to java command or you can attach the agent dynamically without even restarting your application.

## Attaching agent dynamically
This is the simplest way to attach jSonde agent to your application

Select "File" \ "Attach to Sun JVM" menu item, and you'll see the dialog like shown below:
![Attach to JVM](http://bedrin.github.io/jsonde/attachToJvm.png "Attach to JVM")

In this dialog there's a list of all java applications running on your computer

Other fields in this dialog will be explained in the other chapter of this manual

Please note, that this feature may be unavailable on some virtual machines. It was properly tested only on Sun JVM 1.6+

## Attaching agent using command line parameters

Let's assume that you execute your java application using following command

java -jar app.jar

In order to execute this application with jSonde agent, you need to add new parameters like shown below:

java -javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001-jar app.jar

By no means you can use jSonde to analyze not only J2SE applications. You can find a few examples of using jSonde with Tomcat, JBoss and other environments below.

### Apache Tomcat
#### Microsoft Windows

File: %TOMCAT_HOME%\bin\catalina.bat

Add the following line:

set JAVA_OPTS=-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001 %JAVA_OPTS%

If you use the windows service or system tray startup use the following parameters instead:

-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001

#### GNU/Linux & Mac OS X

File: $TOMCAT_HOME/bin/catalina.sh

Add the following line:

JAVA_OPTS="-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001 $JAVA_OPTS"

### JBoss
#### Microsoft Windows

File: %JBOSS_HOME%\bin\run.bat

Add the following line:

set JAVA_OPTS=-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001 %JAVA_OPTS%

#### GNU/Linux & Mac OS X

File: $JBOSS_HOME/bin/run.sh

Add the following line:

JAVA_OPTS="-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001 $JAVA_OPTS"

### Other Application Servers
Just check your application server configuration for the place, where you should specify jvm parameters

Hint: this is the same place where you should specify memory options -Xms & -Xmx

### Using jSonde with applets
You can analyze applets running inside a browser with Java Plugin installed.

Please add the following option in "Java(TM) Plug-in Control Panel", on the "Advanced" tab, in the field "Java Runtime Parameters":

-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001

On Windows, "Java(TM) Plug-in Control Panel" is accessible from the Windows "Control Panel"

Important: If the browser is running, you must restart it after you have made the changes.

You can also analyze applets started with appletviewer command by passing -J-javaagent:<path-to-jSonde-folder>/lib/jsonde.agent-1.0.0.beta7.jar=60001 as a command line parameter.

## Connecting to jSonde agent specified by command line parameters
After you've added jSonde agent to java parameters and started your application, you'll se the following message in output:

jSonde agent started

Now you need to start jSonde GUI and select "File" \ "New Project" menu item

After that you'll see a dialog like shown below. It's pretty similar to one, we've described in chapter "Attaching agent dynamically" above. Hence the meaning of the similar fields in these two dialogs is the same

![New Project](http://bedrin.github.io/jsonde/newProjectDialog.png "New Project")


Building
============
jSonde is built using JDK6 and Maven 3+ - just checkout the project and type `mvn install`
JDK6 is required only for building the project - once it's built, you can use jSonde with JRE 1.5+

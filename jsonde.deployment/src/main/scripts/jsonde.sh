#!/bin/bash
#
# jSonde GUI start script
#

dir=`dirname $0`
cd $dir
dir=`pwd`

JAR_FILE_NAME="jsonde.jar"

if [ "x$JAVA_HOME" == "x" ] ; then
JAVA=`which java`
else
JAVA="$JAVA_HOME/bin/java"
fi

exec $JAVA -Xmx512m -jar $JAR_FILE_NAME
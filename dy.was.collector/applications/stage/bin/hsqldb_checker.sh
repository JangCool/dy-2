#!/bin/bash

#if [ $JAVA_HOME ]
#then
##        echo "JAVA_HOME found at $JAVA_HOME"
#        RUN_JAVA=$JAVA_HOME/bin/java
#else
##        echo "JAVA_HOME environment variable not available."
#    RUN_JAVA=`which java 2>/dev/null`
#fi

retVal=`${RUN_JAVA} -jar  ${PROG_HOME}/lib/sqltool.jar  --rcFile=${PROG_HOME}/bin/sqltool.rc --sql 'select 1 from INFORMATION_SCHEMA.SYSTEM_USERS;'  localhost`
retVal=`echo "$retVal" | sed 's/[[:space:]]//g'`
if [ "$retVal" = "1" ]
then
        echo "[HSQLDB connection succeeded!!] "
else
        echo "HSQLDB connection failed!!  Please check HSQLDB!!"
        exit
fi

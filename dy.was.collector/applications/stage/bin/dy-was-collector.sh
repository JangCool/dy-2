#!/bin/bash
# ----------------------------------------------------------------------
# ----- Customizable Variables -----------------------------------------
# ----------------------------------------------------------------------

JAVA_HOME=/applications/jdk1.8.0_121
PROG_HOME=$(dirname $(cd "$(dirname "$0")" && pwd))
echo $PROG_HOME
PROG_NAME=dy-was-collector
# ----------------------------------------------------------------------


# ----------------------------------------------------------------------
# ----- Do not touch below this line!-----------------------------------
# ----------------------------------------------------------------------
RUNDIR=${PROG_HOME}/bin
MAIN_CLASS=DyCollectorServer
pidfile=/${RUNDIR}/$PROG_NAME.pid


if [ $JAVA_HOME ]
then
        RUN_JAVA=$JAVA_HOME/bin/java
else
#        echo "JAVA_HOME environment variable not available."
    RUN_JAVA=`which java 2>/dev/null`
fi

export RUN_JAVA
export PROG_HOME


RUNUSER=$USER
GRACETIME=10
MAXMEM=2048m
MINMEM=2048m
JAVAOPTS="-Xms$MINMEM -Xmx$MAXMEM  -DstartupMode=start -Djava.net.preferIPv4Stack=true -Dserver.conf=${PROG_HOME}/conf/config.properties -DPROG_HOME=${PROG_HOME} -Dlogback.configurationFile=${PROG_HOME}/conf/logback.xml"
JAVAOPTS="$JAVAOPTS   -verbose:gc -Xloggc:${PROG_HOME}/logs/gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=9 -XX:GCLogFileSize=20m"


#JMx  Setting
#export JAVAOPTS="$JAVAOPTS -Dcom.sun.management.jmxremote=true"
#export JAVAOPTS="$JAVAOPTS -Dcom.sun.management.jmxremote.port=9999"
#export JAVAOPTS="$JAVAOPTS -Dcom.sun.management.jmxremote.ssl=false"
#export JAVAOPTS="$JAVAOPTS -Dcom.sun.management.jmxremote.authenticate=false"
#export JAVAOPTS="$JAVAOPTS -Djava.rmi.server.hostname=127.0.0.1"
   

do_start() {


        # ----------------------------------------------------------------------
        # ----- hsqldb connection check      -----------------------------------
        # ----------------------------------------------------------------------
        . ${PROG_HOME}/bin/hsqldb_checker.sh
        # ----------------------------------------------------------------------

	if [ -f $pidfile ] 
	then
		pid=`cat $pidfile`
        	#res=`ps --pid $pid 2> /dev/null | grep -c $pid 2> /dev/null`
		res=`ps -ef|grep java |grep $MAIN_CLASS | awk '{print $2}'`
        	#if [ $res -eq '0' ]
        	if [ "$res" = ""  ]
        	then
            		rm -f $pidfile > /dev/null
        	else
			echo "$PROG_NAME already running with PID $pid"
			exit 1;
        	fi
	fi
	
	# encoding might be broken otherwise
	#export LANG=en_US.UTF-8
	
	cd $RUNDIR
	set_classpath
	echo "####1 .. $1"
	echo "####2 .. $2"
	if [ "$2" = "log"	] 
        then
		$RUN_JAVA  $JAVAOPTS  $MAIN_CLASS $1  $JAVAARGS
		echo "$RUN_JAVA  $JAVAOPTS -classpath $CLASSPATH $MAIN_CLASS $1  $JAVAARGS"
	else 
        nohup $RUN_JAVA  $JAVAOPTS  $MAIN_CLASS $1  $JAVAARGS > /dev/null &
        pid=`ps -ef|grep java |grep $MAIN_CLASS | awk '{print $2}'`
        echo "$pid" > $pidfile
	fi
        

	# wait for process to start
	sleep 4

	if [ `ps --pid $pid 2> /dev/null | grep -c $pid 2> /dev/null` -eq '0' ]; then
		echo "Process did not start!"
		rm -f $pidfile
		exit 1;
	fi 
	
	echo "Started with PID: $pid"
}

do_stop() {

	if [ -f $pidfile ] 
	then
		pid=`cat $pidfile`
		echo "Stopping $pid"
		
		kill -s TERM $pid > /dev/null
		rm -f $pidfile
		
		count=0;
		until [ `ps --pid $pid 2> /dev/null | grep -c $pid 2> /dev/null` -eq '0' ] || [ $count -gt $GRACETIME ]
    	do
      		sleep 1
      		let count=$count+1;
    	done

    	if [ $count -gt $GRACETIME ]; then
    		echo "Force stop of $progname"
      		kill -9 $pid
    	fi
    	
    	echo "Stopped"
	fi
}

do_status() {
	
	if [ -f $pidfile ] 
    then
        pid=`cat $pidfile`
        res=`ps --pid $pid 2> /dev/null | grep -c $pid 2> /dev/null`
        if [ $res -eq '0' ]
        then
            rm -f $pidfile > /dev/null
            echo "$PROG_NAME is not running" 
            exit 1;
        else
            echo "$PROG_NAME is running with PID $pid" 
            exit 0;
        fi
    else
        echo "$PROG_NAME is not running" 
        exit 3;
    fi

}

do_signal() {

	if [ -f $pidfile ] 
	then
		pid=`cat $pidfile`
		echo "Signalling $pid"
		kill -s USR2 $pid > /dev/null
	fi
}

set_classpath() {
    export CLASSPATH=$CLASSPATH:$PROG_HOME/lib/*
}





case "$1" in
start)  echo "Starting $PROG_NAME"
        do_start $1 $2
        ;;
stop)  echo "Stopping $PROG_NAME"
        do_stop
        ;;
status)  
        do_status
        ;;
signal)  
        do_signal
        ;;
restart)
		do_stop
		do_start
		;;
*)      echo "Usage: service $PROG_NAME start|stop|restart|status|signal"
        exit 1
        ;;
esac

@echo off
setlocal
set originDir=%cd%
cd /D %~dp0


set RUN=DyCollectorServer
set JAVA_HOME=D:\tools\jdk\jdk1.8.0_111_x64
set NOW=%date:-=%

set MAXMEM=1024m
set MINMEM=1024m

set JAVA_OPTS= -XX:NewSize=258m -XX:MaxNewSize=258m -XX:PermSize=64m -XX:MaxPermSize=64m -XX:MaxDirectMemorySize=256m
set JAVA_OPTS=%JAVA_OPTS% -Xms%MINMEM% -Xmx%MAXMEM%
set JAVA_OPTS=%JAVA_OPTS% -verbose:gc -Xloggc:../logs/gc_%NOW%.log
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+HeapDumpOnOutOfMemoryError  -XX:SurvivorRatio=6
set JAVA_OPTS=%JAVA_OPTS% -XX:HeapDumpPath=..\logs\dump.hprof

set JAVA_OPTS=%JAVA_OPTS% -Dsun.rmi.dgc.server.gcInterval=3600000
set JAVA_OPTS=%JAVA_OPTS% -Dsun.rmi.dgc.client.gcInterval=3600000
set JAVA_OPTS=%JAVA_OPTS% -Djava.net.preferIPv4Stack=true
set JAVA_OPTS=%JAVA_OPTS% -Dlogback.configuration=..\conf\logback.xml
set JAVA_OPTS=%JAVA_OPTS% -Dserver.conf=..\conf\config.properties

rem JMx  Setting
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote=true
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.port=9999
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl=false
set JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
set JAVA_OPTS=%JAVA_OPTS% -Djava.rmi.server.hostname=127.0.0.1


set CLASSPATH=..\lib\*


%JAVA_HOME%\bin\java -server %JAVA_OPTS% -classpath %CLASSPATH%  %RUN% start %*
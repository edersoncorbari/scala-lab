#!/usr/bin/env bash

CLASS=io.github.edersoncorbari.FsImageApp
JAR=../scala-lab.jar
CONF=../application.conf
NAME=SCALA-LAB-FSIMAGE-LOCAL

spark2-submit --master local[*] \
  --driver-memory 3G \
  --files /etc/hive/conf.cloudera.hive/hive-site.xml,${CONF} \
  --driver-java-options "-Dconfig.file=${CONF}" \
  --name ${NAME} \
  --class ${CLASS} \
  ${JAR}

echo "Application (${NAME}) finished with code: $?"
exit $?

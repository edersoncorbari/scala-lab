#!/usr/bin/env bash

CLASS=io.github.edersoncorbari.FsImageApp
JAR=../scala-lab.jar
CONF=../application.conf
NAME=SCALA-LAB-FSIMAGE-YARN

spark2-submit --master yarn \
  --deploy-mode cluster \
  --driver-memory 1G \
  --executor-memory 1G \
  --executor-cores 3 \
  --files /etc/hive/conf.cloudera.hive/hive-site.xml,${CONF} \
  --driver-java-options "-Dconfig.resource=${CONF}" \
  --name ${NAME} \
  --class ${CLASS} \
  ${JAR}

echo "Application (${NAME}) finished with code: $?"
exit $?

#!/bin/sh
echo "********************************************************"
echo "Starting Configuration Server $1 123"
echo "********************************************************"
java -jar -Dspring.profiles.active=$1 /usr/local/configserver/*.jar
#!/bin/sh

echo "client compilation"
javac FileClient.java
echo "done"

echo "server compilation"
javac FileServer.java
echo "done"

java FileServer 28540

#chmod 600 *.class

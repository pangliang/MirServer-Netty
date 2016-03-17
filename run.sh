#!/bin/bash

java -Dfile.encoding=GBK -Djava.awt.headless=true -server -jar build/libs/MirServer-Netty-1.0-SNAPSHOT.jar > stdout.log 2>&1

#!/bin/sh

export PATH=/bin/:/usr/bin/:$PATH
exec java -jar ./target/xAd-1.0-SNAPSHOT-jar-with-dependencies.jar "$@"


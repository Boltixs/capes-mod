#!/bin/sh
APP_BASE_NAME=`basename "$0"`
DIRNAME=`dirname "$0"`
[ -z "$DIRNAME" ] && DIRNAME="."
APP_HOME=`cd "$DIRNAME" && pwd`
exec gradle "$@"

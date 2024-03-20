#! /bin/sh
### BEGIN INIT INFO
# Provides:             Telechips Camera Application
# Required-Start:
# Required-Stop:
# Default-Start:        2 5
# Default-Stop:         0
# Short-Description:    Telechips Camera Application
# Description:          camera_app is a simple camera app for Telechips Automotive Linux SDK
### END INIT INFO
#
# -*- coding: utf-8 -*-
# Debian init.d script for Telechips Camera Application
# Copyright © 2014 Wily Taekhyun Shin <thshin@telechips.com>

# Source function library.
. /etc/init.d/functions

rvc=/usr/bin/camera_app
DESC="telechips camera application"

test -x $rvc || exit 1

[ -r /etc/default/tc-rvc ] && . "/etc/default/tc-rvc"

case "$1" in
  start)
  	echo -n "Starting $DESC: "
    start-stop-daemon -S -b -q -x $rvc -- $EXTRA_ARGS
  	echo "done."
	;;
  stop)
  	echo -n "Stopping $DESC: "
    start-stop-daemon -K -q -n camera_app
  	echo "done."
	;;
  restart)
  	echo -n "Restarting $DESC: "
    start-stop-daemon -K -q -n camera_app
	sleep 1
    start-stop-daemon -S -b -q -x $rvc -- $EXTRA_ARGS
	echo "done."
	;;

  status)
	status $rvc
	exit $?
  ;;

  *)
	echo "Usage: /etc/init.d/tc-rvc-app {start|stop|status|restart}"
	exit 1
esac

exit 0

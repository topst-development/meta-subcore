#! /bin/sh
### BEGIN INIT INFO
# Provides:             Qt Cluser Demo Application
# Required-Start:
# Required-Stop:
# Default-Start:        5
# Default-Stop:         0
# Short-Description:    Qt Cluster Demo Application
# Description:          Qt Cluster Demo Application
### END INIT INFO
#
# -*- coding: utf-8 -*-
# Debian init.d script for Qt Cluster Demo Application
# Copyright © 2014 Wily Taekhyun Shin <thshin@telechips.com>

# Source function library.

DAEMON=/usr/bin/qtcluster
DESC="qt cluster demo application"
ARGUMENTS="-plugin evdevkeyboard"

test -x $DAEMON || exit 0

case "$1" in
  start)
	. /etc/profile.d/profile_local.sh
  	echo -n "Starting $DESC: " > /dev/kmsg
	$DAEMON $ARGUMENTS &
  	echo "done." > /dev/kmsg
	;;
  stop)
  	echo -n "Stopping $DESC: "
	killall qtcluster
  	echo "done."
	;;
  *)
	echo "Usage: /etc/init.d/qt-cluster {start|stop}"
	exit 1
esac

exit 0

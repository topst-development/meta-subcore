#! /bin/sh
### BEGIN INIT INFO
# Provides:             Trust RVC Application
# Required-Start:
# Required-Stop:
# Default-Start:        1
# Default-Stop:         0
# Short-Description:    Trust RVC Application
# Description:          Trust RVC Application
### END INIT INFO

# Source function library.

DAEMON=/usr/bin/Trvc
DESC="Trust RVC application"
ARGUMENTS=""

test -x $DAEMON || exit 0

case "$1" in
  start)
	. /etc/profile.d/profile_local.sh
	export QT_QPA_EGLFS_FB=/dev/fb3
  	echo -n "Starting $DESC: " > /dev/kmsg
	$DAEMON $ARGUMENTS &
  	echo "done." > /dev/kmsg
	;;
  stop)
  	echo -n "Stopping $DESC: "
	killall Trvc
  	echo "done."
	;;
  *)
	echo "Usage: /etc/init.d/Trvc {start|stop}"
	exit 1
esac

exit 0

#!/bin/bash

PID=$(/bin/pidof camera_app)

case $1 in
	hibernate|suspend)
		echo "rvc PID = $PID"
		echo `cat /proc/$PID/wchan`
	
		if [ -z $PID ]; then
			echo "Not fid rvc"
			exit $NA
		else
			echo "suspend rvc"

			kill -20 $PID

			WAIT_LOOP=0
			status=`cat /proc/$PID/wchan`

			while [ $status != "do_signal_stop" ] ; do
				sleep 0.1
				WAIT_LOOP=$((WAIT_LOOP+1))
				if [ $WAIT_LOOP -eq 30 ]; then
					break
				fi
				status=`cat /proc/$PID/wchan`
				echo "wait $WAIT_LOOP, $status"
			done

			if [ "$status" == "do_signal_stop" ] ; then
				echo "$PID sleeps: $status"
			else
				echo "$PID does not sleep: $status"
			fi

		fi
		;;
	thaw|resume)
		echo `cat /proc/$PID/wchan`
		echo "hey, the suspend to RAM seems to be over..."
		kill -18 $PID
		;;

	*)  exit $NA
		;;
esac

#! /bin/sh
#/usr/bin/tcc_mem w 0x14200180 0x140
#/usr/bin/tcc_mem w 0x14200030 0x000
#sleep 3
#
#/usr/bin/tcc_mem w 0x14200030 0x33333003
#sleep 2

echo c > /proc/sysrq-trigger

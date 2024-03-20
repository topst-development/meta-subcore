DESCRIPTION = "TRVC Demo Applications"
SECTION = "applications"
LICENSE = "CLOSED"

SRC_URI = "file://trvc.tar.gz \
		   file://trvc.init.sh \
		   file://syncerror.sh \
		   file://tcc_mem \
"

inherit qmake5 update-rc.d

DEPENDS += "qtbase libtcutils libtcguiutils"

S = "${WORKDIR}/trvc"
B = "${S}"

INIT_NAME = "trvc"

INITSCRIPT_NAME = "${INIT_NAME}"
INITSCRIPT_PARAMS = "start 92 S . stop 20 0 1 6 ."

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/trvc.init.sh ${D}${sysconfdir}/init.d/${INIT_NAME}

	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/syncerror.sh ${D}${sbindir}/syncerror.sh

	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/tcc_mem ${D}${bindir}/tcc_mem
}

FILES_${PN} += "${sysconfdir} ${sbindir} ${bindir}"
INSANE_SKIP_${PN} += "already-stripped"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', ' file://tc-update-engine.service', ' file://tc-update-engine.init.sh', d)} \
"
UPDATE_RCD := "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'update-rc.d', d)}"

EXTRA_OECONF_append = " --enable-standalone-app"

inherit ${UPDATE_RCD}

# for systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "tc-update-engine.service"

# for sysvinit
INIT_NAME = "tc-update-engine"

INITSCRIPT_NAME = "${INIT_NAME}"
INITSCRIPT_PARAMS = "start 40 S . stop 20 0 1 6 ."

do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
		install -d ${D}${sysconfdir}/init.d
		install -m 0755 ${WORKDIR}/tc-update-engine.init.sh ${D}${sysconfdir}/init.d/${INIT_NAME}
	else
		install -d ${D}/${systemd_unitdir}/system
		install -m 644 ${WORKDIR}/tc-update-engine.service ${D}/${systemd_unitdir}/system
	fi
}

FILES_${PN} += " \
		${sysconfdir} \
		${localstatedir} \
		${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}', '', d)} \
"


DESCRIPTION = "Telechips Rear View Camera Applications for Telechips Automoitve Linux SDK"
SECTION = "applications"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=8ca43cbc842c2336e835926c2166c28b"

#ALS_BRANCH ??= "${@oe.utils.conditional('LINUXLIBCVERSION', '5.4', 'v4.x_k54', 'v4.x', d)}"

SRC_URI = "${TELECHIPS_AUTOMOTIVE_APP_GIT}/tc-camera-app.git;protocol=${ALS_GIT_PROTOCOL};branch=${ALS_BRANCH} \
	file://tc-rvc-app.init.sh \
	file://tc-rvc-app.service \
    file://default \
	file://suspend-rvc.sh \
"
#SRCREV = "419165186618f10eede64502a70a6a975af9fa6e"
SRCREV = "${AUTOREV}"

UPDATE_RCD := "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'update-rc.d', d)}"

#inherit autotools pkgconfig ${UPDATE_RCD}
inherit autotools pkgconfig

DEPENDS += "virtual/kernel"
PATCHTOOL = "git"

S = "${WORKDIR}/git"

RDEPENDS_${PN} += "bash"

# for systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "tc-rvc-app.service"

# for sysvinit
INIT_NAME = "tc-rvc-app"
INITSCRIPT_NAME = "${INIT_NAME}"
INITSCRIPT_PARAMS = "start 03 S . stop 20 0 1 6 ."

EXTRA_OECONF += "KERNEL_DIR=${STAGING_KERNEL_DIR}"
CFLAGS_append = " -I${STAGING_KERNEL_BUILDDIR}/include"


do_install_append() {
    install -Dm 0644 ${WORKDIR}/default ${D}${sysconfdir}/default/tc-rvc

	#if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
	#	install -d ${D}${sysconfdir}/init.d
	#	install -m 0755 ${WORKDIR}/tc-rvc-app.init.sh ${D}${sysconfdir}/init.d/${INIT_NAME}
	#else
	#	install -d ${D}/${systemd_unitdir}/system
	#	install -m 644 ${WORKDIR}/tc-rvc-app.service ${D}/${systemd_unitdir}/system
	#fi

	sed -i "s%RVC_SW_NUM%${RVC_SW_NUM}%g"		${D}${sysconfdir}/default/tc-rvc
	sed -i "s%RVC_RECOVERY%${RVC_RECOVERY}%g"	${D}${sysconfdir}/default/tc-rvc

	if ${@bb.utils.contains('INVITE_PLATFORM', 'str', 'true', 'false', d)}; then
		install -d ${D}${bindir}
		install -m 0755 ${WORKDIR}/suspend-rvc.sh ${D}${bindir}/
	fi

}

FILES_${PN} += " \
		${sysconfdir} \
		${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_unitdir}', '', d)} \
"

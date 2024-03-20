FILESEXTRAPATHS_append = " :${THISDIR}/${PN}/${TCC_ARCH_FAMILY}"
DESCRIPTION = "Qt Cluster demo application"
SECTION = "applications"
LICENSE = "CLOSED"

SRC_URI = "file://qtcluster"
SRC_URI[md5sum] = "1ebda9bf6150f2b2d8945c6691ea4dd2"
SRC_URI[sha256sum] = "a2d5539e7a6f608d193a2cee1a6a475f03a2acc8daeaf5ca454afc9c3447440e"

SRC_URI += "${@bb.utils.contains("DISTRO_FEATURES", 'systemd', 'file://qt-cluster.service', 'file://qt-cluster.init.sh', d)}"

# check mandatory features
REQUIRED_DISTRO_FEATURES = "opengl"

# check conflict features when using arm
CONFLICT_DISTRO_FEATURES += "wayland"
UPDATE_RCD := "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', 'update-rc.d', d)}"

inherit autotools pkgconfig ${UPDATE_RCD} features_check

# for systemd
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "qt-cluster.service"

# for sysvinit
INIT_NAME = "qt-cluster"
INITSCRIPT_NAME = "${INIT_NAME}"
INITSCRIPT_PARAMS = "start 02 S . stop 30 0 1 6 ."

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -d 	${D}${bindir}
	install -m 0755 ${WORKDIR}/qtcluster	${D}${bindir}

	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -d ${D}/${systemd_unitdir}/system
		install -m 644 ${WORKDIR}/qt-cluster.service  											${D}${systemd_unitdir}/system/

		sed -i 's%\(^Environment=QT_QPA_EGLFS_PHYSICAL_WIDTH=\)LCD_WIDTH%\1${LCD_WIDTH}%g'      ${D}${systemd_unitdir}/system/qt-cluster.service
		sed -i 's%\(^Environment=QT_QPA_EGLFS_PHYSICAL_HEIGHT=\)LCD_HEIGHT%\1${LCD_HEIGHT}%g'   ${D}${systemd_unitdir}/system/qt-cluster.service
		sed -i 's%\(^Environment=QT_QPA_EGLFS_WIDTH=\)LCD_WIDTH%\1${LCD_WIDTH}%g'      			${D}${systemd_unitdir}/system/qt-cluster.service
		sed -i 's%\(^Environment=QT_QPA_EGLFS_HEIGHT=\)LCD_HEIGHT%\1${LCD_HEIGHT}%g'   			${D}${systemd_unitdir}/system/qt-cluster.service
	else
		install -d ${D}${sysconfdir}/init.d
		install -m 0755 ${WORKDIR}/qt-cluster.init.sh ${D}${sysconfdir}/init.d/${INIT_NAME}
	fi
}

FILES_${PN} += " \
	${bindir} \
	${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', '${sysconfdir}', '', d)} \
"
RDEPENDS_${PN} += "libgles2-telechips libegl-telechips libdrm"
RDEPENDS_${PN}_append_tcc803x = " libmali-telechips"
RDEPENDS_${PN}_append_tcc805x = " libudev"

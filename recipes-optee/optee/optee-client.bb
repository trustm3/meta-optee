SUMMARY = "OPTEE Client"
HOMEPAGE = "https://github.com/OP-TEE/optee_client"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=69663ab153298557a59c67a60a743e5b"

PV = "3.6.0+git${SRCPV}"

inherit python3native systemd

SRC_URI = "git://github.com/OP-TEE/optee_client.git \
           file://tee-supplicant.service"
S = "${WORKDIR}/git"

#SRCREV = "c48bc3be9f23529952c7dd80ddd775bf580315b8"
SRCREV = "master"

SYSTEMD_SERVICE_${PN} = "tee-supplicant.service"

do_install() {
    oe_runmake install

    install -D -p -m0755 ${S}/out/export/usr/sbin/tee-supplicant ${D}${sbindir}/tee-supplicant

    install -D -p -m0644 ${S}/out/export/usr/lib/libteec.so.1.0 ${D}${libdir}/libteec.so.1.0
    ln -sf libteec.so.1.0 ${D}${libdir}/libteec.so
    ln -sf libteec.so.1.0 ${D}${libdir}/libteec.so.1

    cp -a ${S}/out/export/usr/include ${D}/usr/

    sed -i -e s:/etc:${sysconfdir}:g \
           -e s:/usr/bin:${sbindir}:g \
              ${WORKDIR}/tee-supplicant.service

    install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
}

SUMMARY = "OP-TEE Trusted OS"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

PV="3.6.0+git${SRCPV}"

DEPENDS = "python3-pycrypto-native python3-pyelftools-native"

inherit deploy python3native

SRCREV = "3.6.0"
SRC_URI = "git://github.com/OP-TEE/optee_os.git \
           file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
           file://0002-set-optee-dram-shm-size.patch \
          "

S = "${WORKDIR}/git"

OPTEEMACHINE ?= "${MACHINE}"
OPTEEOUTPUTMACHINE ?= "${MACHINE}"

export CROSS_COMPILE="${TARGET_PREFIX}"

CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

DEBUG ??= "1"
TEE_LOG_LEVEL = "${@bb.utils.contains('DEBUG', '1', '4', '2', d)}"
TEE_CORE_DEBUG = "${@bb.utils.contains('DEBUG', '1', 'y', 'n', d)}"

EXTRA_OEMAKE = "\
	LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
	CROSS_COMPILE=${CROSS_COMPILE} \
	CROSS_COMPILE_core=${CROSS_COMPILE} \
	CROSS_COMPILE_ta_arm64=${CROSS_COMPILE} \
	PLATFORM=${OPTEEMACHINE} \
	CFG_ARM64_core=y \
	CFG_ARM32_core=n \
	ta-targets=ta_arm64 \
	CFG_USER_TA_TARGETS=ta_arm64 \
	CFG_TEE_CORE_LOG_LEVEL=${TEE_LOG_LEVEL} \
	CFG_TEE_CORE_DEBUG=${TEE_CORE_DEBUG} \
	CFG_DT=n \
	DEBUG=${DEBUG} \
"

OPTEE_ARCH_armv7a = "arm32"
OPTEE_ARCH_aarch64 = "arm64"

do_compile() {
    unset LDFLAGS
    oe_runmake all CFG_TEE_TA_LOG_LEVEL=0
}

do_install() {
    #install core on boot directory
    install -d ${D}${nonarch_base_libdir}/firmware/

    install -m 644 ${B}/out/arm-plat-${OPTEEOUTPUTMACHINE}/core/*.bin ${D}${nonarch_base_libdir}/firmware/
    install -m 644 ${B}/out/arm-plat-${OPTEEOUTPUTMACHINE}/core/*.elf ${D}${nonarch_base_libdir}/firmware/
    #install TA devkit
    install -d ${D}/usr/include/optee/export-user_ta/

    for f in  ${B}/out/arm-plat-${OPTEEOUTPUTMACHINE}/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR  $f  ${D}/usr/include/optee/export-user_ta/
    done
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_deploy() {
    install -d ${DEPLOYDIR}/optee
    for f in ${D}${nonarch_base_libdir}/firmware/*; do
        install -m 644 $f ${DEPLOYDIR}/optee/
    done
}

addtask deploy before do_build after do_install

FILES_${PN} = "${nonarch_base_libdir}/firmware/"
FILES_${PN}-dev = "/usr/include/optee"

INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"

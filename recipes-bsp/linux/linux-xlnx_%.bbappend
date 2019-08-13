#SRC_URI += "file://0001-arch-arm64-Kconfig.platforms-Allow-to-build-without-.patch"
SRC_URI += "file://0001-arch-arm64-boot-dts-xilinx-zynqmp-zcu104-revC.dts-op.patch"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"


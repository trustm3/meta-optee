
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

OPTEE_DTSI = "optee.dtsi"

COMPATIBLE_MACHINE_zynqmp = ".*"
SRC_URI_append_zynqmp = " file://${OPTEE_DTSI}"

do_configure_append() {
        cp ${WORKDIR}/${OPTEE_DTSI} ${B}/device-tree
        echo "/include/ \"${OPTEE_DTSI}\"" >> ${B}/device-tree/system-top.dts
}


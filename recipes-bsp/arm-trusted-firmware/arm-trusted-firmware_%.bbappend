FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

COMPATIBLE_MACHINE_zynqmp = ".*"
SRC_URI_append_zynqmp = " file://0001-zynqmp-place-atf-in-ocm-memory-even-if-bl32-is-used.patch"


#DEBUG = "1"
#EXTRA_OEMAKE_zynqmp_append = " ZYNQMP_ATF_MEM_BASE=0x50000000 ZYNQMP_ATF_MEM_SIZE=0x80000"
#ATF_CONSOLE_zynqmp = "cadence1"
EXTRA_OEMAKE_zynqmp_append = " SPD=opteed ZYNQMP_BL32_MEM_BASE=0x60000000 ZYNQMP_BL32_MEM_SIZE=0x80000"

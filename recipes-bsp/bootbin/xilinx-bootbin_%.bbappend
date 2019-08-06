BIF_PARTITION_ATTR_zynqmp = "fsbl pmu atf u-boot bl32"
BIF_PARTITION_ATTR[bl32] = "destination_cpu=a53-0,exception_level=el-1,trustzone"
BIF_PARTITION_IMAGE[bl32] = "${DEPLOY_DIR_IMAGE}/optee/tee.elf"
BIF_PARTITION_DEPENDS[bl32] = "optee-os:do_deploy"

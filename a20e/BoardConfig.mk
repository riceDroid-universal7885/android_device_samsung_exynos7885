DEVICE_PATH := device/samsung/a20e

# Asserts
TARGET_OTA_ASSERT_DEVICE := a20e,a20edd

# Keymaster
TARGET_KEYMASTER_VARIANT := samsung

# Kernel
TARGET_KERNEL_CONFIG := exynos7885-a20e_permissive_defconfig

# Display
TARGET_SCREEN_DENSITY := 320

# Partitions
BOARD_RECOVERYIMAGE_PARTITION_SIZE := 55574528 # 55MB
BOARD_SYSTEMIMAGE_PARTITION_SIZE := 4320133120 #4.02GB

# Recovery
TARGET_RECOVERY_FSTAB := $(DEVICE_PATH)/recovery/recovery.fstab

# Sepolicy
BOARD_SEPOLICY_TEE_FLAVOR := teegris

# Inherit common board flags
include device/samsung/universal7885-common/BoardConfigCommon.mk

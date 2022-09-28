# Copyright (C) 2018 The LineageOS Project
# SPDX-License-Identifier: Apache-2.0

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/product_launched_with_p.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit device configuration
$(call inherit-product, device/samsung/a10dd/device.mk)

# Device identifier. This must come after all inclusions
PRODUCT_DEVICE := a10dd
PRODUCT_NAME := full_a10dd
PRODUCT_MODEL := SM-A105F
PRODUCT_BRAND := samsung
PRODUCT_MANUFACTURER := samsung

RICE_MAINTAINER := eun0115
RICE_CHIPSET := universal7884B
SUSHI_BOOTANIMATION := 720
TARGET_BUILD_GRAPHENEOS_CAMERA := true
TARGET_FACE_UNLOCK_SUPPORTED := true
RICE_OFFICIAL := true
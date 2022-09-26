DEVICE_PACKAGE_OVERLAYS += $(LOCAL_PATH)/overlay

TARGET_LOCAL_ARCH := arm64

# Inherit common device configuration
$(call inherit-product, device/samsung/universal7885-common/universal7885-common.mk)

$(call inherit-product, vendor/samsung/a40/a40-vendor.mk)

$(call inherit-product, frameworks/native/build/phone-xhdpi-4096-dalvik-heap.mk)

# USB
PRODUCT_PACKAGES += \
    android.hardware.usb-service.samsung

TARGET_SCREEN_HEIGHT := 2280
TARGET_SCREEN_WIDTH := 1080

# Feature Permissions
PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.fingerprint.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.fingerprint.xml \
    frameworks/native/data/etc/android.hardware.sensor.light.xml:$(TARGET_COPY_OUT_VENDOR)/etc/permissions/android.hardware.sensor.light.xml
    
PRODUCT_PACKAGES += \
   fstab.exynos7904

PRODUCT_PACKAGES += \
   android.hardware.sensors@1.0-service

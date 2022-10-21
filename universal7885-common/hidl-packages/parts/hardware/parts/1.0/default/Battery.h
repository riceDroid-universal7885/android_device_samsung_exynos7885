// Copyright (C) 2021 Eureka Team
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

#pragma once

#include <hidl/MQDescriptor.h>
#include <hidl/Status.h>
#include <vendor/eureka/hardware/parts/1.0/IBatteryStats.h>

#define ANDROID_SYSTEM_UID 1000
#define ANDROID_ROOT_UID 0

namespace vendor::eureka::hardware::parts::V1_0 {

using ::android::sp;
using ::android::hardware::Return;
using ::android::hardware::Void;

struct BatteryStats : public IBatteryStats {
  // Methods from ::vendor::eureka::hardware::parts::V1_0::IBatteryStats follow.
  Return<int32_t> getBatteryStats(BatterySys stats) override;
  Return<void> setBatteryWritable(BatterySys stats, Status value) override;

  // Methods from ::android::hidl::base::V1_0::IBase follow.
  static IBatteryStats *getInstance(void);
};
} // namespace vendor::eureka::hardware::parts::V1_0

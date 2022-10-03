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
#include <vendor/eureka/hardware/parts/1.0/IDisplayConfigs.h>

namespace vendor::eureka::hardware::parts::V1_0 {

using ::android::sp;
using ::android::hardware::Return;
using ::android::hardware::Void;

struct DisplayConfigs : public IDisplayConfigs {
  // Methods from ::vendor::eureka::hardware::parts::V1_0::IDisplayConfigs
  // follow.
  Return<void> writeDisplay(Status enable, DisplaySys type);
  // Methods from ::android::hidl::base::V1_0::IBase follow.
  static IDisplayConfigs *getInstance(void);
};
} // namespace vendor::eureka::hardware::parts::V1_0

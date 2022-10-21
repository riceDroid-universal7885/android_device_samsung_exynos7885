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

#include <android-base/logging.h>
#include <android/binder_manager.h>
#include <android/binder_process.h>

#include "FMSupport.h"
#include "FMDevControl.h"

using ::aidl::vendor::eureka::hardware::fmradio::FMDevControl;
using ::aidl::vendor::eureka::hardware::fmradio::FMSupport;

template <class C> static void registerAsService(std::shared_ptr<C> service, const char *inst) {
  const std::string instance = std::string() + C::descriptor + "/" + inst;
  binder_status_t status =
      AServiceManager_addService(service->asBinder().get(), instance.c_str());
  CHECK(status == STATUS_OK);
  LOG(INFO) << "Register done for instance " << inst;
}

int main() {
  ABinderProcess_setThreadPoolMaxThreadCount(0);

  registerAsService(ndk::SharedRefBase::make<FMSupport>(), "support");
  registerAsService(ndk::SharedRefBase::make<FMDevControl>(), "default");

  ABinderProcess_joinThreadPool();
  return -1; // should never get here
}

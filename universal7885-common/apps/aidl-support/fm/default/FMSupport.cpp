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

#include "FMSupport.h"

#include <algorithm>

#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#include <FileIO.h>

#include "CommonMacro.h"

namespace aidl::vendor::eureka::hardware::fmradio {

#define FM_SYSFS_BASE "/sys/devices/virtual/s610_radio/s610_radio"
constexpr const char *FM_FREQ_CTL = FM_SYSFS_BASE "/radio_freq_ctrl";
constexpr const char *FM_FREQ_SEEK = FM_SYSFS_BASE "/radio_freq_seek";

::ndk::ScopedAStatus FMSupport::open(void) { NOT_SUPPORTED; }

::ndk::ScopedAStatus FMSupport::getValue(GetType type, int *_aidl_return) {
  RETURN_IF_FAILED_LOCK;
  switch (type) {
  case GetType::GET_TYPE_FM_FREQ:
    *_aidl_return = FileIO::readline(FM_FREQ_CTL);
    break;
  case GetType::GET_TYPE_FM_UPPER_LIMIT:
  case GetType::GET_TYPE_FM_LOWER_LIMIT:
  case GetType::GET_TYPE_FM_RMSSI:
    NOT_SUPPORTED;
  case GetType::GET_TYPE_FM_BEFORE_CHANNEL:
    FileIO::writeline(FM_FREQ_SEEK, "0 " + std::to_string(SYSFS_SPACING * 10));
    *_aidl_return = FileIO::readline(FM_FREQ_CTL);
    break;
  case GetType::GET_TYPE_FM_NEXT_CHANNEL:
    FileIO::writeline(FM_FREQ_SEEK, "1 " + std::to_string(SYSFS_SPACING * 10));
    *_aidl_return = FileIO::readline(FM_FREQ_CTL);
    break;
  case GetType::GET_TYPE_FM_SYSFS_IF:
    *_aidl_return = access(FM_SYSFS_BASE, F_OK);
    break;
  case GetType::GET_TYPE_FM_MUTEX_LOCKED:
    if (lock.try_lock()) {
       *_aidl_return = false;
       lock.unlock();
    } else {
       *_aidl_return = true;
    }
    break;
  default:
    break;
  };
  lock.unlock();
  return ndk::ScopedAStatus::ok();
}
::ndk::ScopedAStatus FMSupport::setValue(SetType type, int value) {
  RETURN_IF_FAILED_LOCK;
  switch (type) {
  case SetType::SET_TYPE_FM_FREQ:
    FileIO::writeline(FM_FREQ_CTL, value * 1000);
    break;
  case SetType::SET_TYPE_FM_MUTE:
  case SetType::SET_TYPE_FM_VOLUME:
  case SetType::SET_TYPE_FM_THREAD:
  case SetType::SET_TYPE_FM_RMSSI:
  case SetType::SET_TYPE_FM_SEARCH_CANCEL:
  case SetType::SET_TYPE_FM_SPEAKER_ROUTE:
    NOT_SUPPORTED;
  case SetType::SET_TYPE_FM_SEARCH_START:
    search_thread = std::thread([this] {
        for (int i = 0; i < TRACK_SIZE; i++) {
              FileIO::writeline(FM_FREQ_SEEK, "1 " + std::to_string(SYSFS_SPACING * 10));
              int freq = FileIO::readline(FM_FREQ_CTL);
              if (std::find(freqs_list.begin(), freqs_list.end(), freq) != freqs_list.end()) continue;
              freqs_list.push_back(freq);
        }
        lock.unlock();
    });
    break;
  default:
    break;
  };
  if (type != SetType::SET_TYPE_FM_SEARCH_START) lock.unlock();
  return ::ndk::ScopedAStatus::ok();
}

::ndk::ScopedAStatus FMSupport::getFreqsList(std::vector<int> *_aidl_return) {
  RETURN_IF_FAILED_LOCK;
  *_aidl_return = freqs_list;
  lock.unlock();
  return ::ndk::ScopedAStatus::ok();
}
::ndk::ScopedAStatus FMSupport::close() { NOT_SUPPORTED; }
} // namespace aidl::vendor::eureka::hardware::fmradio

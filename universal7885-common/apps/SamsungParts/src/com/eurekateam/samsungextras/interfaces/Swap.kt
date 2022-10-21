/*
 * Copyright (C) 2022 Eureka Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eurekateam.samsungextras.interfaces

import vendor.eureka.hardware.parts.ISwapOnData

import android.os.ServiceManager

class Swap {
    private val mSwap : ISwapOnData
    init {
       mSwap = ISwapOnData.Stub.asInterface(ServiceManager.waitForDeclaredService("vendor.eureka.hardware.parts.ISwapOnData/default"))
    }

    fun setSwapOn(mEnabled: Boolean) = if (mEnabled) mSwap.setSwapOn() else mSwap.setSwapOff()
    fun mkFile(mSize: Int) = mSwap.makeSwapFile(mSize)
    fun delFile() = mSwap.removeSwapFile()
    external fun getFreeSpace(): Double
    external fun getSwapSize(): Long
}

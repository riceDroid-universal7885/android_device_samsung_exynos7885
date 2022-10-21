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

import vendor.eureka.hardware.parts.IFlashBrightness

import android.os.ServiceManager

class Flashlight {
    private val mFlash : IFlashBrightness

    init {
       mFlash = IFlashBrightness.Stub.asInterface(ServiceManager.waitForDeclaredService("vendor.eureka.hardware.parts.IFlashBrightness/default"))
    }

    fun setFlash(value: Int) = mFlash.setFlashlightWritable(value)
     
    fun getFlash(a10: Boolean): Int = mFlash.readFlashlightstats(!a10)
    fun setEnabled(enable: Boolean) = mFlash.setFlashlightEnable(enable)
}

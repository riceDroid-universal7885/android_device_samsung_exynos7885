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
package com.eurekateam.samsungextras.smartcharge

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SeekBarPreference
import com.android.settingslib.widget.ButtonPreference
import com.android.settingslib.widget.MainSwitchPreference
import com.android.settingslib.widget.OnMainSwitchChangeListener
import com.android.settingslib.widget.SelectorWithWidgetPreference
import com.eurekateam.samsungextras.R
import com.eurekateam.samsungextras.interfaces.StatsType
import com.eurekateam.samsungextras.interfaces.SmartCharge
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class SmartChargeFragment : PreferenceFragmentCompat(), OnMainSwitchChangeListener, View.OnClickListener, Preference.OnPreferenceChangeListener, SelectorWithWidgetPreference.OnClickListener {
    private lateinit var mLimit: SelectorWithWidgetPreference
    private lateinit var mRestart: SelectorWithWidgetPreference
    private lateinit var mSharedPreferences: SharedPreferences
    private lateinit var mSmartChargeBtn: MainSwitchPreference
    private lateinit var mAdjust: SeekBarPreference
    private lateinit var mApplyBtn: ButtonPreference
    private lateinit var mLimitShow: Preference
    private lateinit var mRestartShow: Preference
    private lateinit var mLimitStat: Preference
    private lateinit var mRestartStat: Preference
    private val mPoolExecutor = ScheduledThreadPoolExecutor(3)
    private var mSelected = SelectedOption.SELECTED_LIMIT
    private val mSmartCharge = SmartCharge()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.smartcharge_settings)
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())

        mSmartChargeBtn = findPreference(PREF_SMARTCHARGE_MAIN)!!
        mAdjust = findPreference(PREF_ADJUST)!!
        mLimit = findPreference(PREF_LIMIT_SELECT)!!
        mRestart = findPreference(PREF_RESTART_SELECT)!!

        mLimitShow = findPreference(PREF_LIMIT)!!
        mRestartShow = findPreference(PREF_RESTART)!!
        mLimitStat = findPreference(PREF_LIMIT_STAT)!!
        mRestartStat = findPreference(PREF_RESTART_STAT)!!
        mApplyBtn = findPreference(PREF_APPLY)!!

        mApplyBtn.setOnClickListener(this)
        mApplyBtn.isEnabled = false
        mSmartChargeBtn.addOnSwitchChangeListener(this)
        mAdjust.min = 1
        mAdjust.max = 5
        mAdjust.value = 3
        mAdjust.onPreferenceChangeListener = this
        mLimit.setOnClickListener(this)
        mRestart.setOnClickListener(this)
    }

    override fun onRadioButtonClicked(btn: SelectorWithWidgetPreference) {
        when (btn) {
            mLimit -> {
                mSelected = SelectedOption.SELECTED_LIMIT
                mRestart.isChecked = false
            }
            mRestart -> {
                mSelected = SelectedOption.SELECTED_RESTART
                mLimit.isChecked = false
            }
            else -> {}
        }
    }

    private inline fun fromStringToNum(s: String): Int {
        return if (s.contains("ten")) 10 else if (s.contains("one")) 1 else 0
    }

    private inline fun fromSelectedToId(): String = when (mSelected) {
        SelectedOption.SELECTED_LIMIT -> PREF_LIMIT
        SelectedOption.SELECTED_RESTART -> PREF_RESTART
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference == mAdjust) {
            var tmp = mSharedPreferences.getInt(fromSelectedToId(), 50)
            val toAdd = when (newValue as Int) {
                0 -> -10
                1 -> -1
                3 -> 1
                4 -> 10
                else -> 0
            }
            if (tmp + toAdd > 99) tmp = 99 else if (tmp + toAdd < 1) tmp = 1 else tmp += toAdd
            mSharedPreferences.edit().putInt(fromSelectedToId(), tmp).apply()
            when (mSelected) {
                SelectedOption.SELECTED_LIMIT -> mLimitShow
                SelectedOption.SELECTED_RESTART -> mRestartShow
            }.summary = "$tmp %"
            mSmartChargeBtn.isEnabled = false
            mApplyBtn.isEnabled = true
            mAdjust.value = 3
            return true
        }
        return false
    }

    override fun onClick(v: View) {
        if (v == mApplyBtn) {
            val limit = mSharedPreferences.getInt(PREF_LIMIT, 20)
            val restart = mSharedPreferences.getInt(PREF_RESTART, 80)
            if (limit < restart) {
                mSmartCharge.setConfig(limit, restart)
                mSmartChargeBtn.isEnabled = true
                mApplyBtn.isEnabled = false
            }
        }
    }

    private val mScheduler = Runnable {
        requireActivity().runOnUiThread {
            mLimitStat.summary = "${mSmartCharge.getStats(StatsType.TYPE_LIMITED_CNT)} times"
            mRestartStat.summary = "${mSmartCharge.getStats(StatsType.TYPE_RESTARTED_CNT)} times"
        }
    }

    override fun onSwitchChanged(switchView: Switch, isChecked: Boolean) {
        if (isChecked) {
            mSmartCharge.start()
            mPoolExecutor.scheduleWithFixedDelay(mScheduler, 0, 5, TimeUnit.MINUTES)
        } else {
            mSmartCharge.stop()
            mPoolExecutor.shutdown()
        }
    } 
    companion object {
        const val PREF_SMARTCHARGE_MAIN = "smartcharge"
        const val PREF_LIMIT_SELECT = "choose_limit"
        const val PREF_RESTART_SELECT = "choose_restart"
        const val PREF_LIMIT = "limit"
        const val PREF_RESTART = "restart"
        const val PREF_LIMIT_STAT = "limit_stat"
        const val PREF_RESTART_STAT = "restart_stat"
        const val PREF_APPLY = "apply"
        const val PREF_ADJUST = "adjust"
    }
}

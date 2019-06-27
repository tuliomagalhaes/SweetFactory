package com.tuliohdev.modularapplication

import android.content.Context
import android.content.Intent
import androidx.fragment.app.DialogFragment

interface FeatureBFactory {

    fun newIntentForFeatureBActivity(context : Context): Intent

    fun newInstanceFeatureBDialogFragment(): DialogFragment

}

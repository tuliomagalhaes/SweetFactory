package com.tuliohdev.modularapplication

import android.content.Context
import android.content.Intent

interface FeatureAActivityIntentFactory {

    fun newIntent(context: Context): Intent

}

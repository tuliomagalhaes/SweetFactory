package com.tuliohdev.modularapplication.navigation

import android.content.Context
import android.content.Intent

interface FeatureAActivityIntentFactory {

    fun featureAIntent(context: Context): Intent

}
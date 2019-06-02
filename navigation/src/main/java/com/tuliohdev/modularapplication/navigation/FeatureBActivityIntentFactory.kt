package com.tuliohdev.modularapplication.navigation

import android.content.Context
import android.content.Intent

interface FeatureBActivityIntentFactory {

    fun featureBIntent(context: Context): Intent

}
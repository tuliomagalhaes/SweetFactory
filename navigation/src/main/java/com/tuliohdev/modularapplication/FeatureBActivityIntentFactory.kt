package com.tuliohdev.modularapplication

import android.content.Context
import android.content.Intent

interface FeatureBActivityIntentFactory {

    fun newIntent(context : Context): Intent

}
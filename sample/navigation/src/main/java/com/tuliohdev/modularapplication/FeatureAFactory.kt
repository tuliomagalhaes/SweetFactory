package com.tuliohdev.modularapplication

import android.content.Context
import android.content.Intent

interface FeatureAFactory {

    fun newIntent(context: Context): Intent

}

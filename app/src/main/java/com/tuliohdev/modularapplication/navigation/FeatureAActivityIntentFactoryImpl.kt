package com.tuliohdev.modularapplication.navigation

import android.content.Context
import android.content.Intent
import com.tuliohdev.featurea.FeatureAActivity

class FeatureAActivityIntentFactoryImpl : FeatureAActivityIntentFactory {

    override fun featureAIntent(context: Context): Intent = Intent(context, FeatureAActivity::class.java)

}
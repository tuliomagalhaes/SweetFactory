package com.tuliohdev.modularapplication.navigation

import android.content.Context
import android.content.Intent
import com.tuliohdev.featureb.FeatureBActivity

class FeatureBActivityIntentFactoryImpl : FeatureBActivityIntentFactory {

    override fun featureBIntent(context: Context): Intent = Intent(context, FeatureBActivity::class.java)

}
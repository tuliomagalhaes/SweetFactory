package com.tuliohdev.featureb

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tuliohdev.modularapplication.navigation.FeatureAActivityIntentFactory
import com.tuliohdev.modularapplication.navigation.IntentFactory

class FeatureBActivity : AppCompatActivity() {

    var featureANavigator = IntentFactory.getInstance(FeatureAActivityIntentFactory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feature_b)

        findViewById<Button>(R.id.btOpenFeatureA).setOnClickListener {
            startActivity(featureANavigator.featureAIntent(this))
        }
    }
}
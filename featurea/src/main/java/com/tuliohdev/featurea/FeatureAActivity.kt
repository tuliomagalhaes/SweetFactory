package com.tuliohdev.featurea

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tuliohdev.modularapplication.navigation.FeatureBActivityIntentFactory
import com.tuliohdev.modularapplication.navigation.IntentFactory

class FeatureAActivity : AppCompatActivity() {

    var featureBNavigator = IntentFactory.getInstance(FeatureBActivityIntentFactory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feature_a)

        findViewById<Button>(R.id.btOpenFeatureB).setOnClickListener {
            startActivity(featureBNavigator.featureBIntent(this))
        }
    }

}
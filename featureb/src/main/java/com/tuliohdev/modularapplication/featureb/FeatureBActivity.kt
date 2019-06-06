package com.tuliohdev.modularapplication.featureb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tuliohdev.modularapplication.FeatureAActivityIntentFactory
import com.tuliohdev.modularapplication.FeatureBActivityIntentFactory

import sweetfactory.SweetFactory
import sweetfactory.annotations.IntentFactory
import sweetfactory.annotations.IntentFactoryMethod

@IntentFactory(forInterface = FeatureBActivityIntentFactory::class)
class FeatureBActivity : AppCompatActivity() {

    companion object {
        @IntentFactoryMethod
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, FeatureBActivity::class.java)
        }
    }

    private var featureANavigator = SweetFactory.newInstanceOf(FeatureAActivityIntentFactory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feature_b)

        findViewById<Button>(R.id.btOpenFeatureA).setOnClickListener {
            startActivity(featureANavigator.newIntent(this))
        }
    }
}
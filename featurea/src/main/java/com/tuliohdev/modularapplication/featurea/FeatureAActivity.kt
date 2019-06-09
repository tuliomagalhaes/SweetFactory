package com.tuliohdev.modularapplication.featurea

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

@IntentFactory(forInterface = FeatureAActivityIntentFactory::class)
class FeatureAActivity : AppCompatActivity() {

    companion object {
        @IntentFactoryMethod
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, FeatureAActivity::class.java)
        }
    }

    private var featureBNavigator = SweetFactory.newInstanceOf(FeatureBActivityIntentFactory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feature_a)

        findViewById<Button>(R.id.btOpenFeatureB).setOnClickListener {
            startActivity(featureBNavigator?.newIntent(this))
        }
    }

}
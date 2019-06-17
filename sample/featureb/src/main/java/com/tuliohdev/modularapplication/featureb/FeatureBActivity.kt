package com.tuliohdev.modularapplication.featureb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tuliohdev.modularapplication.FeatureAFactory
import com.tuliohdev.modularapplication.FeatureBFactory
import sweetfactory.SweetFactory
import sweetfactory.annotations.SweetFactoryDeclaration

import sweetfactory.annotations.SweetFactoryMethod

@SweetFactoryDeclaration(factory = FeatureBFactory::class)
class FeatureBActivity : AppCompatActivity() {

    companion object {
        @SweetFactoryMethod
        @JvmStatic
        fun newIntentForFeatureBActivity(context: Context): Intent {
            return Intent(context, FeatureBActivity::class.java)
        }
    }

    private var featureANavigator = SweetFactory.newInstanceOf(FeatureAFactory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feature_b)

        findViewById<Button>(R.id.btOpenFeatureA).setOnClickListener {
            startActivity(featureANavigator?.newIntent(this))
        }
    }
}
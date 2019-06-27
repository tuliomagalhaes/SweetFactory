package com.tuliohdev.modularapplication.featurea

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

@SweetFactoryDeclaration(factory = FeatureAFactory::class)
class FeatureAActivity : AppCompatActivity() {

    companion object {
        @SweetFactoryMethod
        @JvmStatic
        fun newIntent(context: Context): Intent {
            return Intent(context, FeatureAActivity::class.java)
        }
    }

    private var featureBNavigator = SweetFactory.newInstanceOf(FeatureBFactory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_feature_a)

        findViewById<Button>(R.id.btOpenFeatureB).setOnClickListener {
            startActivity(featureBNavigator?.newIntentForFeatureBActivity(this))
        }
    }

}

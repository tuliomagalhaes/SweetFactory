package com.tuliohdev.modularapplication.featureb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tuliohdev.modularapplication.FeatureBFactory
import sweetfactory.annotations.SweetFactoryDeclaration
import sweetfactory.annotations.SweetFactoryMethod

@SweetFactoryDeclaration(factory = FeatureBFactory::class)
class FeatureBDialogFragment : DialogFragment() {

    companion object {
        @SweetFactoryMethod
        @JvmStatic
        fun newInstanceFeatureBDialogFragment(): DialogFragment {
            return FeatureBDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_featureb, container)
    }
}

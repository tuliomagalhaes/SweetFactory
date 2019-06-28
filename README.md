SweetFactory
============

[ ![Download](https://api.bintray.com/packages/tulio/maven/SweetFactory/images/download.svg) ](https://bintray.com/tulio/maven/SweetFactory/_latestVersion)
[![CircleCI](https://circleci.com/gh/Tulioh/SweetFactory.svg?style=svg)](https://circleci.com/gh/Tulioh/SweetFactory)
[![codecov](https://codecov.io/gh/Tulioh/SweetFactory/branch/master/graph/badge.svg)](https://codecov.io/gh/Tulioh/SweetFactory)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

To easily reference classes from other modules in a multi-module architecture, SweetFactory helps you create a implementation of Abstract Factory that is in base module and reference it in any other module. 

 * Secure your code to be sure when you request an implementation of a class in runtime, this class exists.
 * Validation of existence of Factory implementation in build time.
 * Solve navigation problems in your multi-module project.
 
Download
--------
 
 If you want to instantiate a factory in your module, put this dependency:
 
```groovy 
dependencies {
    implementation 'com.tuliomagalhaes:sweetfactory:1.0.0'
}
```
 
 If you want to generate a factory annotating your classes, put this one:
 
```groovy
dependencies {
    kapt 'com.tuliomagalhaes:sweetfactory-compiler:1.0.0'
}
```
 
 But in case you have to generate a factory and instantiate in the same module, put both:
 
```groovy
dependencies {
    implementation 'com.tuliomagalhaes:sweetfactory:1.0.0'
    kapt 'com.tuliomagalhaes:sweetfactory-compiler:1.0.0'
}
```
 
Example
--------
 
 Navigation module:
 
```kotlin
interface FeatureAFactory {
    fun newIntent(context: Context, param: String): Intent
}
```

 FeatureA module:
 
 ```kotlin
 @SweetFactoryDeclaration(factory = FeatureAFactory::class)
 class FeatureAActivity : Activity() {
    
    companion object {
        private const val PARAM_KEY = "PARAM_KEY"
    
        @JvmStatic
        @SweetFactoryMethod
        fun newIntent(context: Context, param: String): Intent {
            val intent = Intent(context, FeatureAActivity::class.java)
            intent.putExtra(PARAM_KEY, param)
        }
    }
 }
 ```
 
 Splash module:
  
 ```kotlin
  class SplashActivity : Activity() {
  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        val featureAFactory = SweetFactory.newInstanceOf(FeatureAFactory::class)
        featureAFactory.newIntent(this)
    }
  }
 ```
 As you can see, SweetFactory will generate in compile time the FeatureAFactoryImpl that extends FeatureAFactory 
 interface and it will call FeatureAActivity companion methods.
 The example above does not limit only to activities, you can use this library to instantiate anything.
 
 I created a [sample app](sample/) that have 3 modules: featurea, featureb and navigation. 
 FeatureA calls FeatureB and FeatureB calls FeatureA. 
 Navigation module is responsible to have the factories declaration to be implemented in feature's module.
  
License
-------
 
     Copyright 2019 Túlio Magalhães
 
     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at
 
        http://www.apache.org/licenses/LICENSE-2.0
 
     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.

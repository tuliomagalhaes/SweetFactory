package sweetfactory.compiler

import com.squareup.javapoet.TypeSpec

data class FactoryImplementation(
    val packageName: String,
    val typeSpecBuilder: TypeSpec.Builder
)
package sweetfactory.compiler

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

internal data class FactoryImplementation(
    val packageName: String,
    val typeSpecBuilder: TypeSpec.Builder
) {
    companion object {
        fun from(fullClassName: String): FactoryImplementation {
            val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
            val packageName = fullClassName.replace(".$className", "")

            val interfaceClass = ClassName.get(packageName, className)

            val builder = TypeSpec.classBuilder(className + "Impl")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(interfaceClass)

            return FactoryImplementation(packageName, builder)
        }
    }
}
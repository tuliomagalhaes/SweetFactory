package sweetfactory.compiler

import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import sweetfactory.annotations.SweetFactoryDeclaration
import sweetfactory.annotations.SweetFactoryMethod
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

internal class SweetFactoryDeclarationImplementer {

    fun getClassNameOfFactory(typeElement: TypeElement): String {
        val intentFactoryAnnotation = typeElement.getAnnotation(SweetFactoryDeclaration::class.java)

        var typeMirror: TypeMirror? = null
        try {
            (intentFactoryAnnotation as SweetFactoryDeclaration).factory
        } catch (mte: MirroredTypeException) {
            typeMirror = mte.typeMirror
        }

        return typeMirror!!.toString()
    }

    fun implementMethods(builder: TypeSpec.Builder, typeElement: TypeElement) {
        val factoryImplementationClassName = typeElement.qualifiedName.toString()

        for (innerElement in typeElement.enclosedElements) {
            if (innerElement is ExecutableElement &&
                innerElement.getKind() == ElementKind.METHOD &&
                innerElement.getAnnotation(SweetFactoryMethod::class.java) != null) {

                val methodName = innerElement.simpleName.toString()
                val methodSpec = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .returns(TypeName.get(innerElement.returnType))

                val parametersName = addMethodParametersAndReturnAllParameters(innerElement, methodSpec)

                val returnStatement = String.format(
                    "return %s.%s(%s)",
                    factoryImplementationClassName,
                    methodName,
                    parametersName
                )
                methodSpec.addStatement(returnStatement)

                builder.addMethod(methodSpec.build())
            }
        }
    }

    private fun addMethodParametersAndReturnAllParameters(element: ExecutableElement,
                                                          methodSpec: MethodSpec.Builder): String {
        val parametersCount = element.parameters.size
        val parametersName = StringBuilder()

        for (i in 0 until parametersCount) {
            val parameter = element.parameters[i]

            val parameterName = parameter.simpleName.toString()

            methodSpec.addParameter(TypeName.get(parameter.asType()), parameterName)

            parametersName.append(parameterName)

            if (i + 1 < parametersCount) {
                parametersName.append(", ")
            }
        }

        return parametersName.toString()
    }

}

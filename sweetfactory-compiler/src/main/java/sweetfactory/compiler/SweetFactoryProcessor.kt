package sweetfactory.compiler

import com.google.auto.service.AutoService
import com.squareup.javapoet.*
import sweetfactory.annotations.SweetFactoryDeclaration
import sweetfactory.annotations.SweetFactoryMethod
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
class SweetFactoryProcessor : AbstractProcessor() {

    private var messagerUtil: MessagerUtil? = null

    override fun getSupportedAnnotationTypes(): Set<String> {
        return TreeSet(listOf(SweetFactoryDeclaration::class.java.canonicalName))
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)

        messagerUtil = MessagerUtil(processingEnvironment.messager)
    }

    override fun process(annotations: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val factories = HashMap<String, FactoryImplementation>()

        roundEnvironment.getElementsAnnotatedWith(SweetFactoryDeclaration::class.java).forEach { element ->
            val typeElement = element as TypeElement

            val factoryClassName = getClassNameOfFactory(typeElement)

            val factoryImplementation: FactoryImplementation
            if (factories.containsKey(factoryClassName)) {
                factoryImplementation = factories[factoryClassName]!!
            } else {
                factoryImplementation = generateFactoryImplementation(factoryClassName)
                factories[factoryClassName] = factoryImplementation
            }

            implementMethods(factoryImplementation.typeSpecBuilder, typeElement)
        }

        factories.forEach { entry ->
            val fileSpec = JavaFile.builder(
                entry.value.packageName,
                entry.value.typeSpecBuilder.build()
            ).build()

            try {
                fileSpec.writeTo(processingEnv.filer)
            } catch (e: IOException) {
                val className = fileSpec.toJavaFileObject().name.replace("/", ".")
                messagerUtil!!.error("Error when trying to generate code for %s", className)
            }
        }

        return false
    }

    private fun getClassNameOfFactory(typeElement: TypeElement): String {
        val intentFactoryAnnotation = typeElement.getAnnotation(SweetFactoryDeclaration::class.java)

        var typeMirror: TypeMirror? = null
        try {
            (intentFactoryAnnotation as SweetFactoryDeclaration).factory
        } catch (mte: MirroredTypeException) {
            typeMirror = mte.typeMirror
        }

        return typeMirror!!.toString()
    }

    private fun generateFactoryImplementation(factoryClassName: String): FactoryImplementation {
        val className = factoryClassName.substring(factoryClassName.lastIndexOf(".") + 1)
        val packageName = factoryClassName.replace(".$className", "")

        val interfaceClass = ClassName.get(packageName, className)

        val builder = TypeSpec.classBuilder(className + "Impl")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(interfaceClass)

        return FactoryImplementation(packageName, builder)
    }

    private fun implementMethods(builder: TypeSpec.Builder, typeElement: TypeElement) {
        val factoryImplementationClassName = typeElement.qualifiedName.toString()

        for (innerElement in typeElement.enclosedElements) {
            if (innerElement is ExecutableElement &&
                innerElement.getKind() == ElementKind.METHOD &&
                innerElement.getAnnotation(SweetFactoryMethod::class.java) != null
            ) {

                val parametersName = StringBuilder()
                val methodName = innerElement.simpleName.toString()
                val methodSpec = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override::class.java)
                    .returns(TypeName.get(innerElement.returnType))

                val parametersCount = innerElement.parameters.size
                for (i in 0 until parametersCount) {
                    val parameter = innerElement.parameters[i]

                    val parameterName = parameter.simpleName.toString()

                    methodSpec.addParameter(TypeName.get(parameter.asType()), parameterName)
                    parametersName.append(parameterName)

                    if (i + 1 < parametersCount) {
                        parametersName.append(", ")
                    }
                }

                val returnStatement = String.format(
                    "return %s.%s(%s)",
                    factoryImplementationClassName,
                    methodName,
                    parametersName.toString()
                )
                methodSpec.addStatement(returnStatement)

                builder.addMethod(methodSpec.build())
            }
        }
    }
}
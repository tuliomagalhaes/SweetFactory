package sweetfactory.compiler

import com.google.auto.service.AutoService
import com.google.common.annotations.VisibleForTesting
import com.squareup.javapoet.*
import sweetfactory.annotations.SweetFactoryDeclaration
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class SweetFactoryProcessor : AbstractProcessor() {

    private lateinit var sweetFactoryDeclarationImplementer: SweetFactoryDeclarationImplementer

    override fun getSupportedAnnotationTypes(): Set<String> {
        return TreeSet(listOf(SweetFactoryDeclaration::class.java.canonicalName))
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    @VisibleForTesting
    internal fun init(processingEnvironment: ProcessingEnvironment?,
             sweetFactoryDeclarationImplementer: SweetFactoryDeclarationImplementer) {
        super.init(processingEnvironment)
        this.sweetFactoryDeclarationImplementer = sweetFactoryDeclarationImplementer
    }

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        this.init(processingEnvironment, SweetFactoryDeclarationImplementer())
    }

    override fun process(annotations: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        val factories = HashMap<String, FactoryImplementation>()

        roundEnvironment.getElementsAnnotatedWith(SweetFactoryDeclaration::class.java).forEach { element ->
            val typeElement = element as TypeElement

            val factoryClassName = sweetFactoryDeclarationImplementer.getClassNameOfFactory(typeElement)

            val factoryImplementation: FactoryImplementation
            if (factories.containsKey(factoryClassName)) {
                factoryImplementation = factories[factoryClassName]!!
            } else {
                factoryImplementation = FactoryImplementation.from(factoryClassName)
                factories[factoryClassName] = factoryImplementation
            }

            sweetFactoryDeclarationImplementer.implementMethods(factoryImplementation.typeSpecBuilder, typeElement)
        }

        factories.forEach { entry ->
            val javaFile = JavaFile.builder(
                entry.value.packageName,
                entry.value.typeSpecBuilder.build()
            ).build()

            try {
                javaFile.writeTo(processingEnv.filer)
            } catch (e: IOException) {
                val className = javaFile.toJavaFileObject().name.replace("/", ".")
                processingEnv.messager.error("Error when trying to generate code for %s", className)
            }
        }

        return false
    }


}
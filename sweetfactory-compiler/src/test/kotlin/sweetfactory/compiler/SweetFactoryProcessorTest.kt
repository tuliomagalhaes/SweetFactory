package sweetfactory.compiler

import com.google.testing.compile.CompilationRule
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.present
import com.squareup.javapoet.JavaFile
import utils.getPrivateField
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sweetfactory.annotations.SweetFactoryDeclaration
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

private const val SWEET_FACTORY_DECLARATION_IMPLEMENTER_NAME = "sweetFactoryDeclarationImplementer"

internal class SweetFactoryProcessorTest {

    private val processingEnvironment: ProcessingEnvironment = mockk()

    private lateinit var sweetFactoryProcessor: SweetFactoryProcessor

    @Rule @JvmField
    val compilationRule = CompilationRule()

    @Before
    internal fun setUp() {
        sweetFactoryProcessor = SweetFactoryProcessor()
    }

    @Test
    fun `getSupportedSourceVersion should return javaVersion8`() {
        // When
        val supportedSourceVersion = sweetFactoryProcessor.supportedSourceVersion

        // Then
        assertThat(supportedSourceVersion.name, equalTo("RELEASE_8"))
    }

    @Test
    fun `getSupportedAnnotationTypes should has length 1 and has SweetFactoryDeclaration annotation`() {
        // When
        val supportedAnnotationTypes = sweetFactoryProcessor.supportedAnnotationTypes

        // Then
        assertThat(supportedAnnotationTypes, hasSize(equalTo(1)))
        assertThat(supportedAnnotationTypes.contains(SweetFactoryDeclaration::class.java.canonicalName), equalTo(true))
    }

    @Test
    fun `init should instantiate sweetFactoryDeclarationImplementer variable`() {
        // When
        sweetFactoryProcessor.init(processingEnvironment)

        // Then
        val sweetFactoryDeclarationImplementer = sweetFactoryProcessor
            .getPrivateField<SweetFactoryDeclarationImplementer>(SWEET_FACTORY_DECLARATION_IMPLEMENTER_NAME)
        assertThat(sweetFactoryDeclarationImplementer, present())
    }

    @Test
    fun `process should generate factory implementation for same class name`() {
        // Given
        val roundEnvironment: RoundEnvironment = mockk()
        val filer: Filer = mockk()
        val typeElement: TypeElement = mockk()
        val javaFileBuilder: JavaFile.Builder = mockk()
        val javaFile: JavaFile = mockk()
        val sweetFactoryDeclarationImplementer: SweetFactoryDeclarationImplementer = mockk()
        val elements = mutableSetOf(typeElement, typeElement)
        val factoryClassName = "com.test.Test"
        mockkStatic(JavaFile::class)
        every { roundEnvironment.getElementsAnnotatedWith(SweetFactoryDeclaration::class.java) } returns elements
        every { sweetFactoryDeclarationImplementer.getClassNameOfFactory(typeElement) } returns factoryClassName
        every { sweetFactoryDeclarationImplementer.implementMethods(any(), any()) } just Runs
        every { JavaFile.builder(any(), any()) } returns javaFileBuilder
        every { javaFileBuilder.build() } returns javaFile
        every { javaFile.writeTo(filer) } just Runs
        every { processingEnvironment.filer } returns filer

        // When
        sweetFactoryProcessor.init(processingEnvironment, sweetFactoryDeclarationImplementer)
        sweetFactoryProcessor.process(setOf(), roundEnvironment)

        // Then
        verify { sweetFactoryDeclarationImplementer.implementMethods(any(), any()) }
        verify { javaFile.writeTo(any<Filer>()) }
    }
}
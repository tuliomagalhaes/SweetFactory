package sweetfactory.compiler

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import sweetfactory.annotations.SweetFactoryDeclaration
import sweetfactory.annotations.SweetFactoryMethod
import javax.lang.model.element.*
import com.google.testing.compile.CompilationRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SweetFactoryDeclarationImplementerTest {

    private lateinit var sweetFactoryDeclarationImplementer: SweetFactoryDeclarationImplementer

    @Rule @JvmField
    val compilation = CompilationRule()

    @Before
    internal fun setUp() {
        sweetFactoryDeclarationImplementer = SweetFactoryDeclarationImplementer()
    }

    @Test
    fun `getClassNameOfFactory should return a class name from typeMirror`() {
        // Given
        val typeElement = compilation.elements.getTypeElement(AnnotatedClass::class.java.canonicalName)

        // When
        val classNameOfFactory = sweetFactoryDeclarationImplementer.getClassNameOfFactory(typeElement)

        // Then
        assertThat(classNameOfFactory, equalTo(FactoryToBeImplemented::class.java.canonicalName))
    }

    @Test
    fun `implementMethods should generate methods from typeElement`() {
        // Given
        val factoryImplementation = FactoryImplementation.from(FactoryToBeImplemented::class.java.canonicalName)
        val typeElement = compilation.elements.getTypeElement(AnnotatedClass::class.java.canonicalName)

        // When
        sweetFactoryDeclarationImplementer.implementMethods(factoryImplementation.typeSpecBuilder, typeElement)

        // Then
        val typeSpec = factoryImplementation.typeSpecBuilder.build()
        val voidMethodSpec = typeSpec.methodSpecs[0]
        val withReturnMethodSpec = typeSpec.methodSpecs[1]

        assertThat(voidMethodSpec.modifiers.contains(Modifier.PUBLIC), equalTo(true))
        assertThat(voidMethodSpec.annotations[0].type.toString(), equalTo(Override::class.java.canonicalName))
        assertThat(voidMethodSpec.returnType.toString(), equalTo("void"))

        assertThat(voidMethodSpec.name.toString(), equalTo("voidMethod"))
        assertThat(voidMethodSpec.code.toString(), equalTo("return sweetfactory.compiler.AnnotatedClass.voidMethod(arg0);\n"))
        assertThat(withReturnMethodSpec.name.toString(), equalTo("withReturn"))
        assertThat(withReturnMethodSpec.code.toString(), equalTo("return sweetfactory.compiler.AnnotatedClass.withReturn(arg0, arg1);\n"))
    }
}

@SweetFactoryDeclaration(factory = FactoryToBeImplemented::class)
private class AnnotatedClass {

    companion object {
        @JvmStatic
        @SweetFactoryMethod
        fun voidMethod(param: String) {

        }

        @JvmStatic
        @SweetFactoryMethod
        fun withReturn(param1: String, param2: String): String {
            return param1 + param2
        }
    }

}

private interface FactoryToBeImplemented {

    fun voidMethod(param: String)

    fun withReturn(param1: String, param2: String): String

}
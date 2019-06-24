package sweetfactory.compiler

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.squareup.javapoet.TypeSpec
import org.junit.Test
import javax.lang.model.element.Modifier

internal class FactoryImplementationTest {

    @Test
    fun `from should return a FactoryImplementation`() {
        // Given
        val fullClassName = "sweetfactory.compiler.Test"

        // When
        val factoryImplementation = FactoryImplementation.from(fullClassName)

        // Then
        val packageName = factoryImplementation.packageName
        val typeSpec = factoryImplementation.typeSpecBuilder.build()
        assertThat(packageName, equalTo("sweetfactory.compiler"))
        assertThat(typeSpec.name, equalTo("TestImpl"))
        assertThat(typeSpec.kind, equalTo(TypeSpec.Kind.CLASS))
        assertThat(typeSpec.modifiers.contains(Modifier.PUBLIC), equalTo(true))
        assertThat(typeSpec.modifiers.contains(Modifier.FINAL), equalTo(true))
        assertThat(typeSpec.superinterfaces[0].toString(), equalTo(fullClassName))
    }
}
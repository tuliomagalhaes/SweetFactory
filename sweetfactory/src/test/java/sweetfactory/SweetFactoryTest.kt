package sweetfactory

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class SweetFactoryTest {

    @Test
    fun `newInstanceOf should return a instance of FactoryImpl`() {
        // Given
        val factoryClass = Factory::class

        // When
        val factoryImpl = SweetFactory.newInstanceOf(factoryClass)

        // Then
        assertThat(factoryImpl?.sayHello(), equalTo("Hello"))
    }

}
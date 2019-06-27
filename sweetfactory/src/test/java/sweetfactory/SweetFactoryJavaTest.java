package sweetfactory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SweetFactoryJavaTest {

    @Test
    public void newInstanceOf_should_return_a_instance_of_FactoryImpl() {
        // Given
        final Class<Factory> factoryClass = Factory.class;

                // When
        final Factory factoryImpl = SweetFactory.newInstanceOf(factoryClass);

        // Then
        assertThat(factoryImpl.sayHello(), equalTo("Hello"));
    }

}

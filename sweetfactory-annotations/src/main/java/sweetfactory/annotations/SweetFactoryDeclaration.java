package sweetfactory.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Tell the SweetFactory Compiler to generate a implementation for the annotated class
 * <pre><code>
 * {@literal @}SweetFactoryDeclaration(factory = FeatureActivityFactory.class)
 * public class FeatureActivity extends Activity {}
 * </code></pre>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SweetFactoryDeclaration {
    Class factory();
}

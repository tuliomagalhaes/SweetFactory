package sweetfactory.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Tell the SweetFactoryDeclaration Compiler to generate a implementation for the annotated class
 * <pre><code>
 * {@literal @}SweetFactoryDeclaration
 * public interface FeatureActivityIntentFactory {}
 * </code></pre>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SweetFactoryDeclaration {
    Class factory();
}

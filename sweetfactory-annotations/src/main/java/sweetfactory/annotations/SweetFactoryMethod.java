package sweetfactory.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used in conjunction with @see {@literal @}sweetfactory.annotations.SweetFactoryDeclaration to tell the SweetFactory Compiler how to implement the factory
 * <pre><code>
 * {@literal @}SweetFactoryMethod
 * public static Intent newIntent(Context context) { ... }
 * </code></pre>
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface SweetFactoryMethod {
}

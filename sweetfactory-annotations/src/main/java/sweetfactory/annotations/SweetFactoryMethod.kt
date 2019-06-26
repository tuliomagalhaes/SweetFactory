package sweetfactory.annotations

/**
 * This annotation is used in conjunction with @see @sweetfactory.annotations.SweetFactoryDeclaration
 * to tell the SweetFactory Compiler how to implement the factory
 * <pre>`
 * @SweetFactoryMethod
 * public static Intent newIntent(Context context) { ... }
`</pre> *
 */
@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SweetFactoryMethod

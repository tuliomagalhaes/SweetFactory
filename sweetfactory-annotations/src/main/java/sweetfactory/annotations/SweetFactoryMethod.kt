package sweetfactory.annotations

/**
 * This annotation is used in conjunction with @see @sweetfactory.annotations.SweetFactoryDeclaration
 * to tell the SweetFactory Compiler how to implement the factory
 * <pre>`
 * @SweetFactoryMethod
 * @JvmStatic
 * fun newIntent(context: Context): Intent { ... }
`</pre> *
 */
@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SweetFactoryMethod

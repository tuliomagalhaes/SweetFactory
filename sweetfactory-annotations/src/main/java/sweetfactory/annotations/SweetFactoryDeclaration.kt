package sweetfactory.annotations

import kotlin.reflect.KClass

/**
 * Tell the SweetFactory Compiler to generate a implementation for the annotated class
 * <pre>`
 * @SweetFactoryDeclaration(factory = FeatureActivityFactory::class)
 * class FeatureActivity : Activity() {}
`</pre> *
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class SweetFactoryDeclaration(val factory: KClass<*>)

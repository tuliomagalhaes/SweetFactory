package sweetfactory

import kotlin.reflect.KClass

/**
 * Create an instance of a factory implemented by SweetFactory processor
 * <pre>`
 * SweetFactory.newInstanceOf(FeatureActivityFactory::class)
`</pre> *
 */
object SweetFactory {

    @JvmStatic
    fun <T : Any> newInstanceOf(clazz: KClass<T>): T? = newInstanceOf(clazz.java)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T> newInstanceOf(clazz: Class<T>): T? {
        return try {
            val className = clazz.name + "Impl"
            val c = Class.forName(className)
            c.newInstance() as T
        } catch (ex: ClassNotFoundException) {
            null
        }
    }

}

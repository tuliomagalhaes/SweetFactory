package sweetfactory

import kotlin.reflect.KClass

object SweetFactory {

    @JvmStatic
    fun <T : Any> newInstanceOf(clazz: KClass<T>): T? = newInstanceOf(clazz.java)

    @JvmStatic
    fun <T> newInstanceOf(clazz: Class<T>): T? {
        return try {
            val className = clazz.name + "Impl"
            val c = Class.forName(className)
            c.newInstance() as T
        } catch (ex: Exception) {
            null
        }
    }

}

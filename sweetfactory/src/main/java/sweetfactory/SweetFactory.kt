package sweetfactory

object SweetFactory {

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

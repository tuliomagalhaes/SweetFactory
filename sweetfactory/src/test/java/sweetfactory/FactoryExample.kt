package sweetfactory

internal interface Factory {

    fun sayHello(): String

}

internal class FactoryImpl : Factory {

    override fun sayHello(): String = "Hello"

}
package sweetfactory.compiler

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

class MessagerUtil(private val messager: Messager) {

    fun error(message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.ERROR, null, message, args)
    }

    fun error(element: Element, message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args)
    }

    fun warning(element: Element, message: String, vararg args: Any) {
        printMessage(Diagnostic.Kind.WARNING, element, message, args)
    }

    private fun printMessage(kind: Diagnostic.Kind, element: Element?, message: String, vararg args: Any) {
        var message = message
        if (args.isNotEmpty()) {
            message = String.format(message, *args)
        }

        if (element == null) {
            messager.printMessage(kind, message)
        } else {
            messager.printMessage(kind, message, element)
        }
    }
}
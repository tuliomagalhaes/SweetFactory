package sweetfactory.compiler;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

class MessagerUtil {

    private Messager messager;

    MessagerUtil(Messager messager) {
        this.messager = messager;
    }

    public void error(String message, Object... args) {
        printMessage(Kind.ERROR, null, message, args);
    }

    public void error(Element element, String message, Object... args) {
        printMessage(Kind.ERROR, element, message, args);
    }

    public void warning(Element element, String message, Object... args) {
        printMessage(Kind.WARNING, element, message, args);
    }

    private void printMessage(Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        if (element == null) {
            messager.printMessage(kind, message);
        } else {
            messager.printMessage(kind, message, element);
        }
    }

}

package sweetfactory;

public class SweetFactory {

    public static <T> T newInstanceOf(Class<T> clazz) {
        final String className = clazz.getName() + "Impl";
        try {
            final Class c = Class.forName(className);
            return (T) c.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}

package utils

import java.lang.reflect.Field

@Suppress("UNCHECKED_CAST")
fun <T> Any.getPrivateField(fieldName: String): T {
    val field = getFieldFromHierarchy(this.javaClass, fieldName)
    return field.get(this) as T
}

fun Any.setPrivateField(fieldName: String, fieldValue: Any) {
    val field = getFieldFromHierarchy(this.javaClass, fieldName)
    field.set(this, fieldValue)
}

private fun getFieldFromHierarchy(clazz: Class<*>, fieldName: String): Field {
    var clazzToSearch = clazz
    var field = getField(clazzToSearch, fieldName)
    while (field == null && clazzToSearch != Any::class.java && clazzToSearch != Object::class.java) {
        clazzToSearch = clazzToSearch.superclass
        field = getField(clazzToSearch, fieldName)
    }
    if (field == null) {
        throw RuntimeException(
            "You want me to set value to this field: '" + fieldName +
                    "' on this class: '" + clazzToSearch.simpleName +
                    "' but this field is not declared withing hierarchy of this class!"
        )
    }
    return field
}

private fun getField(clazz: Class<*>, field: String): Field? {
    return try {
        val declaredField = clazz.getDeclaredField(field)
        declaredField.isAccessible = true
        declaredField
    } catch (e: NoSuchFieldException) {
        null
    }

}
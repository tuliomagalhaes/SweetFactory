package utils

import java.lang.reflect.Field

fun <T> Any.getPrivateField(fieldName: String): T {
    try {
        return getFieldFromHierarchy(this.javaClass, fieldName) as T
    } catch (e: Exception) {
        throw RuntimeException("Unable to get value from a private field.", e)
    }

}

fun Any.setPrivateField(fieldName: String, fieldValue: Any) {
    try {
        val field = getFieldFromHierarchy(this.javaClass, fieldName)
        field.set(this, fieldValue)
    } catch (e: Exception) {
        throw RuntimeException("Unable to set value on a private field.", e)
    }

}

private fun getFieldFromHierarchy(clazz: Class<*>, fieldName: String): Field {
    var clazz = clazz
    var field = getField(clazz, fieldName)
    while (field == null && clazz != Any::class.java && clazz != Object::class.java) {
        clazz = clazz.superclass
        field = getField(clazz, fieldName)
    }
    if (field == null) {
        throw RuntimeException(
            "You want me to set value to this field: '" + fieldName +
                    "' on this class: '" + clazz.simpleName +
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
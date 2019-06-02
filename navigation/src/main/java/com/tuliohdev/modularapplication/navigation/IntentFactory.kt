package com.tuliohdev.modularapplication.navigation

object IntentFactory {

    fun <T> getInstance(clazz: Class<T>): T {
        val className = clazz.name + "Impl"
        val c = Class.forName(className)
        return c.newInstance() as T
    }

}
package testingUtils

import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import org.mockito.stubbing.Stubber

fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!

fun <T> eq(obj: T): T = Mockito.eq<T>(obj)
fun <T> any(): T = Mockito.any<T>()
fun anyInt(): Int = Mockito.anyInt()

fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> = ArgumentCaptor.forClass(T::class.java)
inline fun <reified T : Any> classMock(): T = Mockito.mock(T::class.java)

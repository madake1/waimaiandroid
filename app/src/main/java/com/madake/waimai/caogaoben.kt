package com.madake.waimai

import kotlin.math.max

fun main() {
    val a = 10
    val b =11
    println(maxNum(a,b))
}

fun maxNum(num1:Int,num2:Int): Int {
    return max(num2,num1)
}
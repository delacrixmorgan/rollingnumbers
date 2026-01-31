package com.dontsaybojio.rollingnumbers

import kotlin.math.ceil
import kotlin.math.pow

fun Double.roundUp(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return ceil(this * factor) / factor
}
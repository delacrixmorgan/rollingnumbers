package com.dontsaybojio.rollingnumbers.model

/**
 * DefaultAnimationDuration in ms
 */
enum class DefaultAnimationDuration(val duration: Int) {
    Slow(3_500),
    Medium(2_000),
    Fast(1_000);

    companion object {
        val Default = Medium
    }
}
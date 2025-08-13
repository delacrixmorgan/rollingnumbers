package io.dontsayboj.rollingnumbers.model

/**
 * DefaultAnimationDuration in ms
 */
enum class DefaultAnimationDuration(val duration: Int) {
    Slow(800),
    Medium(400),
    Fast(200);

    companion object {
        val Default = Medium
    }
}
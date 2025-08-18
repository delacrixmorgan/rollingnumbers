package io.dontsayboj.rollingnumbers.model

/**
 * DefaultAnimationDuration in ms
 */
enum class DefaultAnimationDuration(val duration: Int) {
    Slow(840),
    Medium(450),
    Fast(250);

    companion object {
        val Default = Medium
    }
}
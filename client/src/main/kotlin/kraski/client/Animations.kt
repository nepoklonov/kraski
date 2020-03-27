package kraski.client

import kotlin.browser.window
import kotlin.math.acos
import kotlin.math.min
import kotlin.math.sin

fun animate(duration: Double, timing: (Double) -> Double = { it }, action: (Double) -> Unit) {
    val start = window.performance.now()

    fun request(time: Double) {
        val timeFraction = min((time - start) / duration, 1.0)
        val progress = timing(timeFraction);
        action(progress)
        if (timeFraction < 1) {
            window.requestAnimationFrame(::request)
        }
    }

    window.requestAnimationFrame(::request)
}

fun ((Double) -> Double).out(): (Double) -> Double = {
    1 - this(1 - it)
}


fun ((Double) -> Double).inOut(): (Double) -> Double = {
    if (it < 0.5) this(2 * it) / 2 else (2 - this(2 * (1 - it))) / 2
}

val arcTiming = { time: Double -> 1 - sin(acos(time)) }
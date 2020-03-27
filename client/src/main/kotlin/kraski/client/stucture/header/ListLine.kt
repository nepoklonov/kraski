package kraski.client.stucture.header

import kraski.client.line
import kraski.client.polygon
import kotlinx.css.*
import kotlinx.html.SVG
import react.RBuilder
import react.dom.RDOMBuilder
import styled.css
import styled.styledSvg

fun RBuilder.topLine(color: Color, height: Double, needLine: Boolean) {
    val xSpace = 10.5
    val r = 8.5
    styledSvg {
        css {
            position = Position.absolute
            left = 0.px
            width = (xSpace + r + 5).px
            this.height = 100.pct
        }
        rhombus(xSpace, height / 2, r, color)
        if (needLine) line(xSpace, height / 2, xSpace, height, color) {}
    }
}

fun RDOMBuilder<SVG>.rhombus(xStart: Double, yStart: Double, radius: Double, color: Color) {
    polygon {
        attrs {
            points = listOf(
                xStart to yStart - radius,
                xStart + radius to yStart,
                xStart to yStart + radius,
                xStart - radius to yStart
            ).joinToString(separator = " ") {
                "${it.first},${it.second}"
            }
            fill = color.value
            stroke = color.value
        }
    }
}

fun RBuilder.bottomLine(color: Color, rects: List<Double>) {
    val xSpace = 10.5
    val r = 5.5
    styledSvg {
        css {
            position = Position.absolute
            width = (xSpace + r + 5).px
            height = (rects.last() + r + 5).px
        }
        line(xSpace, 0.0, xSpace, rects.last(), color) { }
        rects.forEach {
            rhombus(xSpace, it, r, color)
        }
    }
}
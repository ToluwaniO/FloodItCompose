package com.toluog.flooditcompose.model

enum class CellColor(val value: Int) {
    RED(0),
    GREEN(1),
    BLUE(2),
    YELLOW(3),
    PURPLE(4),
    ORANGE(5);

    companion object {
        fun from(findValue: Int): CellColor = values().first { it.value == findValue }
    }
}

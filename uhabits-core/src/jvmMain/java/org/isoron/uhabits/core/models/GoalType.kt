package org.isoron.uhabits.core.models

import java.lang.IllegalStateException

enum class GoalType(val value: String) {
    PERSONAL("Personal"), PROFESSIONAL("Professional"),
    FINANCIAL("Financial"), HEALTH("Health");

    companion object {
        fun fromString(value: String): GoalType {
            return when (value) {
                PERSONAL.value -> PERSONAL
                PROFESSIONAL.value -> PROFESSIONAL
                FINANCIAL.value -> FINANCIAL
                HEALTH.value -> HEALTH
                else -> throw IllegalStateException()
            }
        }
    }
}

package com.example.hopeshipstrive.util

import android.os.Bundle

fun Bundle.getIntOrNull(key: String): Int? {
    return if (containsKey(key)) getInt(key) else null
}
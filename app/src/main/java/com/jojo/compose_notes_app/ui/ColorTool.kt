package com.jojo.compose_notes_app.ui

import androidx.compose.ui.graphics.Color

val urgentColor: (isDark: Boolean) -> Color = { if (it) Color(0xFF920000) else Color(0xFFE61B1B) }
package com.bhaskardamayanthi.gossy.managers

object  PollsFragColorsManager {
    val backgroundColors = listOf(
        "#FF5733", "#3498DB", "#2ECC71", "#F1C40F", "#1ABC9C", "#E74C3C", "#2980B9", "#27AE60",
        "#9B59B6", "#F39C12", "#16A085", "#C0392B", "#34495E", "#8E44AD", "#2C3E50", "#D35400",
        "#00FFFF", "#7FFF00", "#FF7F50", "#FFD700", "#B8860B", "#A9A9A9", "#A9A9A9", "#006400",
        "#BDB76B", "#8B008B", "#556B2F", "#FF8C00", "#9932CC", "#8B0000", "#E9967A", "#8FBC8F",
        "#483D8B", "#2F4F4F", "#00CED1", "#9400D3", "#FF1493", "#00BFFF", "#696969", "#1E90FF",
        "#B22222", "#FFFAF0", "#228B22", "#FF00FF", "#DCDCDC", "#F8F8FF", "#FFD700", "#DAA520",
        "#808080", "#008000", "#ADFF2F", "#F0FFF0", "#FF69B4", "#CD5C5C", "#4B0082", "#FFFFF0",
        "#F0E68C", "#E6E6FA", "#FFF0F5", "#7CFC00", "#FFFACD", "#ADD8E6", "#F08080", "#E0FFFF",
        "#FAFAD2", "#90EE90", "#D3D3D3", "#FFB6C1", "#FFA07A", "#20B2AA", "#87CEFA", "#778899",
        "#B0C4DE", "#FFFFE0", "#00FF00", "#32CD32", "#FAF0E6", "#FF00FF", "#800000", "#66CDAA",
        "#0000CD", "#BA55D3", "#9370DB", "#3CB371", "#7B68EE", "#00FA9A", "#48D1CC", "#C71585",
        "#191970", "#F5FFFA", "#FFE4E1", "#FFE4B5", "#FFDEAD", "#000080", "#FDF5E6", "#808000",
        "#6B8E23", "#FFA500", "#FF4500", "#DA70D6", "#EEE8AA", "#98FB98", "#AFEEEE", "#DB7093",
        "#FFEFD5", "#FFDAB9", "#CD853F", "#FFC0CB", "#DDA0DD", "#B0E0E6", "#800080", "#663399"
    )

    // List of corresponding text colors for each background color
    val textColors = backgroundColors.map { bgColor ->
        val backgroundColor = android.graphics.Color.parseColor(bgColor)
        val brightness = (backgroundColor and 0xFF) +
                ((backgroundColor shr 8) and 0xFF) +
                ((backgroundColor shr 16) and 0xFF)
        if (brightness > 3 * 255 / 2) "#000000" else "#FFFFFF" // Determine contrasting text color
    }

}
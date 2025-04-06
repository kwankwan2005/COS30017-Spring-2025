package com.example.assignment3.util

import com.example.assignment3.R

data class CategoryStyle(
    val iconResId: Int,
    val bgColorResId: Int,
    val fgColorResId: Int
)

object CategoryStyleMapper {

    fun getStyle(category: String): CategoryStyle {
        return when (category.lowercase()) {
            "salary" -> CategoryStyle(R.drawable.ic_salary, R.color.bg_green_light, R.color.bg_green)
            "gift" -> CategoryStyle(R.drawable.ic_gift, R.color.bg_pink_light, R.color.bg_pink)
            "bonus" -> CategoryStyle(R.drawable.ic_bonus, R.color.bg_blue_light, R.color.bg_blue)

            "shopping" -> CategoryStyle(R.drawable.ic_shopping, R.color.bg_purple_light, R.color.bg_purple)
            "bills" -> CategoryStyle(R.drawable.ic_bills, R.color.bg_orange_light, R.color.bg_orange)
            "food & drink" -> CategoryStyle(R.drawable.ic_food, R.color.bg_red_light, R.color.bg_red)
            "relatives" -> CategoryStyle(R.drawable.ic_relatives, R.color.bg_brown_light, R.color.bg_brown)

            else -> CategoryStyle(R.drawable.ic_add, R.color.bg_gray_light, R.color.gray)
        }
    }
}

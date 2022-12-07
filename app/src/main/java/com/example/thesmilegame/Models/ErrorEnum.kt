package com.example.thesmilegame.Models

enum class ErrorEnum(val error: String) {
    NON(""),
    FIRST_NAME("Please enter first name"),
    LAST_NAME("Please enter last name"),
    EMAIL("Please enter valid email"),
    PASSWORD("Please enter valid password")
}
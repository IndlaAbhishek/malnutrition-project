package com.bhanu.malnutritionriskapp

data class PredictionResponse(
    val status: String,
    val risk: String,
    val confidence: Double,
    val diet_plan_id: String,
    val diet_message: String
)

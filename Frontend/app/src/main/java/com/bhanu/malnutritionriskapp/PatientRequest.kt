package com.bhanu.malnutritionriskapp

data class PatientRequest(
    val age: Int,
    val weight: Double,
    val height: Int,
    val muac: Double,
    val hemoglobin: Double,
    val sex: String,
    val mother_edu: String,
    val water_source: String,
    val toilet: String,

    val wealth_index: Int,
    val household_size: Int,
    val birth_weight: Double
)

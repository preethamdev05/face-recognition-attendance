package com.attendance.facerec.domain.model

import java.io.Serializable

data class ClassInfo(
    val id: String = "",
    val name: String = "",
    val faculty: String = "",
    val schedule: List<ClassSchedule> = emptyList(),
    val capacity: Int = 60,
    val students: Map<String, Boolean> = emptyMap(),
    val departmentId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable

data class ClassSchedule(
    val day: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val room: String = ""
) : Serializable

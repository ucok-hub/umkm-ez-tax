package com.example.capstone_pajak.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Request body data classes
data class Under2025Request(val penghasilan: Long, val tahun: Int, val golongan: String)
data class Tax2025Request(val penghasilan: Long, val golongan: String, val norma: Int)
data class ProgressiveRequest(val penghasilan: Long, val hargaPokok: Long, val biayaUsaha: Long, val golongan: String)

// Response data class
data class TaxResponse(
    val taxAmount: Long,
    val penghasilanNetto: Long? = null,
    val ptkp: Long? = null,
    val pkp: Long? = null,
    val PPHTerutang: Long? = null
)

interface APIService {

    @POST("/calculate-under2025")
    suspend fun calculateUnder2025(@Body request: Under2025Request): Response<TaxResponse>

    @POST("/calculate-2025")
    suspend fun calculate2025Onwards(@Body request: Tax2025Request): Response<TaxResponse>

    @POST("/calculate-pembukuan-progresif")
    suspend fun calculateProgressive(@Body request: ProgressiveRequest): Response<TaxResponse>
}

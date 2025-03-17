package com.example.lab4

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.Path

data class APIMeditation(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "imageUrl") val imageUrl: String
)
data class APIMeditationDetail(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "duration") val duration: Int,
    @Json(name = "imageUrl") val imageUrl: String
)
class MeditationViewModelFactory(
    private val repository: MeditationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MeditationLibraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MeditationLibraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
interface MeditationApiService {
    @GET("meditations2") // Общие записи
    suspend fun getMeditations(): List<APIMeditation>

    @GET("meditations/{id}") // Детальная информация
    suspend fun getMeditationDetails(@Path("id") id: String): APIMeditationDetail
}

class MeditationRepository(private val apiService: MeditationApiService) {

    suspend fun getMeditations(): List<APIMeditation> {
        return apiService.getMeditations()
    }

    suspend fun getMeditationDetails(id: String): APIMeditationDetail {
        return apiService.getMeditationDetails(id)
    }
}

object RetrofitClient {
    private const val BASE_URL = "https://67d7fa019d5e3a10152cd96a.mockapi.io/meditations/v1/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Добавляем поддержку Kotlin через рефлексию
        .build()

    val instance: MeditationApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Используем Moshi
            .build()
            .create(MeditationApiService::class.java)
    }
}
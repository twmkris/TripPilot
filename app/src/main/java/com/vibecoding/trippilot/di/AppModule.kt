
package com.vibecoding.trippilot.di

import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.vibecoding.trippilot.R
import com.vibecoding.trippilot.data.datasource.local.TripDatabase
import com.vibecoding.trippilot.data.repository.TripRepositoryImpl
import com.vibecoding.trippilot.domain.repository.TripRepository
import com.vibecoding.trippilot.domain.usecase.*
import com.vibecoding.trippilot.presentation.add_trip.AddTripViewModel
import com.vibecoding.trippilot.presentation.trip_list.TripListViewModel
import com.vibecoding.trippilot.presentation.trip_detail.TripDetailViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            TripDatabase::class.java,
            TripDatabase.DATABASE_NAME
        ).build()
    }

    single<TripRepository> {
        TripRepositoryImpl(get<TripDatabase>().tripDao, get())
    }

    single {
        GetTripsUseCase(get())
    }

    single {
        GetTripUseCase(get())
    }

    single {
        AddTripUseCase(get())
    }

    single {
        DeleteTripUseCase(get())
    }

    single {
        GenerateItineraryUseCase(get())
    }

    single {
        GenerativeModel(
            modelName = "gemini-2.5-flash",
            apiKey = androidApplication().getString(R.string.gemini_api_key)
        )
    }

    viewModel {
        TripListViewModel(get())
    }

    viewModel {
        AddTripViewModel(get(), get())
    }

    viewModel { parameters ->
        TripDetailViewModel(
            getTripUseCase = get(),
            savedStateHandle = parameters.get()
        )
    }
}

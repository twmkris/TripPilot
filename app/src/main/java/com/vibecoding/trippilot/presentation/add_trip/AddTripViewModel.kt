
package com.vibecoding.trippilot.presentation.add_trip

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vibecoding.trippilot.domain.model.Trip
import com.vibecoding.trippilot.domain.usecase.AddTripUseCase
import com.vibecoding.trippilot.domain.usecase.GenerateItineraryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddTripViewModel(
    private val addTripUseCase: AddTripUseCase,
    private val generateItineraryUseCase: GenerateItineraryUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "AddTripViewModel"
    }

    // 行程規劃狀態
    private val _planningState = MutableStateFlow<PlanningState>(PlanningState.Idle)
    val planningState: StateFlow<PlanningState> = _planningState.asStateFlow()

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            try {
                // 開始規劃行程
                _planningState.value = PlanningState.Planning
                Log.d(TAG, "開始規劃行程: ${trip.destination}")
                
                // 生成行程
                val itinerary = generateItineraryUseCase(trip)
                Log.d(TAG, "行程生成完成: $itinerary")
                
                // 保存 Trip
                val tripWithItinerary = trip.copy(itinerary = itinerary)
                addTripUseCase(tripWithItinerary)
                Log.d(TAG, "Trip 保存完成")
                
                // 規劃完成
                _planningState.value = PlanningState.Completed
                
            } catch (e: Exception) {
                Log.e(TAG, "行程規劃失敗", e)
                _planningState.value = PlanningState.Error(e.message ?: "未知錯誤")
            }
        }
    }

    // 重置狀態
    fun resetState() {
        _planningState.value = PlanningState.Idle
    }
}

// 行程規劃狀態
sealed class PlanningState {
    object Idle : PlanningState()           // 初始狀態
    object Planning : PlanningState()       // 規劃中
    object Completed : PlanningState()      // 規劃完成
    data class Error(val message: String) : PlanningState() // 錯誤狀態
}

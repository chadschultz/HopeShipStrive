package com.example.hopeshipstrive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopeshipstrive.datastate.DataState
import com.example.hopeshipstrive.model.TripRepository
import com.example.hopeshipstrive.recyclerview.TripListItem
import com.example.hopeshipstrive.recyclerview.TripListItemTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TripRepository,
    private val tripTransformer: TripListItemTransformer,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.trips.collect { result ->
                val tripState = tripTransformer.updateFromResult(
                    _uiState.value.tripState,
                    result
                )
                _uiState.value = _uiState.value.copy(tripState = tripState)
            }
        }

        val newTripState = _uiState.value.tripState.copy(isLoading = true)
        _uiState.value = _uiState.value.copy(tripState = newTripState)
    }

    data class UiState(val tripState: DataState<List<TripListItem>> = DataState())
}
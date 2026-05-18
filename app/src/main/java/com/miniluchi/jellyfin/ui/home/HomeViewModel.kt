package com.miniluchi.jellyfin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.miniluchi.jellyfin.data.repository.MissingApiKeyException
import com.miniluchi.jellyfin.data.repository.MovieRepository
import com.miniluchi.jellyfin.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val movies: List<Movie>) : HomeUiState
    data class Error(val reason: ErrorReason) : HomeUiState
}

enum class ErrorReason { MissingApiKey, Network, Unknown }

class HomeViewModel(
    private val repository: MovieRepository = MovieRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                HomeUiState.Success(repository.getPopularMovies())
            } catch (e: MissingApiKeyException) {
                HomeUiState.Error(ErrorReason.MissingApiKey)
            } catch (e: IOException) {
                HomeUiState.Error(ErrorReason.Network)
            } catch (e: Exception) {
                HomeUiState.Error(ErrorReason.Unknown)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel() as T
        }
    }
}

package com.miniluchi.jellyfin.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.miniluchi.jellyfin.data.repository.MissingApiKeyException
import com.miniluchi.jellyfin.data.repository.MovieRepository
import com.miniluchi.jellyfin.domain.model.MovieDetail
import com.miniluchi.jellyfin.ui.home.ErrorReason
import com.miniluchi.jellyfin.ui.navigation.NavArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val movie: MovieDetail) : DetailUiState
    data class Error(val reason: ErrorReason) : DetailUiState
}

class DetailViewModel(
    private val movieId: Int,
    private val repository: MovieRepository = MovieRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadMovie()
    }

    fun loadMovie() {
        _uiState.value = DetailUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                DetailUiState.Success(repository.getMovieDetail(movieId))
            } catch (e: MissingApiKeyException) {
                DetailUiState.Error(ErrorReason.MissingApiKey)
            } catch (e: IOException) {
                DetailUiState.Error(ErrorReason.Network)
            } catch (e: Exception) {
                DetailUiState.Error(ErrorReason.Unknown)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val handle: SavedStateHandle = createSavedStateHandle()
                val movieId: Int = handle.get<Int>(NavArgs.MOVIE_ID)
                    ?: error("Missing argument: ${NavArgs.MOVIE_ID}")
                DetailViewModel(movieId = movieId)
            }
        }
    }
}

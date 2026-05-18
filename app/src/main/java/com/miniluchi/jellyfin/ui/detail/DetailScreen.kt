package com.miniluchi.jellyfin.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.miniluchi.jellyfin.R
import com.miniluchi.jellyfin.domain.model.MovieDetail
import com.miniluchi.jellyfin.ui.components.ErrorView
import com.miniluchi.jellyfin.ui.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val title = (uiState as? DetailUiState.Success)?.movie?.title
        ?: stringResource(R.string.detail_title)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_content_description),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                DetailUiState.Loading -> LoadingView()
                is DetailUiState.Error -> ErrorView(
                    reason = state.reason,
                    onRetry = viewModel::loadMovie,
                )
                is DetailUiState.Success -> MovieDetailContent(state.movie)
            }
        }
    }
}

@Composable
private fun MovieDetailContent(movie: MovieDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            val image = movie.backdropUrl ?: movie.posterUrl
            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = stringResource(R.string.backdrop_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            if (movie.originalTitle.isNotBlank() && movie.originalTitle != movie.title) {
                Text(
                    text = stringResource(R.string.original_title_label, movie.originalTitle),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            if (movie.tagline.isNotBlank()) {
                Text(
                    text = stringResource(R.string.tagline_label, movie.tagline),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                )
            }

            if (movie.releaseDate.isNotBlank()) {
                Text(
                    text = stringResource(R.string.release_date_label, movie.releaseDate),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (movie.runtimeMinutes > 0) {
                Text(
                    text = stringResource(R.string.runtime_label, movie.runtimeMinutes),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (movie.status.isNotBlank()) {
                Text(
                    text = stringResource(R.string.status_label, movie.status),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            if (movie.genres.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.genres_label, movie.genres.joinToString(", ")),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Text(
                text = stringResource(R.string.rating_label, movie.voteAverage, movie.voteCount),
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = stringResource(R.string.overview_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(
                text = movie.overview.ifBlank { stringResource(R.string.overview_empty) },
                style = MaterialTheme.typography.bodyMedium,
            )

            if (movie.homepage.isNotBlank()) {
                Text(
                    text = stringResource(R.string.homepage_label) + " : " + movie.homepage,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            // Bottom spacer
            Box(modifier = Modifier.padding(bottom = 16.dp))
        }

        // Poster preview (small) at the bottom for visual reference
        if (movie.posterUrl != null) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(0.4f)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = stringResource(R.string.poster_content_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

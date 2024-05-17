package v.kira.cinemadb.features.tv

import v.kira.cinemadb.model.TVShowResult
import javax.inject.Inject

class TVUseCase @Inject constructor(
    private val repository: TVRepository
) {

    suspend fun getAiringTodayTVShows(
        token: String,
        language: String,
        page: Int
    ): List<TVShowResult> {
        return repository.getAiringTodayTVShows(token, language, page)
    }

    suspend fun getTopRatedTVShows(
        token: String,
        language: String,
        page: Int
    ): List<TVShowResult> {
        return repository.getTopRatedTVShows(token, language, page)
    }

    suspend fun getTrendingTVShows(
        token: String,
        language: String,
        page: Int
    ): List<TVShowResult> {
        return repository.getTrendingTVShows(token, language, page)
    }

    suspend fun getTVShowDetails(
        token: String,
        tvSeriesId: Long,
        language: String
    ): TVShowResult {
        return repository.getTvShowDetails(token, tvSeriesId, language)
    }
}
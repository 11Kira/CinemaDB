package v.kira.cinemadb.features.tv

import v.kira.cinemadb.model.TVResult
import javax.inject.Inject

class TVUseCase @Inject constructor(
    private val repository: TVRepository
) {

    suspend fun getAiringTodayTVShows(
        token: String,
        language: String,
        page: Int
    ): List<TVResult> {
        return repository.getAiringTodayTVShows(token, language, page)
    }

    suspend fun getTopRatedTVShows(
        token: String,
        language: String,
        page: Int
    ): List<TVResult> {
        return repository.getTopRatedTVShows(token, language, page)
    }

    suspend fun getTrendingTVShows(
        token: String,
        language: String,
        page: Int
    ): List<TVResult> {
        return repository.getTrendingTVShows(token, language, page)
    }
}
package com.kira.android.cinemadb.model

import com.google.gson.annotations.SerializedName

data class TVShowResult (
    @SerializedName("id")
    val id: Long,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,
    @SerializedName("number_of_episodes")
    val numberOfEpisodes: Int,
    @SerializedName("genres")
    val genres: List<Genre>,
    @SerializedName("origin_country")
    val originCountry: List<String>,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("last_air_date")
    val lastAirDate: String,
    @SerializedName("credits")
    val credits: Credits?,
    var accountStates: AccountStates?,
    )
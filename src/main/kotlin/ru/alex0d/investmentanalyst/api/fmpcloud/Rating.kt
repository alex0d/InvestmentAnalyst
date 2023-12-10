package ru.alex0d.investmentanalyst.api.fmpcloud

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    var symbol: String,
    var date: String?,
    var rating: String?,
    var ratingScore: Int?,
    var ratingRecommendation: String?,
    var ratingDetailsDCFScore: Int?,
    var ratingDetailsDCFRecommendation: String?,
    var ratingDetailsROEScore: Int?,
    var ratingDetailsROERecommendation: String?,
    var ratingDetailsROAScore: Int?,
    var ratingDetailsROARecommendation: String?,
    var ratingDetailsDEScore: Int?,
    var ratingDetailsDERecommendation: String?,
    var ratingDetailsPEScore: Int?,
    var ratingDetailsPERecommendation: String?,
    var ratingDetailsPBScore: Int?,
    var ratingDetailsPBRecommendation: String?
)
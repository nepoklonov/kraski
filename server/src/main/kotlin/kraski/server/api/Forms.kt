package kraski.server.api

import io.ktor.routing.Route
import kraski.common.Answer
import kraski.common.Method
import kraski.common.Request
import kraski.common.models.NewsWithSrc
import kraski.common.models.RaskraskaWithSrc
import kraski.common.models.Review
import kraski.server.database.getAllNews
import kraski.server.database.getAllRaskraska
import kraski.server.database.getAllReview
import kotlinx.serialization.list


fun Route.startNewsGetAllAPI() = listenAndAutoRespond<Request.NewsGetAll>(Method.NewsGetAll) { request, _ ->
    val news = getAllNews(request.width, request.height)
    Answer.ok(NewsWithSrc.serializer().list, news)
}

fun Route.startRaskraskaGetAllAPI() = listenAndAutoRespond<Request.RaskraskaGetAll>(Method.RaskraskaGetAll) { request, _ ->
    val raskraska = getAllRaskraska(request.width, request.height)
    Answer.ok(RaskraskaWithSrc.serializer().list, raskraska)
}

fun Route.startReviewsGetAllAPI() = listenAndAutoRespond<Request.ReviewsGetAll>(Method.ReviewsGetAll) { _, _ ->
    Answer.ok(Review.serializer().list, getAllReview())
}
package org.codosoft.topis2.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MovieService(
    private val client: HttpClient) {

    suspend fun getFetchMovies(): RemoteResult{
        return client
            .get("/3/discover/movie?sort_by=popularity.desc")
            .body<RemoteResult>()
    }

    suspend fun getMovieDetail(id: Int): RemoteMovie{
        return client
            .get("/3/movie/$id")
            .body<RemoteMovie>()
    }
}
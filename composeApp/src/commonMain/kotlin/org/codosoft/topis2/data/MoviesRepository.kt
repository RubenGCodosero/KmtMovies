package org.codosoft.topis2.data

import org.codosoft.topis2.Movie

class MoviesRepository(private val movieService: MovieService) {
    suspend fun fetchPopularMovies(): List<Movie> {
        return movieService.getFetchMovies().results.map { it.toDomainMovie() }
    }

    suspend fun fetchMovieById(id: Int): Movie {
        return movieService.getMovieDetail(id).toDomainMovie()
    }

    private fun RemoteMovie.toDomainMovie() = Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        poster = "https://image.tmdb.org/t/p/w185$posterPath",
        backdrop = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" },
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity,
        voteAverage = voteAverage
    )
}
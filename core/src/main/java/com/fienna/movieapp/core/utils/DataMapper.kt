package com.fienna.movieapp.core.utils

import com.fienna.movieapp.core.data.local.entity.CartEntity
import com.fienna.movieapp.core.data.local.entity.WishlistEntity
import com.fienna.movieapp.core.data.remote.data.CreditResponse
import com.fienna.movieapp.core.data.remote.data.DetailMovieResponse
import com.fienna.movieapp.core.data.remote.data.NowPlayingResponse
import com.fienna.movieapp.core.data.remote.data.PaymentResponse
import com.fienna.movieapp.core.data.remote.data.PopularResponse
import com.fienna.movieapp.core.data.remote.data.SearchResponse
import com.fienna.movieapp.core.data.remote.data.UpComingResponse
import com.fienna.movieapp.core.domain.model.DataCart
import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.core.domain.model.DataDetailMovie
import com.fienna.movieapp.core.domain.model.DataNowPlaying
import com.fienna.movieapp.core.domain.model.DataPayment
import com.fienna.movieapp.core.domain.model.DataPopular
import com.fienna.movieapp.core.domain.model.DataSearch
import com.fienna.movieapp.core.domain.model.DataSession
import com.fienna.movieapp.core.domain.model.DataUpcoming
import com.fienna.movieapp.core.domain.model.DataWishlist
import com.fienna.movieapp.core.domain.state.SplashState

object DataMapper {
    fun Triple<String?, String, Boolean>.toUiData() = DataSession(
        userName = this.first,
        userId = this.second,
        onBoardingState = this.third
    )

    fun DataSession.toSplashState() = when{
        this.userName?.isEmpty() == true ->{
            SplashState.Profile
        }
        this.userName?.isNotEmpty() == true-> {
            SplashState.Dashboard
        }
        this.onBoardingState -> {
            SplashState.Login
        }
        else -> {
            SplashState.OnBoarding
        }
    }

    fun NowPlayingResponse.Result.toUiData() = DataNowPlaying(
        backdropPath = backdropPath,
        posterPath = posterPath,
        id = id,
        title = title,
        genreIds = genreIds,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

    fun NowPlayingResponse.toUiListData() = results.map { result -> result.toUiData() }.toList()

    fun UpComingResponse.Result.toUIData() = DataUpcoming(
        posterPath=posterPath,
        id=id,
        title=title,
        voteAverage=voteAverage,
        voteCount=voteCount,
        releaseDate=releaseDate,
        genreIds = genreIds,
        popularity = popularity
    )

    fun UpComingResponse.toUiListData() = results.map { result -> result.toUIData() }.toList()

    fun PopularResponse.Result.toUIData() = DataPopular(
        posterPath=posterPath,
        id=id,
        title=title,
        voteAverage=voteAverage,
        voteCount=voteCount,
        releaseDate=releaseDate,
        genreIds = genreIds,
        popularity = popularity
    )

    fun PopularResponse.toUiListData() = results.map { result -> result.toUIData() }.toList()

    fun DetailMovieResponse.toUiData() = DataDetailMovie(
        id = id,
        title = title,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        overview = overview,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        posterPath = posterPath,
        genres = genres.map { genre ->
            DataDetailMovie.Genre(
                genreId = genre.id,
                name = genre.name
            )
        }
    )

    fun CreditResponse.Cast.toUiData() = DataCredit(
        character = character,
        name = name,
        profilePath = profilePath
    )
    fun CreditResponse.toUiListData() = cast.map { cast -> cast.toUiData() }.toList()

    fun SearchResponse.Result.toUiData() = DataSearch(
        id = id,
        genreIds = genreIds,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

    fun SearchResponse.toUiListData() = results.map { result -> result.toUiData() }.toList()

    fun CartEntity.toUiData() = DataCart(
        cartId = cartId,
        userId = userId,
        movieId = movieId,
        posterPath = posterPath,
        title = title,
        genreName = genreName,
        popularity = popularity
    )

    fun DataCart.toEntity() =CartEntity(
        cartId = cartId,
        userId = userId,
        movieId = movieId,
        posterPath = posterPath,
        title = title,
        genreName = genreName,
        popularity = popularity
    )

    fun WishlistEntity.toUiData() = DataWishlist(
        wishlistId = wishlistId,
        userId = userId,
        movieId = movieId,
        posterPath = posterPath,
        title = title,
        popularity = popularity,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

    fun DataWishlist.toEntity() = WishlistEntity(
        wishlistId = wishlistId,
        userId = userId,
        movieId = movieId,
        posterPath = posterPath,
        title = title,
        popularity = popularity,
        voteAverage = voteAverage,
        voteCount = voteCount
    )

    fun PaymentResponse.Data.toUiData() = DataPayment(
        title = title,
        item = item.map {item ->
            DataPayment.Item(
                image = item.image,
                label = item.label,
                status = item.status
            )

        }
    )

    fun PaymentResponse.toListData() = data.map { data ->  data.toUiData() }.toList()

}
package com.example.laboratorio05.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.laboratorio05.data.model.MovieModel
import com.example.laboratorio05.data.model.MovieWithActor

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_table")
    suspend fun getAllMovies() : List<MovieModel>

    @Transaction
    @Insert
    suspend fun insertMovie(movie: MovieModel)

    @Query("SELECT * FROM movie_table WHERE movieId = :id")
    suspend fun getMovieWithActorsById(id: Int): MovieWithActor?
}
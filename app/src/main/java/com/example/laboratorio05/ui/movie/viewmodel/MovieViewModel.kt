package com.example.laboratorio05.ui.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.laboratorio05.MovieReviewerApplication
import com.example.laboratorio05.data.model.MovieModel
import com.example.laboratorio05.repositories.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    var id = MutableLiveData(0)
    var name = MutableLiveData("")
    var category = MutableLiveData("")
    var description =  MutableLiveData("")
    var qualification =  MutableLiveData("")
    var status = MutableLiveData("")



    suspend fun getMovies(): List<MovieModel> = repository.getMovies()

    suspend fun getMovieWithActorById(movieId: Int) = repository.getMoviesWithActors(movieId)

    private fun addMovies(movie: MovieModel) {
        viewModelScope.launch {
            repository.addMovies(movie)
        }
    }

    fun createMovie() {
        if (!validateData()) {
            status.value = WRONG_INFORMATION
            return
        }

        val movie = MovieModel(
            name = name.value!!,
            category = category.value!!,
            description = description.value!!,
            qualification = qualification.value!!
        )

        addMovies(movie)
        clearData()

        status.value = MOVIE_CREATED
    }

    private fun validateData(): Boolean {
        when {
            name.value.isNullOrEmpty() -> return false
            category.value.isNullOrEmpty() -> return false
            description.value.isNullOrEmpty() -> return false
            qualification.value.isNullOrEmpty() -> return false
        }
        return true
    }

    fun clearData() {
        id.value = 0
        name.value = ""
        category.value = ""
        description.value = ""
        qualification.value = ""
    }

    fun clearStatus() {
        status.value = INACTIVE
    }

    fun setSelectedMovie(movie: MovieModel) {
        id.value = movie.movieId
        name.value = movie.name
        category.value = movie.category
        description.value = movie.description
        qualification.value = movie.qualification
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MovieReviewerApplication
                MovieViewModel(app.movieRepository)
            }
        }

        const val MOVIE_CREATED = "Movie created"
        const val WRONG_INFORMATION = "Wrong information"
        const val INACTIVE = ""
    }

}
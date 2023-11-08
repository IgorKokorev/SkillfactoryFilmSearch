package film.search.filmsearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import film.search.filmsearch.App
import film.search.filmsearch.domain.Film
import film.search.filmsearch.domain.Interactor

class MainFragmentViewModel : ViewModel() {
    val filmsListLiveData = MutableLiveData<List<Film>>()
    private var interactor: Interactor = App.instance.interactor
    init {
        val films = interactor.getFilmsDB()
        filmsListLiveData.postValue(films)

//        val client = OkHttpClient()
//
//        val request1 = Request.Builder()
//            .url("https://api.themoviedb.org/3/authentication")
//            .get()
//            .addHeader("accept", "application/json")
//            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDc4NzQzNGYzMzg1NjcyYWVlYTA4MDAzMzRjMWUxNyIsInN1YiI6IjY1NDRkNGE3MWFjMjkyMDExYjNkNzgzYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.PLydGoD3-dmqbnBIPWQanSIlWBfNby_BJIkKGMO49_0")
//            .build()
//
//        val response1 = client.newCall(request1).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//            override fun onResponse(call: Call, response: Response) {
//                try {
//                    val responseBody = response.body
//                    println("!!! ${responseBody?.string()}")
//                } catch (e: Exception) {
//                    println("!!! $response")
//                    e.printStackTrace()
//                }
//            }
//        })
//
//
//        val request2 = Request.Builder()
//            .url("https://api.themoviedb.org/3/search/movie?query=weaks&include_adult=true&language=en-US&page=1")
//            .get()
//            .addHeader("accept", "application/json")
//            .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4MDc4NzQzNGYzMzg1NjcyYWVlYTA4MDAzMzRjMWUxNyIsInN1YiI6IjY1NDRkNGE3MWFjMjkyMDExYjNkNzgzYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.PLydGoD3-dmqbnBIPWQanSIlWBfNby_BJIkKGMO49_0")
//            .build()
//
//        val response2 = client.newCall(request2).execute()
//        response2.body
    }
}

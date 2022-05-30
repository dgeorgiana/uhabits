package org.isoron.uhabits

import retrofit2.Call
import retrofit2.http.GET

interface HabitsIdeasService {

    @GET("countries")
    fun getAffectedCountryList () : Call<List<MyHabitsIdea>>
}
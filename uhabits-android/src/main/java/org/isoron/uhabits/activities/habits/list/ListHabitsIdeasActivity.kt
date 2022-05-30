package org.isoron.uhabits.activities.habits.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_list_habits_ideas.*
import org.isoron.uhabits.*
import retrofit2.Call
import retrofit2.Response

class ListHabitsIdeasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_habits_ideas)
        loadHabits()
    }

    private fun loadHabits() {

        //initiate the service
        val destinationService  = HabitsIdeasServiceBuilder.buildService(HabitsIdeasService::class.java)
        val requestCall = destinationService.getAffectedCountryList()

        //make network call asynchronously
        requestCall.enqueue(object : retrofit2.Callback<List<MyHabitsIdea>> {
            override fun onResponse(call: Call<List<MyHabitsIdea>>, response: Response<List<MyHabitsIdea>>) {
                Log.d("Response", "onResponse: ${response.body()}")
                if (response.isSuccessful){
                    val ideasList  = response.body()!!
                    Log.d("Response", "countrylist size : ${ideasList.size}")
                    ideas_recycler.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@ListHabitsIdeasActivity,2)
                        adapter = HabitsIdeasAdapter(response.body()!!)
                    }
                }else{
                    Toast.makeText(this@ListHabitsIdeasActivity, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<MyHabitsIdea>>, t: Throwable) {
                Toast.makeText(this@ListHabitsIdeasActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.example.visionmobile.ui.providers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.example.visionmobile.ApiClient
import com.example.visionmobile.R
import com.example.visionmobile.model.ArticleModel
import com.example.visionmobile.model.ProviderModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProviderDetailFragment : Fragment() {

    var root : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        root = inflater.inflate(R.layout.fragment_provider_detail, container, false)
        var id = arguments?.getInt("providerId")

        getProvider(root!!, id!!)
        return root
    }

    fun getProvider(root : View, id: Int) {
        val listCall = ApiClient().getProviderById(id)

        listCall.enqueue(object : Callback<ProviderModel> {
            override fun onResponse(call: Call<ProviderModel>, response: Response<ProviderModel>) {
                val provider = response.body()

                root.findViewById<TextView>(R.id.textViewProviderBusinessname).setText(provider!!.businessname)
                root.findViewById<TextView>(R.id.textViewProviderCuit).setText(provider!!.cuit.toString())
                root.findViewById<TextView>(R.id.textViewProviderLocation).setText(provider!!.address  + " - " + provider!!.location + " - " + provider!!.province)
                root.findViewById<TextView>(R.id.textViewProviderEmail).setText(provider!!.email)

                root.findViewById<TextView>(R.id.textViewCreatedAt).setText(provider!!.created_at)
                root.findViewById<TextView>(R.id.textViewUpdatedAt).setText(provider!!.updated_at)
                requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ProviderModel>, t: Throwable) {
                Log.e("error", t.message.toString())
                requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }
        })

    }

}
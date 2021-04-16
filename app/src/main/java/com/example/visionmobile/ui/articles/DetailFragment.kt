package com.example.visionmobile.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.visionmobile.ApiClient
import com.example.visionmobile.R
import com.example.visionmobile.model.ArticleModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailFragment : Fragment() {
    companion object {
        fun newInstance() = DetailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        val root = inflater.inflate(R.layout.detail_fragment, container, false)
        var id = arguments?.getInt("articleId")

        getArticle(root, id!!)

        return root
    }

    fun getArticle(root : View, id: Int) {
        val listCall = ApiClient().getArticleById(id)

        listCall.enqueue(object : Callback<ArticleModel> {
            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                val article = response.body()

                root.findViewById<TextView>(R.id.article_product).setText(article!!.line + " " + article!!.product )
                root.findViewById<TextView>(R.id.article_price).setText("$${article!!.price}")
                root.findViewById<TextView>(R.id.article_description).setText(article!!.description)
                root.findViewById<TextView>(R.id.textViewCreatedAt).setText(article!!.created_at)
                root.findViewById<TextView>(R.id.textViewUpdatedAt).setText(article!!.updated_at)

                root.findViewById<TextView>(R.id.textViewProviderBusinessname).setText(article!!.provider!!.businessname)
                root.findViewById<TextView>(R.id.textViewProviderCuit).setText(article!!.provider!!.cuit.toString())
                root.findViewById<TextView>(R.id.textViewProviderLocation).setText(article!!.provider!!.address  + " - " + article!!.provider!!.location + " - " + article!!.provider!!.province)
                root.findViewById<TextView>(R.id.textViewProviderEmail).setText(article!!.provider!!.email)
                requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                Log.e("error", t.message.toString())
                requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }
        })

    }
}
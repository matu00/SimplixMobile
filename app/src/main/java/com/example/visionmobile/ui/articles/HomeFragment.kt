package com.example.visionmobile.ui.articles

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.visionmobile.ApiClient
import com.example.visionmobile.R
import com.example.visionmobile.adapter.ItemArticleAdapter
import com.example.visionmobile.model.ArticleModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    var root : View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        root = inflater.inflate(R.layout.fragment_home, container, false)
        root!!.findViewById<FloatingActionButton>(R.id.create_article).setOnClickListener {
            findNavController(root!!).navigate(R.id.create_article_fragment)
        }

        root!!.findViewById<FloatingActionButton>(R.id.scan_barcode).setOnClickListener {
            findNavController(root!!).navigate(R.id.scan_barcode_fragment)
        }

        root!!.findViewById<EditText>(R.id.searchProvider).setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            v.clearFocus()
            root!!.hideKeyboard()

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searhArticle(v)
                return@OnEditorActionListener true
            }
            false
        })

        getAllArticles()

        return root
    }

    fun getAllArticles() {
        val lvArticles = root!!.findViewById<ListView>(R.id.lvArticles)
        val listCall = ApiClient().getArticles()

        listCall.enqueue(object : Callback<List<ArticleModel>> {
            override fun onResponse(
                    call: Call<List<ArticleModel>>,
                    response: Response<List<ArticleModel>>
            ) {
                lvArticles.adapter = ItemArticleAdapter(this@HomeFragment.requireContext(), response.body()!!)
                activity!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<List<ArticleModel>>, t: Throwable) {
                Log.e("error", t.message.toString())
            }
        })
    }

    fun searhArticle(textView: TextView) {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        if (textView.getText().toString().isNullOrEmpty()) {
            getAllArticles()
        }
        else {
            val lvArticles = root!!.findViewById<ListView>(R.id.lvArticles)
            val listCall = ApiClient().searchArticle(textView.getText().toString())

            listCall.enqueue(object : Callback<List<ArticleModel>> {
                override fun onResponse(
                        call: Call<List<ArticleModel>>,
                        response: Response<List<ArticleModel>>
                ) {
                    if (response.isSuccessful) {
                        lvArticles.adapter = ItemArticleAdapter(this@HomeFragment.requireContext(), response.body()!!)

                        if (lvArticles.adapter.isEmpty())
                            Toast.makeText(getActivity(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }

                    activity!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<List<ArticleModel>>, t: Throwable) {
                    Log.e("error", t.message.toString())
                }
            })
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
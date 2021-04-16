package com.example.visionmobile.ui.providers

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
import androidx.navigation.Navigation
import com.example.visionmobile.ApiClient
import com.example.visionmobile.R
import com.example.visionmobile.adapter.ItemArticleAdapter
import com.example.visionmobile.adapter.ItemProviderAdapter
import com.example.visionmobile.model.ArticleModel
import com.example.visionmobile.model.ProviderModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProviderHomeFragment : Fragment() {
    var root : View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        root = inflater.inflate(R.layout.fragment_provider_home, container, false)

        root!!.findViewById<FloatingActionButton>(R.id.createProvider).setOnClickListener {
            Navigation.findNavController(root!!).navigate(R.id.fragment_provider_create)
        }

        root!!.findViewById<EditText>(R.id.searchProvider).setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            v.clearFocus()
            root!!.hideKeyboard()

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searhProvider(v)
                return@OnEditorActionListener true
            }
            false
        })

        getAllProviders()

        return root
    }

    fun getAllProviders() {
        val lvProviders = root!!.findViewById<ListView>(R.id.lvProviders)
        val listCall = ApiClient().getProviders()

        listCall.enqueue(object : Callback<List<ProviderModel>> {
            override fun onResponse(
                    call: Call<List<ProviderModel>>,
                    response: Response<List<ProviderModel>>
            ) {
                val a  =response.body()
                lvProviders.adapter = ItemProviderAdapter(this@ProviderHomeFragment.requireContext(), response.body()!!)
                activity!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<List<ProviderModel>>, t: Throwable) {
                Log.e("error", t.message.toString())
            }
        })
    }

    fun searhProvider(textView: TextView) {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        if (textView.getText().toString().isNullOrEmpty()) {
            getAllProviders()
        }
        else {
            val lvProvider = root!!.findViewById<ListView>(R.id.lvProviders)
            val listCall = ApiClient().searchProvider(textView.getText().toString())

            listCall.enqueue(object : Callback<List<ProviderModel>> {
                override fun onResponse(
                    call: Call<List<ProviderModel>>,
                    response: Response<List<ProviderModel>>
                ) {
                    if (response.isSuccessful) {
                        lvProvider.adapter = ItemProviderAdapter(this@ProviderHomeFragment.requireContext(), response.body()!!)

                        if (lvProvider.adapter.isEmpty())
                            Toast.makeText(getActivity(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    }

                    activity!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
                }

                override fun onFailure(call: Call<List<ProviderModel>>, t: Throwable) {
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
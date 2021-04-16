package com.example.visionmobile.ui.articles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.visionmobile.ApiClient
import com.example.visionmobile.CaptureActivityCustom
import com.example.visionmobile.R
import com.example.visionmobile.model.ArticleModel
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateArticleFragment : Fragment() {
    var root : View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_create_article, container, false)

        root!!.findViewById<Button>(R.id.createArticle).setOnClickListener {
            createArticle(root!!)
        }

        root!!.findViewById<Button>(R.id.getCodeBar).setOnClickListener {
            scanQRCode()
        }


        return root
    }

    fun createArticle(root: View) {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        root.hideKeyboard()

        val userInfo = ArticleModel(
                id = null,
                line = root.findViewById<EditText>(R.id.editTextLine).text.toString(),
                code = root.findViewById<EditText>(R.id.editTextCode).text.toString(),
                product = root.findViewById<EditText>(R.id.editTextProduct).text.toString(),
                description = root.findViewById<EditText>(R.id.editTextDescription).text.toString(),
                price = root.findViewById<EditText>(R.id.editTextPrice).text.toString(),
                coefficient = null,
                provider_id = "1",
                created_at = null,
                updated_at = null,
                codebar = root.findViewById<EditText>(R.id.editTextCodeBar).text.toString(),
                provider = null)

        val listCall = ApiClient().createArticle(userInfo)

        listCall.enqueue(
                object : Callback<ArticleModel> {
                    override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                        Log.e("error", t.message.toString())
                        Toast.makeText(getActivity(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
                    }

                    override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                        if (response.isSuccessful) {
                            Navigation.findNavController(root).navigate(R.id.nav_home)
                            Toast.makeText(getActivity(), "Se registró un nuevo articulo", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Ocurrió un error al registrar un nuevo articulo", Toast.LENGTH_SHORT).show();
                        }
                        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
                    }
                }
        )
    }

    private fun scanQRCode(){
        val intent = IntentIntegrator.forSupportFragment(this@CreateArticleFragment)
        intent.setCaptureActivity(CaptureActivityCustom::class.java)
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intent.setPrompt("ESCANEAR CODIGO")
        intent.setCameraId(0)
        intent.setBarcodeImageEnabled(false)
        intent.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            root!!.findViewById<EditText>(R.id.editTextCodeBar).setText(result.contents)
            root!!.findViewById<EditText>(R.id.editTextCode).setText(result.contents)
            Toast.makeText(getActivity(), "Se escaneó correctamente", Toast.LENGTH_SHORT).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
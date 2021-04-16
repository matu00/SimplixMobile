package com.example.visionmobile.ui.articles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

class ScanBarcodeFragment : Fragment() {
    var root : View? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.VISIBLE

        root = inflater.inflate(R.layout.fragment_scan_barcode, container, false)
        val btnScan: Button = root!!.findViewById(R.id.scan_barcode_1)
        btnScan.setOnClickListener {
            scanQRCode()
        }

        scanQRCode()

        return root
    }

    private fun scanQRCode(){
        val intent = IntentIntegrator.forSupportFragment(this@ScanBarcodeFragment)
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
            getArticleByCodeBar(result.contents)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getArticleByCodeBar(codeBar: String){
        val article = ArticleModel(
                id = null,
                line = null,
                code = null,
                product = null,
                description = null,
                price = null,
                coefficient = null,
                provider_id = "1",
                created_at = null,
                updated_at = null,
                codebar = codeBar,
                provider = null)

        val listCall = ApiClient().getArticleByCodeBar(article)

        listCall.enqueue(object : Callback<ArticleModel> {
            override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                if (response.isSuccessful) {
                    val article = response.body()
                    root!!.findViewById<TextView>(R.id.article_product).setText(article!!.line + " " + article!!.product )
                    root!!.findViewById<TextView>(R.id.article_price).setText("$${article!!.price}")
                    root!!.findViewById<TextView>(R.id.article_description).setText(article!!.description)
                    root!!.findViewById<TextView>(R.id.textViewCreatedAt).setText(article!!.created_at)
                    root!!.findViewById<TextView>(R.id.textViewUpdatedAt).setText(article!!.updated_at)

                    root!!.findViewById<TextView>(R.id.textViewProviderBusinessname).setText(article!!.provider!!.businessname)
                    root!!.findViewById<TextView>(R.id.textViewProviderCuit).setText(article!!.provider!!.cuit.toString())
                    root!!.findViewById<TextView>(R.id.textViewProviderLocation).setText(article!!.provider!!.address  + " - " + article!!.provider!!.location + " - " + article!!.provider!!.province)
                    root!!.findViewById<TextView>(R.id.textViewProviderEmail).setText(article!!.provider!!.email)
                } else {
                    Toast.makeText(getActivity(), "No se pudo encontrar articulo", Toast.LENGTH_SHORT).show();
                }

                requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }

            override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
                Log.e("error", t.message.toString())
                requireActivity()!!.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.INVISIBLE
            }
        })
    }
}
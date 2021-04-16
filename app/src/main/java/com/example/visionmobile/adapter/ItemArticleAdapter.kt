package com.example.visionmobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import com.example.visionmobile.R
import com.example.visionmobile.model.ArticleModel
import com.example.visionmobile.model.ProviderModel


class ItemArticleAdapter(private val mContext: Context, private val articles: List<ArticleModel>) : ArrayAdapter<ArticleModel>(
        mContext,
        0,
        articles
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_article, parent, false)

        val article = articles[position]

        layout.findViewById<TextView>(R.id.name).text = article.product + "-" + article.description + "-" + article.code
        layout.findViewById<TextView>(R.id.price).text = "$${article.price}"

        layout.setOnClickListener {
            val bundle = bundleOf("articleId" to article.id)
            findNavController(layout).navigate(R.id.detail_fragment, bundle);
        }

        return layout
    }
}

class ItemProviderAdapter(private val mContext: Context, private val provider: List<ProviderModel>) : ArrayAdapter<ProviderModel>(
        mContext,
        0,
        provider
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_provider, parent, false)

        val provider = provider[position]

        layout.findViewById<TextView>(R.id.tVProvideName).text = provider.businessname
        layout.findViewById<TextView>(R.id.tVProvideCuit).text = if (provider.cuit == null) "-" else provider.cuit.toString()

        layout.setOnClickListener {
            val bundle = bundleOf("providerId" to provider.id)
            findNavController(layout).navigate(R.id.fragment_provider_detail, bundle);
        }

        return layout
    }
}
package com.aysesenses.n11_casestudy.ui.adapter

import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aysesenses.data.local.entitiy.UserEntity
import com.aysesenses.n11_casestudy.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("listUser")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<UserEntity>?) {
    val adapter = recyclerView.adapter as UserListAdapter
    adapter.submitList(data)
}

//Inside the binding adapter, use Glide to download the image display it in imgView
@BindingAdapter("avatarImageUrl")
fun bindImage(userAvatarImageView: ImageView, userAvatarImageUrl: String?) {
    userAvatarImageUrl?.let {
        val imgUri = userAvatarImageUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(userAvatarImageView.context)
            .load(imgUri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .transform(RoundedCorners(16))
            .into(userAvatarImageView)
    }
}

@BindingAdapter("favorite")
fun setImage(imageView: ImageView, favorite : String?) {
    if (favorite == "no") {
        imageView.setImageResource(android.R.drawable.btn_star_big_off)
    } else {
        imageView.setImageResource(android.R.drawable.btn_star_big_on)
    }
}

@BindingAdapter("detailFavorite")
fun updateImage(imageView: ImageView, favorite : Boolean) {

    if (favorite) {
        Log.e("as","1")
        imageView.setImageResource(android.R.drawable.btn_star_big_on)
    } else {
        Log.e("as","2")
        imageView.setImageResource(android.R.drawable.btn_star_big_off)
    }
}
package com.example.plogging.ui.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.plogging.R

@BindingAdapter("displayImageRound")
fun displayImageRound(view: ImageView, url : String?) {
    if (url != null) {
        Glide.with(view)
            .load(url)
            .circleCrop()
            .error(R.drawable.ic_user_empty_profile_24)
            .into(view)
    } else {
        view.setImageResource(R.drawable.ic_user_empty_profile_24)
    }
}
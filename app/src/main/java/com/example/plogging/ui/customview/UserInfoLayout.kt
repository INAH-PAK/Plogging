package com.example.plogging.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.plogging.R
import com.example.plogging.databinding.ViewUserinfolayoutBinding

class UserInfoLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding: ViewUserinfolayoutBinding

    init {
        binding = ViewUserinfolayoutBinding.inflate(LayoutInflater.from(context), this, true)
        context.theme.obtainStyledAttributes(attrs, R.styleable.UserInfoLayout, 0, 0)
            .use { array ->
                val labelResId = array.getResourceId(R.styleable.UserInfoLayout_userInfoLabel, 0)
                val userProfile =
                    array.getResourceId(R.styleable.UserInfoLayout_userProfileImage, 0)
                setInfoLabel(labelResId)
                setUserProfile(userProfile)
            }
    }

    private fun setInfoLabel(labelResId: Int) {
        binding.tvName.text = context.getString(labelResId)
    }

    private fun setUserProfile(userProfile: Int) {
        // TODO :  binding adapter 의 displatImageRound 를 이용해야 함.
    }
}
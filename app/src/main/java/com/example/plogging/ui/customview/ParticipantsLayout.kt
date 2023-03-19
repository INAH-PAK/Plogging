package com.example.plogging.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.plogging.R
import com.example.plogging.databinding.ViewParticitateinfolayoutBinding

class ParticipantsLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding: ViewParticitateinfolayoutBinding

    init {
        binding = ViewParticitateinfolayoutBinding.inflate(LayoutInflater.from(context), this, true)
        context.theme.obtainStyledAttributes(attrs, R.styleable.ParticipantsLayout, 0, 0)
            .use { array ->
                val labelResId =
                    array.getResourceId(R.styleable.ParticipantsLayout_participantsLabel, 0)
                val ParticipantsCount =
                    array.getResourceId(R.styleable.ParticipantsLayout_userCount, 0)
                setInfoLabel(labelResId)
                setParticipantsCount(ParticipantsCount)
            }
    }

    private fun setInfoLabel(labelResId: Int) {
        binding.tvUserCount.text = context.getText(labelResId)
    }

    private fun setParticipantsCount(count: Int) {
        // TODO : recycler에 참가자 등록하기.
    }
}
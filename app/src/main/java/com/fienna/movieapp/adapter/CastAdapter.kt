package com.fienna.movieapp.adapter

import android.view.View
import coil.load
import com.fienna.movieapp.core.base.BaseListAdapter
import com.fienna.movieapp.core.domain.model.DataCredit
import com.fienna.movieapp.databinding.ItemActorBinding
import com.fienna.movieapp.utils.AppConstant

class CastAdapter ():BaseListAdapter<DataCredit, ItemActorBinding>(ItemActorBinding::inflate){
    override fun onItemBind(): (DataCredit, ItemActorBinding, View, Int) -> Unit =
        {data, binding, view, _ ->
            binding.run {
                imgActor.load(AppConstant.imageLink + data.profilePath)
                tvCastName.text = data.name
                tvCastCharacter.text =data.character
            }
        }

}
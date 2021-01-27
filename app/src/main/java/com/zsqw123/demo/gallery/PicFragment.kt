package com.zsqw123.demo.gallery

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.zsqw123.demo.gallery.databinding.FragPicBinding

/**
 * 在使用 PicFragment 之前, 请务必传入 nowPicture
 */
class PicFragment : Fragment() {
    private lateinit var binding: FragPicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragPicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bigIv.transitionName = "clickedIv"
        Glide.with(this).load(nowPicture.getUrl(720)).into(binding.bigIv)
        binding.bigIv.setOnClickListener { navigator.pop() }
    }

    companion object {
        lateinit var nowPicture: Picture
    }
}
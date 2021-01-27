package com.zsqw123.demo.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.zsqw123.demo.gallery.databinding.FragRootBinding

class RootFragment : Fragment(R.layout.frag_root) {
    private lateinit var binding: FragRootBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragRootBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickedIv.setOnClickListener {
            navigator.push(PicFragment::class, null, FragmentNavigatorExtras(it to "clickedIv"))

        }
    }
}
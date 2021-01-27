package com.zsqw123.demo.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.fragivity.MyNavHost
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.zsqw123.demo.gallery.databinding.FragRootBinding
import com.zsqw123.demo.gallery.databinding.RootRvItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RootFragment : Fragment(R.layout.frag_root) {
    private lateinit var binding: FragRootBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragRootBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rootRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val service = NetworkBase.creatService(TujianService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            val picsResp = service?.today()
            val body = picsResp?.body()
            if (body != null && picsResp.isSuccessful) {
                withContext(Dispatchers.Main) {
                    val adapter = RootAdapter(navigator, body)
                    binding.rootRv.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}

class RootAdapter(private val naviHost: MyNavHost, private val picList: List<Picture>) : RecyclerView.Adapter<RootHolder>() {
    private lateinit var binding: RootRvItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootHolder {
        binding = RootRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RootHolder(binding)
    }

    override fun onBindViewHolder(holder: RootHolder, position: Int) {
        holder.itemBinding.clickIvItem.apply {
            val url = picList[position].getUrl(720)
            println(url)
            Glide.with(this).load(url).into(this)
//            Glide.with(this).asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
//                override fun onLoadCleared(placeholder: Drawable?) {}
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    (this@apply).setImageBitmap(resource)
//                }
//            })
            setOnClickListener {
                transitionName = "clickedIv"
                PicFragment.nowPicture = picList[position]
                naviHost.push(PicFragment::class, null, FragmentNavigatorExtras(it to "clickedIv"))
            }
        }
    }

    override fun getItemCount(): Int = picList.size


}

class RootHolder(val itemBinding: RootRvItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {}
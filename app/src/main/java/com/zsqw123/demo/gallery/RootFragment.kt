package com.zsqw123.demo.gallery

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zsqw123.demo.gallery.databinding.FragRootBinding
import com.zsqw123.demo.gallery.databinding.RootRvItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RootFragment : Fragment(R.layout.frag_root) {
    private lateinit var binding: FragRootBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragRootBinding.inflate(layoutInflater, container, false)
        reenterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
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
                val manyOfBody = mutableListOf<Picture>()
                repeat(10) {
                    manyOfBody.addAll(body)
                }
                withContext(Dispatchers.Main) {
                    val adapter = RootAdapter(activity, manyOfBody)
                    binding.rootRv.adapter = adapter
                }
            }
        }
    }
}

class RootAdapter(private val activity: Activity?, private val picList: List<Picture>) : RecyclerView.Adapter<RootHolder>() {
    private lateinit var binding: RootRvItemBinding
    override fun getItemCount(): Int = picList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RootHolder {
        binding = RootRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RootHolder(binding)
    }

    override fun onBindViewHolder(holder: RootHolder, position: Int) {
        val pic = picList[position]
        holder.itemBinding.clickIvItem.run {
            Glide.with(this).load(pic.getUrl(720)).into(this)
            setOnClickListener {
                it.transitionName = pic.pid
                PicActivity.pics = picList
                PicActivity.nowPos = position
                PicActivity.nowDrawable = this.drawable
                val intent = Intent(context, PicActivity::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(activity, it, pic.pid)
                activity?.startActivity(intent, options.toBundle())
            }
        }
    }
}

class RootHolder(val itemBinding: RootRvItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
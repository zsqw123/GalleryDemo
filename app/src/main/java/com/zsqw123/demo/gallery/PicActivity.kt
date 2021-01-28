package com.zsqw123.demo.gallery

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zsqw123.demo.gallery.databinding.ActivityPicBinding
import com.zsqw123.demo.gallery.databinding.PicPagerItemBinding

class PicActivity : AppCompatActivity() {
    lateinit var binding: ActivityPicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.wrapper.setImageDrawable(nowDrawable)
        binding.picsPager.run {
            adapter = PicsPagerAdapter(this@PicActivity, pics)
            setCurrentItem(nowPos, false)
            transitionName = pics[nowPos].pid
        }
    }

    override fun onBackPressed() {
        finishPicView()
    }

    fun finishPicView() {
        if (binding.picsPager.currentItem == nowPos) finishAfterTransition()
        else finish()
    }

    companion object {
        var nowPos = 0
        lateinit var pics: List<Picture>
        lateinit var nowDrawable: Drawable
    }
}

class PicsPagerAdapter(private val activity: PicActivity, private val picList: List<Picture>) : RecyclerView.Adapter<PicHolder>() {
    private lateinit var binding: PicPagerItemBinding
    override fun getItemCount(): Int = picList.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicHolder {
        binding = PicPagerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PicHolder(binding)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        val pic = picList[position]
        holder.itemBinding.clickIvItem.run {
            val glideRequst = Glide.with(this).load(pic.getUrl(1080))
            if (activity.binding.wrapper.visibility != View.GONE) glideRequst.listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean = false
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    activity.binding.wrapper.visibility = View.GONE
                    return false
                }
            })
            glideRequst.into(this)
            setOnClickListener {
                activity.finishPicView()
            }
        }
    }
}

class PicHolder(val itemBinding: PicPagerItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
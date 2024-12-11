package me.ztiany.apm.scene.bitmap

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.squareup.picasso.Picasso
import me.ztiany.apm.App
import me.ztiany.apm.databinding.BitmapActivityMonitorSceneBinding
import me.ztiany.apm.utils.dp
import timber.log.Timber

class BitmapMonitorSceneActivity : AppCompatActivity() {

    private val URL_LIST: List<String> = mutableListOf(
        "https://img2.baidu.com/it/u=867579726,2670217964&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800",
        "https://img1.baidu.com/it/u=2825489197,612817393&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800",
        "https://t7.baidu.com/it/u=1732966997,2981886582&fm=193&f=GIF",
        "https://img2.baidu.com/it/u=449329914,897680117&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
        "https://img0.baidu.com/it/u=4159207327,1114356188&fm=253&fmt=auto&app=138&f=JPEG?w=600&h=361",
        "https://img0.baidu.com/it/u=2427603358,581212902&fm=253&fmt=auto&app=120&f=JPEG?w=653&h=490"
    )

    private lateinit var binding: BitmapActivityMonitorSceneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BitmapActivityMonitorSceneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
        setupImageList()
    }

    private fun setupButtons() = with(binding) {
        btnDumpStatistics.setOnClickListener {
            Timber.i("dump statistics")
            Timber.i(App.bitmapMonitor.dumpBitmapStatistics().toString())
        }
        btnDumpSimpleStatistics.setOnClickListener {
            Timber.i("dump simple statistics")
            Timber.i(App.bitmapMonitor.dumpBitmapSimpleStatistics().toString())
        }
    }

    private fun setupImageList() = with(binding.rvMonitorScene) {
        layoutManager = LinearLayoutManager(this@BitmapMonitorSceneActivity)
        addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
        adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun getItemCount() = URL_LIST.size

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return object : ViewHolder(ImageView(this@BitmapMonitorSceneActivity).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(200))
                }) {}
            }

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                Picasso.get().load(URL_LIST[position]).into((holder.itemView as ImageView))
            }
        }
    }

}
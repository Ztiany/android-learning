package me.ztiany.apm.scene.fluency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.trace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import me.ztiany.apm.R
import me.ztiany.apm.databinding.FluencyActivityListSceneBinding
import timber.log.Timber

class ListFluencySceneActivity : AppCompatActivity() {

    private val portraitList = listOf(
        R.drawable.head_portrait1,
        R.drawable.head_portrait2,
        R.drawable.head_portrait3,
        R.drawable.head_portrait4,
        R.drawable.head_portrait5,
        R.drawable.head_portrait6,
        R.drawable.head_portrait7,
        R.drawable.head_portrait8,
        R.drawable.head_portrait9,
        R.drawable.head_portrait10,
        R.drawable.head_portrait11,
        R.drawable.head_portrait12,
        R.drawable.head_portrait13,
        R.drawable.head_portrait14,
        R.drawable.head_portrait15
    )

    private lateinit var binding: FluencyActivityListSceneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FluencyActivityListSceneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() = with(binding.rvSlowList) {
        layoutManager = LinearLayoutManager(this@ListFluencySceneActivity)
        addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
        adapter = SlowListAdapter(generateData())
    }

    private fun generateData(): List<Item> {
        val list = mutableListOf<Item>()
        for (i in 0 until 100) {
            list.add(
                Item(
                    portraitList[i % portraitList.size],
                    "title $i",
                    "content $i"
                )
            )
        }
        return list
    }

    private class Item(
        val portraitRes: Int,
        val title: String,
        val content: String,
    )

    private class SlowListAdapter(private val list: List<Item>) : RecyclerView.Adapter<SlowListAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fluency_item_slow, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            trace("ListFluencySceneActivity.bind") {
                holder.bind(list[position])
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val tvTitle: TextView = itemView.findViewById(R.id.tv_item_title)
            private val tvContent: TextView = itemView.findViewById(R.id.tv_item_content)
            private val ivPortrait: ImageView = itemView.findViewById(R.id.iv_item_icon)

            fun bind(item: Item) {
                tvTitle.text = item.title
                tvContent.text = item.content
                ivPortrait.setImageResource(item.portraitRes)
                var int = 3
                repeat(10 * 10000000) {
                    int += 1
                }
                Timber.d("int = $int")
            }
        }
    }

}
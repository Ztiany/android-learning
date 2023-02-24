package com.ztiany.view.drawable

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.base.ui.drawable.*
import com.ztiany.view.R
import com.ztiany.view.SimpleLayoutFragment

class CodingDrawableFragment : SimpleLayoutFragment() {

    override fun getLayoutId(): Int = R.layout.drawable_fragment_by_coding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        useByCustomView()
        useByCoding()
    }

    private fun useByCustomView() {
        //Nothing to do.
    }

    private fun useByCoding() {
        getView(R.id.drawable_view01).background = CodeGradientDrawable.Builder(requireContext()).apply {
            solidColor(CodeColorStateList.valueOf(Color.parseColor("#FF0000")))
            shape(GradientDrawable.OVAL)
        }.build()

        getView(R.id.drawable_view02).background = CodeGradientDrawable.Builder(requireContext()).apply {
            val gradient = Gradient.Builder(requireContext()).apply {
                val colors = IntArray(2).apply {
                    this[0] = Color.parseColor("#ff0000")
                    this[1] = Color.parseColor("#00ff00")
                }
                this.gradientColors(colors)
            }

            gradient(gradient)
        }.build()


        getView(R.id.drawable_view03).background = CodeGradientDrawable.Builder(requireContext()).apply {
            val gradient = Gradient.Builder(requireContext()).apply {
                val colors = IntArray(2).apply {
                    this[0] = Color.parseColor("#ff0000")
                    this[1] = Color.parseColor("#00ff00")
                }
                this.gradientColors(colors)
            }
            val corner = Corner.Builder(requireContext()).apply {
                this.radius(20F)
            }
            gradient(gradient)
            corner(corner)
        }.build()


        getView(R.id.drawable_view04).background = CodeGradientDrawable.Builder(requireContext()).apply {
            val gradient = Gradient.Builder(requireContext()).apply {
                val colors = IntArray(3).apply {
                    this[0] = Color.parseColor("#ff0000")
                    this[1] = Color.parseColor("#0000ff")
                    this[2] = Color.parseColor("#00ff00")
                }
                this.gradientColors(colors)
            }
            val corner = Corner.Builder(requireContext()).apply {
                this.radius(20F)
            }
            gradient(gradient)
            corner(corner)
        }.build()

        val codeColor1 = CodeColorStateList.Builder().apply {
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#ff0000"))
                this.addState(StatePressed)
                this.minusState(StateChecked)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#00ff00"))
                this.minusState(StatePressed)
                this.addState(StateChecked)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#0000ff"))
            })
        }.build()

        val codeColor2 = CodeColorStateList.Builder().apply {
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#ff0000"))
                this.minusState(StateChecked)
                this.addState(StatePressed)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#00ff00"))
                this.minusState(StatePressed)
                this.addState(StateChecked)
            })
            this.addSelectorColorItem(SelectorColorItem.Builder().apply {
                this.color(Color.parseColor("#0000ff"))
            })
        }.build()

        println("codeColor1 equals codeColor2: ${codeColor1 == codeColor2}")
        (getView(R.id.drawable_tv01) as TextView).setTextColor(codeColor1)

        getView(R.id.drawable_view05).background = CodeStateListDrawable.Builder().apply {
            this.addSelectorDrawableItem(SelectorDrawableItem.Builder().apply {
                this.drawable(resources.getDrawable(R.drawable.img1))
                this.minusState(StatePressed)
            })
            this.addSelectorDrawableItem(SelectorDrawableItem.Builder().apply {
                this.drawable(resources.getDrawable(R.drawable.img2))
                this.addState(StatePressed)
            })
        }.build()
    }

    private fun getView(viewId: Int) = requireView().findViewById<View>(viewId)

}
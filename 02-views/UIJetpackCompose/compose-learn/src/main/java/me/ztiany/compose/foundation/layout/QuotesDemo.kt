package me.ztiany.compose.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout

/**
 * ConstraintLayout 的使用。
 */
@Preview
@Composable
fun QuotesDemo() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        val (quotesFirstLineRef, quotesSecondLineRef, quotesThirdLineRef, quotesForthLineRef) = remember { createRefs() }
        createVerticalChain(quotesFirstLineRef, quotesSecondLineRef, quotesThirdLineRef, quotesForthLineRef, chainStyle = ChainStyle.Spread)
        Text(text = "寄蜉蝣于天地，",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(quotesFirstLineRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        Text(text = "渺沧海之一粟。",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(quotesSecondLineRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        Text(text = "哀吾生之须臾，",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(quotesThirdLineRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        Text(text = "羡长江之无穷。",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(quotesForthLineRef) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
    }
}
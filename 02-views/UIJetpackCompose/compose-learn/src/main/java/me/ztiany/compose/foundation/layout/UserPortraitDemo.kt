package me.ztiany.compose.foundation.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import me.ztiany.compose.R

/**
 * ConstraintLayout 的使用。
 */
@Composable
fun UserPortraitDemo() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
    ) {

        val (userPortraitBackgroundRef, userPortraitImgRef, welcomeRef, quotesRef) = remember { createRefs() }
        val guideLine = createGuidelineFromTop(0.2f)

        Box(modifier = Modifier
            .constrainAs(userPortraitBackgroundRef) {
                top.linkTo(parent.top)
                bottom.linkTo(guideLine)
                height = Dimension.fillToConstraints
                width = Dimension.matchParent
            }
            .background(Color(0xFF1E9FFF))
        )

        Image(painter = painterResource(id = R.drawable.head_portrait1),
            contentDescription = "portrait",
            modifier = Modifier
                .constrainAs(userPortraitImgRef) {
                    top.linkTo(guideLine)
                    bottom.linkTo(guideLine)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(100.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color(0xFF5FB878), shape = CircleShape))

        Text(
            text = "Compose 技术爱好者",
            color = Color.White,
            fontSize = 26.sp,
            modifier = Modifier.constrainAs(welcomeRef) {
                top.linkTo(userPortraitImgRef.bottom, 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

    }
}
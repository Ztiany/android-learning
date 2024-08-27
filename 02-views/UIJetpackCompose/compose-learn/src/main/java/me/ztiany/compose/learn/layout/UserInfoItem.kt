package me.ztiany.compose.learn.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import me.ztiany.compose.R

/**
 * ConstraintLayout 的使用
 */
@Composable
fun ConstraintLayoutDemo() {
    ConstraintLayout(
        modifier = Modifier
            .width(300.dp)
            .height(100.dp)
            .padding(10.dp)
    ) {

        val (portraitImageRef, usernameTextRef, desTextRef) = remember { createRefs() }

        Image(
            painter = painterResource(id = R.drawable.head_portrait1),
            contentDescription = "portrait",
            modifier = Modifier.constrainAs(portraitImageRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        )

        Text(
            text = "Compose 技术爱好者",
            fontSize = 16.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .constrainAs(usernameTextRef) {
                    top.linkTo(portraitImageRef.top)
                    start.linkTo(portraitImageRef.end, 10.dp)
                    width = Dimension.preferredWrapContent
                }
        )

        Text(
            text = "我的个人描述...",
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Light,
            modifier = Modifier
                .constrainAs(desTextRef) {
                    top.linkTo(usernameTextRef.bottom, 5.dp)
                    start.linkTo(portraitImageRef.end, 10.dp)
                }
        )
    }

}
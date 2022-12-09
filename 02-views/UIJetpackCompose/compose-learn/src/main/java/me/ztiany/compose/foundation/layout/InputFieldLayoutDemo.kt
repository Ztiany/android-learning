package me.ztiany.compose.foundation.layout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun InputFieldLayoutDemo() {
    ConstraintLayout(
        modifier = Modifier
            .width(400.dp)
            .padding(10.dp)
    ) {
        val (usernameTextRef, passwordTextRef, usernameInputRef, passWordInputRef, dividerRef) = remember { createRefs() }
        val barrier = createEndBarrier(usernameTextRef, passwordTextRef)
        Text(text = "用户名", fontSize = 14.sp, textAlign = TextAlign.Left, modifier = Modifier.constrainAs(usernameTextRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
        Divider(
            Modifier
                .fillMaxWidth()
                .constrainAs(dividerRef) {
                    top.linkTo(usernameTextRef.bottom)
                    bottom.linkTo(passwordTextRef.top)
                })
        Text(text = "密码", fontSize = 14.sp, modifier = Modifier.constrainAs(passwordTextRef) {
                top.linkTo(usernameTextRef.bottom, 19.dp)
                start.linkTo(parent.start)
            })
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.constrainAs(usernameInputRef) {
            start.linkTo(barrier, 10.dp)
            top.linkTo(usernameTextRef.top)
            bottom.linkTo(usernameTextRef.bottom)
            height = Dimension.fillToConstraints
        })
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier.constrainAs(passWordInputRef) {
            start.linkTo(barrier, 10.dp)
            top.linkTo(passwordTextRef.top)
            bottom.linkTo(passwordTextRef.bottom)
            height = Dimension.fillToConstraints
        })
    }
}

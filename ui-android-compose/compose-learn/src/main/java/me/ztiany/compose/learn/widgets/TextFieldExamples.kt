package me.ztiany.compose.learn.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import me.ztiany.compose.R

/**
 * TextFiled is a component that allows users to enter text.
 *
 * For more information,  refers to:
 *
 * 1. https://jetpackcompose.cn/docs/elements/textfield
 * 2. https://developer.android.com/develop/ui/compose/text
 */
@Composable
fun TextFiledExample() {
    BasicTextField()
    Spacer(modifier = Modifier.size(5.dp))
    SimpleTextField()
    Spacer(modifier = Modifier.size(5.dp))
    TextFieldWithLabel()
    Spacer(modifier = Modifier.size(5.dp))
    OutlinedInputFieldLayout()
    Spacer(modifier = Modifier.size(5.dp))
    PasswordTextField()
    Spacer(modifier = Modifier.size(5.dp))
    CustomBackgroundTextField()
    Spacer(modifier = Modifier.size(5.dp))
    SearchBar()
}

/**
 * 使用 BasicTextField 创建一个简单的文本输入框。
 */
@Composable
private fun BasicTextField() {
    Column(Modifier.border(1.dp, Color.Gray)) {
        var textState by remember { mutableStateOf(TextFieldValue("I am a Text Widget.")) }
        BasicTextField(value = textState, onValueChange = {
            textState = it
        })
        Text("The text-field has this text: " + textState.text)
    }
}

@Composable
private fun SimpleTextField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true
    )
}

@Composable
private fun CustomBackgroundTextField() {
    var text by remember { mutableStateOf("") }

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
        },
        decorationBox = { innerTextField ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                Box(modifier = Modifier.weight(1F), contentAlignment = Alignment.CenterStart) {
                    if (text.isEmpty()) {
                        Text(
                            text = "请输入用户名！",
                            style = TextStyle(
                                color = Color(0, 0, 0, 128)
                            )
                        )
                    }
                    innerTextField()
                }
            }
        },
        modifier = Modifier
            .background(Color(0xFFD3D3D3), RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .padding(2.dp)
    )
}

/**
 * 使用 BasicTextField 自由定制 TextFiled。
 */
@Composable
private fun SearchBar() {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD3D3D3))
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {

        BasicTextField(value = text, onValueChange = {
            text = it
        }, decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1F), contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "输入点东西看看吧~",
                            style = TextStyle(
                                color = Color(0, 0, 0, 128)
                            )
                        )
                    }
                    innerTextField()
                }
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = { text = "" },
                        Modifier
                            .padding(horizontal = 5.dp)
                            .size(16.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            }
        }, modifier = Modifier
            .padding(horizontal = 10.dp)
            .background(Color.White, CircleShape)
            .fillMaxWidth()
        )
    }
}

@Composable
private fun PasswordTextField() {
    var text by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(false) }

    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                Icon(painterResource(id = if (passwordHidden) R.drawable.icon_eye_on else R.drawable.icon_eye_off), null)
            }
        },
        label = {
            Text("密码")
        },
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None
    )
}

@Composable
private fun OutlinedInputFieldLayout() {
    ConstraintLayout(
        modifier = Modifier
            .width(400.dp)
            .padding(10.dp)
    ) {
        val (usernameTextRef, passwordTextRef, usernameInputRef, passWordInputRef, dividerRef) = remember { createRefs() }

        val barrier = createEndBarrier(usernameTextRef, passwordTextRef)

        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Text(text = "用户名", fontSize = 16.sp, textAlign = TextAlign.Left, modifier = Modifier
            .padding(vertical = 15.dp)
            .constrainAs(usernameTextRef) {
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

        Text(text = "密码", fontSize = 16.sp, modifier = Modifier
            .padding(vertical = 15.dp)
            .constrainAs(passwordTextRef) {
                top.linkTo(usernameTextRef.bottom, 19.dp)
                start.linkTo(parent.start)
            })

        OutlinedTextField(value = username, onValueChange = { username = it }, modifier = Modifier.constrainAs(usernameInputRef) {
            start.linkTo(barrier, 10.dp)
            top.linkTo(usernameTextRef.top)
            bottom.linkTo(usernameTextRef.bottom)
            height = Dimension.fillToConstraints
        })

        OutlinedTextField(value = password, onValueChange = { password = it }, modifier = Modifier.constrainAs(passWordInputRef) {
            start.linkTo(barrier, 10.dp)
            top.linkTo(passwordTextRef.top)
            bottom.linkTo(passwordTextRef.bottom)
            height = Dimension.fillToConstraints
        })
    }
}

@Composable
private fun TextFieldWithLabel() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        label = {
            Text("邮箱")
        }
    )
}
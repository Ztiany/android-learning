package me.ztiany.compose.foundation.widgets

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ztiany.compose.R

//https://jetpackcompose.cn/docs/elements/text
@Composable
fun TextExample(context: Context) {
    BasicTextFieldDemo()

    Text(text = "我是一个普通的文本")

    Text(
        text = "我是一个大的文本",
        style = TextStyle(color = Color.Blue, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
    )

    Text(text = "使用资源文件钟的字符串：${stringResource(id = R.string.app_name)}")

    ClickableText(text = AnnotatedString("I am Clickable"), onClick = {
        Toast.makeText(context, "This a simple toast tutorial!", Toast.LENGTH_LONG).show()
    })

    Text(text = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Gray)) { append("Don't ") }
        withStyle(style = SpanStyle(color = Color.Red)) { append("have ") }
        withStyle(style = SpanStyle(color = Color.Blue)) { append("an ") }
        withStyle(style = SpanStyle(color = Color.Green, fontSize = 30.sp)) { append("account ") }
        withStyle(style = SpanStyle(color = Color.Black)) { append("?. ") }
    })

    AnnotatedClickableText()

    SelectableText()

    ExpandableText(text = stringResource(id = R.string.text_layout_result_explanation), minimizedMaxLines = 2)
    Spacer(modifier = Modifier.height(10.dp))
    ExpandableText(text = stringResource(id = R.string.launched_effect_explanation), minimizedMaxLines = 3)
    Spacer(modifier = Modifier.height(10.dp))

    CustomMaterialText()
}

@Composable
private fun CustomMaterialText() {
    // 将内部 Text 组件的 alpha 强调程度设置为高
    // 注意: MaterialTheme 已经默认将强调程度设置为 high
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
        Text("这里是 high 强调效果")
    }
    // 将内部 Text 组件的 alpha 强调程度设置为中
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text("这里是 medium 强调效果")
    }
    // 将内部 Text 组件的 alpha 强调程度设置为禁用
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
        Text("这里是禁用后的效果")
    }
}

//https://proandroiddev.com/expandabletext-in-jetpack-compose-b924ea424774
@Composable
private fun ExpandableText(modifier: Modifier = Modifier, text: String, minimizedMaxLines: Int = 2) {

    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var finalText by remember { mutableStateOf(text) }

    val textLayoutResult = textLayoutResultState.value

    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                finalText = "$text Show Less"
            }
            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(minimizedMaxLines - 1)
                val showMoreString = "... Show More"
                val adjustedText = text
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                    .dropLastWhile { it == ' ' || it == '.' }//去掉所有的点和空字符

                finalText = "$adjustedText$showMoreString"
                isClickable = true
            }
        }
    }

    Text(
        text = finalText,
        maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
        //A TextLayoutResult object that callback provides contains paragraph information, size of the text, baselines and other details.
        onTextLayout = { textLayoutResultState.value = it },
        modifier = modifier
            .clickable(enabled = isClickable) { isExpanded = !isExpanded }
            .animateContentSize(),
    )
}

@Composable
private fun SelectableText() {
    SelectionContainer {
        Column {
            Text(
                text = "每天摸鱼",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Left
            )
            Text(
                text = "这好吗",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "这非常地好",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right
            )
        }
    }
}

//https://stackoverflow.com/questions/67243761/partially-color-text-and-make-it-clickable-in-jetpack-compose
@Composable
private fun AnnotatedClickableText() {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        //append your initial text
        withStyle(style = SpanStyle(color = Color.Gray)) { append("Don't have an account? ") }
        //Start of the pushing annotation which you want to color and make them clickable later
        pushStringAnnotation(
            tag = "SignUp",// provide tag which will then be provided when you click the text
            annotation = "SignUp"
        )
        //add text with your different color/style
        withStyle(style = SpanStyle(color = Color.Red)) { append("Sign Up") }
        // when pop is called it means the end of annotation with current tag
        pop()
    }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(
                tag = "SignUp",// tag which you used in the buildAnnotatedString
                start = offset,
                end = offset
            )[0].let { annotation ->
                Toast.makeText(context, annotation.item, Toast.LENGTH_LONG).show()
            }
        }
    )
}

@Composable
private fun BasicTextFieldDemo() {
    var textState by remember { mutableStateOf(TextFieldValue("I am a Text Widget.")) }
    BasicTextField(value = textState, onValueChange = {
        textState = it
    })
    Text("The text-field has this text: " + textState.text)
}
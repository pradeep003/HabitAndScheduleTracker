package com.ftcoding.habitandscheduletracker.presentation.components

import android.graphics.Color.HSVToColor
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.ftcoding.habitandscheduletracker.presentation.ui.theme.Color3
import kotlin.math.roundToInt

@Composable
fun ColorPicker (
    newSelectedColor: (String)-> Unit
) {

    val density = LocalDensity.current
    val paddingHorizontal = 30.dp

    // color hue
    val colorMapWidth = LocalConfiguration.current.screenWidthDp.dp - paddingHorizontal - paddingHorizontal
    val colorMapSelectionRadius = 25.dp

    val colorMapOffsetPx = remember {
        mutableStateOf(
            with(density) {
                // default position of the selector
                -colorMapSelectionRadius.toPx()
            }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        color = Color3
    ) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
            ) {

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            // color map and selector of part1
            ColorMapSelector(
                density = density,
                paddingHorizontal = paddingHorizontal,
                colorMapWidth = colorMapWidth,
                colorMapSelectorRadius = colorMapSelectionRadius,
                colorMapOffsetPx = colorMapOffsetPx.value,
                onColorMapOffsetPx = {
                                     colorMapOffsetPx.value = it
                },
                saturation = 1f,
                lightness = 1f
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            // selected color result

            Result(
                color = getSelectedColor(
                    colorMapOffset = calculateCorrectOffSet(
                        selectorOffset = colorMapOffsetPx.value,
                        selectorRadius = with(density) {
                            colorMapSelectionRadius.toPx()
                        }
                    ),
                    colorMapWidth = with(density) {
                        colorMapWidth.toPx()
                    },
                    saturation = 1f,
                    lightness = 1f
                )
            ) { selectedColor->
                newSelectedColor(selectedColor)
            }

        }
    }
}

@Composable
private fun Result (
    color: Color,
    selectedColor: (String) -> Unit
) {
    
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(60.dp)
                .aspectRatio(1f),
            shadowElevation = 5.dp,
            color = color,
            shape = RoundedCornerShape(8.dp)
        ) {
            
        }
        
        Spacer(modifier = Modifier.padding(vertical = 6.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            
            Text(
                text = "R: ${color.red * 255}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.padding(horizontal = 6.dp))

            Text(
                text = "G: ${color.green * 255}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color =  MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.padding(horizontal = 6.dp))

            Text(
                text = "B: ${color.blue * 255}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color =  MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.padding(horizontal = 6.dp))
        }

        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        
        Text(
            text = color.toHexCode(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color =  MaterialTheme.colorScheme.onBackground
        )
        // callback color.toHexCode()
        selectedColor(color.toHexCode())
    }
    
}

fun Color.toHexCode() : String {

    val red = this.red * 225
    val green = this.green * 225
    val blue = this.blue * 225

    return String.format("#%02x%02x%02x", red.toInt(), green.toInt(), blue.toInt())
}

@Composable
private fun ColorMapSelector (
    density: Density,
    paddingHorizontal: Dp,
    colorMapWidth: Dp,
    colorMapSelectorRadius: Dp,
    colorMapOffsetPx: Float,
    onColorMapOffsetPx: (colorMapOffsetPx: Float) -> Unit,
    saturation: Float,
    lightness: Float
) {
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontal)
            .height(colorMapSelectorRadius * 2)) 
    {
        
        // color map
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .height(40.dp)
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .background(
                    createColorMap(
                        with(density) {
                            colorMapWidth.toPx()
                        }
                    )
                )
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(50))
                .pointerInput(Unit) {
                    // touch color map and move the selector
                    detectTapGestures { offset ->

                        val offsetPx = offset.x - with(density) {
                            // the tapped position need to minus the selector's radius as the correct position for the selector
                            colorMapSelectorRadius.toPx()
                        }
                        onColorMapOffsetPx(offsetPx)
                    }
                }
        )

        // color selector
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(colorMapOffsetPx.roundToInt(), 0)
                }
                .size(colorMapSelectorRadius * 2)
                .clip(CircleShape)
                .background(
                    getSelectedColor(
                        // the start position is minus dp
                        // so we need plus the radius dp again to get the correct px for getting color
                        colorMapOffset = calculateCorrectOffSet(
                            selectorOffset = colorMapOffsetPx,
                            selectorRadius = with(density) {
                                colorMapSelectorRadius.toPx()
                            }
                        ),
                        colorMapWidth = with(density) {
                            colorMapWidth.toPx()
                        },
                        saturation = saturation,
                        lightness = lightness
                    )
                )
                .border(width = 2.dp, color = Color.Black, shape = CircleShape)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->

                        val offsetPx = (colorMapOffsetPx + delta).coerceIn(
                            // keep the drage in the range that the minimum is zero minus selector radius
                            // and the maximum is the color map's width minus selector's radius
                            with(density) {
                                -colorMapSelectorRadius.toPx()
                            },
                            with(density) {
                                (colorMapWidth - colorMapSelectorRadius).toPx()
                            }
                        )
                        onColorMapOffsetPx(offsetPx)
                    }
                )
        )
    }
}

private fun calculateCorrectOffSet(selectorOffset: Float, selectorRadius: Float): Float {
    return selectorOffset + selectorRadius
}

private fun getSelectedColor(
    colorMapOffset: Float ,
    colorMapWidth: Float,
    saturation: Float,
    lightness: Float
    ): Color {

    val hue = (colorMapOffset / colorMapWidth) * 360f
    return Color(
        HSVToColor(
            floatArrayOf(
                hue,
                saturation,
                lightness
            )
        )
    )
}

private fun createColorMap(colorMapWidth: Float) : Brush {

    val colors = mutableListOf<Color>()

    for (i in 0..360) {

        val saturation = 1f
        val lightness = 1f

        val hsv = HSVToColor(
            floatArrayOf(
                i.toFloat(),
                saturation,
                lightness
            )
        )
        colors.add(Color(hsv))
    }

    return Brush.horizontalGradient(
        colors = colors,
        startX = 0f,
        endX = colorMapWidth
    )
}

@Preview(showBackground = true)
@Composable
fun ColorPickerPreview() {
    ColorPicker() {}

}
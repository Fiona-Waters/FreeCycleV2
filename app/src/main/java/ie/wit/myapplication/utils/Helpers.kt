package ie.wit.myapplication.utils


import android.graphics.Color
import com.makeramen.roundedimageview.RoundedTransformationBuilder

fun customTransformation(): com.squareup.picasso.Transformation =
    RoundedTransformationBuilder()
        .borderColor(Color.WHITE)
        .borderWidthDp(2F)
        .cornerRadiusDp(35F)
        .oval(false)
        .build()

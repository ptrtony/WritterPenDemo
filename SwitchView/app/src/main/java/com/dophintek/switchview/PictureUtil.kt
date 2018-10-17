package com.dophintek.switchview

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.view.WindowManager
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType

class PictureUtil{
    companion object {
        fun choosefPhoto(context:Activity,requestCode:Int){
            PictureSelector.create(context)
                    .openGallery(PictureMimeType.ofAll())
                    .maxSelectNum(1)
                    .imageSpanCount(4)
                    .selectionMode(PictureConfig.SINGLE)
                    .isCamera(true)
                    .imageFormat(PictureMimeType.PNG)
                    .isZoomAnim(true)
                    .sizeMultiplier(0.5f)
                    .openClickSound(true)
                    .minimumCompressSize(500)
                    .forResult(requestCode)
        }

        fun createAsciiPic(path: String, context: Context): Bitmap {
            val base = "#8XOHLTI)i=+;:,."// 字符串由复杂到简单
            val text = StringBuilder()
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels
            val height = dm.heightPixels
            var image = BitmapFactory.decodeFile(path)  //读取图片
            val width0 = image.width
            val height0 = image.height
            val width1: Int
            val height1: Int
            val scale = 7
            if (width0 <= width / scale) {
                width1 = width0
                height1 = height0
            } else {
                width1 = width / scale
                height1 = width1 * height0 / width0
            }
            image = scale(path, width1, height1)  //读取图片
            //输出到指定文件中
            var y = 0
            while (y < image.height) {
                for (x in 0 until image.width) {
                    val pixel = image.getPixel(x, y)
                    val r = pixel and 0xff0000 shr 16
                    val g = pixel and 0xff00 shr 8
                    val b = pixel and 0xff
                    val gray = 0.299f * r + 0.578f * g + 0.114f * b
                    val index = Math.round(gray * (base.length + 1) / 255)
                    val s = if (index >= base.length) " " else base[index].toString()
                    text.append(s)
                }
                text.append("\n")
                y += 2
            }
            return textAsBitmap(text, context)
        }

        fun textAsBitmap(text:StringBuilder,context:Context):Bitmap{
            val textPaint = TextPaint()
            textPaint.color = Color.BLACK
            textPaint.isAntiAlias = true
            textPaint.typeface = Typeface.MONOSPACE
            textPaint.textSize = 12f
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            wm.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels
            val layout = StaticLayout(text,textPaint,width,
                    Layout.Alignment.ALIGN_CENTER,1f,0.0f,true)
            val bitmap = Bitmap.createBitmap(layout.width+20,
                    layout.height+20,Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.translate(10f,10f)
            canvas.drawColor(Color.WHITE)
            layout.draw(canvas)
            return bitmap
        }

    }
}
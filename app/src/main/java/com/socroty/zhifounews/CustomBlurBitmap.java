package com.socroty.zhifounews;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class CustomBlurBitmap {
    Bitmap blurBitmap(Bitmap bmp, Context context) {
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation in = Allocation.createFromBitmap(rs, bmp);
        Allocation out = Allocation.createFromBitmap(rs, result);
        blur.setRadius(25f);
        blur.setInput(in);
        blur.forEach(out);
        out.copyTo(result);
        rs.destroy();
        return result;
    }
}

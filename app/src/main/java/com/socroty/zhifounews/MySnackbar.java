package com.socroty.zhifounews;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;

public class MySnackbar {
    //工具类当前持有的Snackbar实例
    private static WeakReference<Snackbar> snackbarWeakReference;

    private MySnackbar(@Nullable WeakReference<Snackbar> snackbarWeakReference){
        MySnackbar.snackbarWeakReference = snackbarWeakReference;
    }

    public Snackbar getSnackbar() {
        if(snackbarWeakReference != null && snackbarWeakReference.get()!=null){
            return snackbarWeakReference.get();
        }else {
            return null;
        }
    }

    public static MySnackbar Custom(View view, String message, int duration){
        return new MySnackbar(new WeakReference<Snackbar>(Snackbar.make(view,message,Snackbar.LENGTH_SHORT).setDuration(duration))).backgroundColor(0XFF323232);
    }

    public MySnackbar backgroundColor(@ColorInt int backgroundColor){
        if(getSnackbar()!=null){
            getSnackbar().getView().setBackgroundColor(backgroundColor);
        }
        return this;
    }

    public MySnackbar messageColor(@ColorInt int messageColor){
        if(getSnackbar()!=null){
            ((TextView)getSnackbar().getView().findViewById(R.id.snackbar_text)).setTextColor(messageColor);
        }
        return this;
    }

    public MySnackbar actionColor(@ColorInt int actionTextColor){
        if(getSnackbar()!=null){
            ((Button)getSnackbar().getView().findViewById(R.id.snackbar_action)).setTextColor(actionTextColor);
        }
        return this;
    }

    public MySnackbar setAction(@StringRes int resId, View.OnClickListener listener){
        if(getSnackbar()!=null){
            return setAction(getSnackbar().getView().getResources().getText(resId), listener);
        }else {
            return this;
        }
    }

    public MySnackbar setAction(CharSequence text, View.OnClickListener listener){
        if(getSnackbar()!=null){
            getSnackbar().setAction(text,listener);
        }
        return this;
    }

    public MySnackbar setCallback(Snackbar.Callback setCallback){
        if(getSnackbar()!=null){
            getSnackbar().setCallback(setCallback);
        }
        return this;
    }

    public MySnackbar margins(int left, int top, int right, int bottom){
        if(getSnackbar()!=null){
            ViewGroup.LayoutParams params = getSnackbar().getView().getLayoutParams();
            ((ViewGroup.MarginLayoutParams) params).setMargins(left,top,right,bottom);
            getSnackbar().getView().setLayoutParams(params);
        }
        return this;
    }

    private GradientDrawable getRadiusDrawable(Drawable backgroundOri){
        GradientDrawable background = null;
        if(backgroundOri instanceof GradientDrawable){
            background = (GradientDrawable) backgroundOri;
        }else if(backgroundOri instanceof ColorDrawable){
            int backgroundColor = ((ColorDrawable)backgroundOri).getColor();
            background = new GradientDrawable();
            background.setColor(backgroundColor);
        }
        return background;
    }

    public MySnackbar radius(float radius){
        if(getSnackbar()!=null){
            //将要设置给mSnackbar的背景
            GradientDrawable background = getRadiusDrawable(getSnackbar().getView().getBackground());
            if(background != null){
                radius = radius<=0?12:radius;
                background.setCornerRadius(radius);
                getSnackbar().getView().setBackgroundDrawable(background);
            }
        }
        return this;
    }

    public void show(){
        Log.e("Jet","show()");
        if(getSnackbar()!=null){
            Log.e("Jet","show");
            getSnackbar().show();
        }else {
            Log.e("Jet","已经被回收");
        }
    }
}

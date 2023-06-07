package com.neverwin.uzeed.uzeed.Views;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.neverwin.uzeed.uzeed.R;

public class GradientTextView extends android.support.v7.widget.AppCompatTextView
{
    public GradientTextView( Context context )
    {
        super( context, null, -1 );
    }
    public GradientTextView( Context context, AttributeSet attrs )
    {
        super( context, attrs, -1 );
    }
    public GradientTextView( Context context, AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void onLayout( boolean changed, int left, int top, int right, int bottom )
    {
        super.onLayout( changed, left, top, right, bottom );
        if(changed)
        {
            getPaint().setShader( new LinearGradient( 0, 0, 0, getHeight(), ContextCompat.getColor(getContext(), R.color.colorPrimaryDark), ContextCompat.getColor(getContext(), R.color.colorPrimary), Shader.TileMode.CLAMP ) );
        }
    }
}

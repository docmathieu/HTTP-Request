package com.docm.httprequest.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.docm.httprequest.R;

/**
 * Created by Romain Mathieu on 14/12/2015.
 *
 * Fragment used to show image
 */
public class FragmentImage extends Fragment
{
    static ImageView resultImage;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment1.
     */
    public static FragmentImage newInstance()
    {
        return new FragmentImage();
    }

    public FragmentImage()
    {
        // Required empty public constructor
    }

    public static void setImage(Bitmap bmp)
    {
        if (resultImage != null) resultImage.setImageBitmap(bmp);
    }

    public static void clear()
    {
        if (resultImage != null) resultImage.setImageResource(android.R.color.transparent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_image, container, false);
        resultImage = (ImageView) layout.findViewById(R.id.resultImage);
        return layout;
    }
}
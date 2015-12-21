package com.docm.httprequest.controller;

import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;

import com.docm.httprequest.fragment.FragmentImage;
import com.docm.httprequest.fragment.FragmentText;

/**
 * Created by Romain Mathieu on 11/12/2015.
 *
 * Output controller trace on resultText
 */
public class OutputController
{
    TabLayout tabLayout;

    /**
     * Constructor.
     */
    public OutputController(TabLayout tabLayout)
    {
        this.tabLayout = tabLayout;
    }

    /**
     * Swap
     *
     * @param nb 0:text 1:image
     */
    public void swap(int nb)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(nb);
        if (tab != null) tab.select();
    }

    /**
     * Trace in resultText
     *
     *  @param message String
     */
    public void trace(String message)
    {
        FragmentText.setText(message);
        swap(0);
    }

    /**
     * Show image
     *
     *  @param bmp Bitmap
     */
    public void show(Bitmap bmp)
    {
        FragmentImage.setImage(bmp);
        swap(1);
    }

    /**
     * Append message in resultText
     *
     *  @param message String
     */
    public void append(String message)
    {
        FragmentText.append(message);
    }

    /**
     * Clear resultText
     */
    public void clear()
    {
        FragmentText.clear();
        FragmentImage.clear();
    }
}

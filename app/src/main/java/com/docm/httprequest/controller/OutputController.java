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
    static TabLayout tabLayout;

    private String eol = System.getProperty("line.separator");

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
    public static void swap(int nb)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(nb);
        if (tab != null) tab.select();
    }

    /**
     * Trace in resultText
     * Hack: Fragment does not show the message if it is on a single line, so we add eol
     *
     * @param message String
     */
    public void trace(String message)
    {
        FragmentText.setText(message + eol);
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

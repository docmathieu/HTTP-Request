package com.docm.httprequest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.docm.httprequest.R;

/**
 * Created by Romain Mathieu on 14/12/2015.
 *
 * Fragment used to show text
 */
public class FragmentText extends Fragment
{
    static TextView resultText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment1.
     */
    public static FragmentText newInstance()
    {
        return new FragmentText();
    }

    public FragmentText()
    {
        // Required empty public constructor
    }

    public static void setText(String message)
    {
        if (resultText != null) resultText.setText(message);
    }

    public static void append(String message)
    {
        if (resultText != null) resultText.append(message);
    }

    public static void clear()
    {
        if (resultText != null) resultText.setText("");
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
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_text, container, false);
        resultText = (TextView) layout.findViewById(R.id.resultText);
        return layout;
    }
}
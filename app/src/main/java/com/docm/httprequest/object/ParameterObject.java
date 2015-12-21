package com.docm.httprequest.object;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.docm.httprequest.R;

/**
 * Created by Romain Mathieu on 28/10/2015.
 *
 * UI Object for new parameter on main page
 */
public class ParameterObject extends LinearLayout {

    public static String DEFAULT_PARAM = "param";
    public static String DEFAULT_VALUE = "value";

    private String paramText = "";
    private String valueText = "";

    private TextView paramView;
    private TextView valueView;

    private Button removeButton;

    public String getParamText() {
        paramText = paramView.getText().toString();
        return paramText;
    }

    public void setParamText(String paramText) {
        this.paramText = paramText;
        paramView.setText(this.paramText);
    }

    public String getValueText() {
        valueText = valueView.getText().toString();
        return valueText;
    }

    public void setValueText(String valueText) {
        this.valueText = valueText;
        valueView.setText(this.valueText);
    }

    /**
     * Constructor.
     *
     * @param context (required)
     */
    public ParameterObject(Context context)
    {
        this(context, null);
    }

    /**
     * Constructor.
     *
     * @param context (required)
     * @param attrs (required)      To get params values
     */
    public ParameterObject(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews(context, attrs);
    }

    /**
     * Create parameter UI Object, set params and inflate context
     *
     * @param context (required)
     * @param attrs (required)         To get params values
     */
    private void initViews(Context context, AttributeSet attrs)
    {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ParameterObject,
                0,
                0
        );

        try {
            paramText = typeArray.getString(R.styleable.ParameterObject_param);
            valueText = typeArray.getString(R.styleable.ParameterObject_value);
        } finally {
            typeArray.recycle();
        }

        if (null == paramText) paramText = DEFAULT_PARAM;
        if (null == valueText) valueText = DEFAULT_VALUE;

        LayoutInflater.from(context).inflate(R.layout.parameter_object, this);

        paramView = (TextView) this.findViewById(R.id.param);
        paramView.setHint(paramText);

        valueView = (TextView) this.findViewById(R.id.value);
        valueView.setHint(valueText);

        initListeners();
    }

    /**
     * Init listeners
     */
    private void initListeners()
    {
        removeButton = (Button) findViewById(R.id.removeParamButton);
        removeButton.setOnClickListener(removeClick);
    }

    /**
     * On click, remove this object
     */
    private View.OnClickListener removeClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            removeButton.setOnClickListener(null);

            LinearLayout toRemove = (LinearLayout) view.getParent().getParent();
            LinearLayout container = (LinearLayout) toRemove.getParent();
            container.removeView(toRemove);
        }
    };
}

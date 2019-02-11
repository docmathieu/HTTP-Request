package com.docm.httprequest.object;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.docm.httprequest.MainActivity;
import com.docm.httprequest.R;
import com.docm.httprequest.imodel.CallBack;
import com.docm.httprequest.model.Request;

/**
 * Created by Romain Mathieu on 28/10/2015.
 *
 * UI Object for link on left panel
 */
public class SaveObject extends LinearLayout
{
    private SaveObject self;
    private Context context;

    private Button removeButton;
    private TextView nameText;
    private TextView descriptionText;

    private Request request;

    private CallBack listener;

    /**
     * Listener Setter
     * Used for callBack
     *
     * @param listener (required) Target Method to call at the end of request
     */
    public void setListener(CallBack listener)
    {
        this.listener = listener;
    }


    /**
     * Get Request
     *
     *  @return Request
     */
    public Request getRequest()
    {
        return request;
    }

    /**
     * Push Request
     */
    public void setRequest(Request request)
    {
        this.request = request;
        nameText.setText(this.request.getRequestName());

        String desc = request.getVerb() + ", " + request.getParams().size() + "p";
        if (!"".equals(request.getReferer())) desc += ", ref";
        if (!"".equals(request.getMimeType())) desc += ", mime";
        desc = this.request.getUrl() + " (" + desc + ")";
        descriptionText.setText(desc);
    }

    /**
     * Constructor.
     *
     * @param context (required)
     */
    public SaveObject(Context context)
    {
        this(context, null);
        /*
        super(context);
        LayoutInflater.from(context).inflate(R.layout.save_object, this);
        */
    }

    /**
     * Constructor.
     *
     * @param context (required)
     * @param attrs (required)      To get params values
     */
    public SaveObject(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initViews(context, attrs);
    }

    /**
     * Create link UI Object, set params and inflate context
     *
     * @param context (required)
     * @param attrs (required)         To get params values
     */
    private void initViews(Context context, AttributeSet attrs)
    {
        this.self = this;
        this.context = context;
        String nameText = "";

        if (attrs != null){
            TypedArray typeArray = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.SaveObject,
                    0,
                    0
            );
            try {
                nameText = typeArray.getString(R.styleable.SaveObject_url);
            } finally {
                typeArray.recycle();
            }
        }

        LayoutInflater.from(context).inflate(R.layout.save_object, this);
        TextView textView = (TextView) this.findViewById(R.id.nameText);
        textView.setText(nameText);

        initListeners();
    }

    /**
     * Init listeners
     */
    private void initListeners()
    {
        removeButton = (Button) findViewById(R.id.removeParamButton);
        removeButton.setOnClickListener(removeClick);

        nameText = (TextView) findViewById(R.id.nameText);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        //nameText.setOnClickListener(linkClick);

        LinearLayout clickZone = (LinearLayout) findViewById(R.id.clickZone);
        clickZone.setOnClickListener(linkClick);
    }

    /**
     * On click, remove this object
     * Because of AlertDialog.Builder themes are not available before HONEYCOMB version, we use getButtons to skin
     */
    private View.OnClickListener removeClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {

            context.setTheme(R.style.AppTheme);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            //AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setTitle("Warning");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", dialogRemoveClickListener);
            builder.setNegativeButton("No", dialogRemoveClickListener);
            AlertDialog dialog = builder.create();
            dialog.show();

            Button no = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            Button yes = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

            TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 4);

            if (no != null) {
                no.setTextColor(ContextCompat.getColor(context, R.color.greyLight));
                no.setBackgroundColor(ContextCompat.getColor(context, R.color.EditTextMainColor2));
                no.setLayoutParams(params);
            }

            if(yes != null){
                yes.setTextColor(ContextCompat.getColor(context, R.color.greyLight));
                yes.setBackgroundColor(ContextCompat.getColor(context, R.color.EditTextMainColor2));
                yes.setLayoutParams(params);
            }
        }
    };

    /**
     * On click, fill main page with data
     */
    private View.OnClickListener linkClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            listener.selectRequestCallBack(request);
        }
    };

    /**
     * Dialog buttons click listener
     */
    DialogInterface.OnClickListener dialogRemoveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    removeButton.setOnClickListener(null);
                    LinearLayout container = (LinearLayout) self.getParent();
                    container.removeView(self);
                    // Save the change !
                    MainActivity.saveAllRequests();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}

package com.docm.httprequest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.docm.httprequest.controller.FileController;
import com.docm.httprequest.controller.HttpController;
import com.docm.httprequest.controller.OutputController;
import com.docm.httprequest.imodel.CallBack;
import com.docm.httprequest.model.MyPagerAdapter;
import com.docm.httprequest.model.Parameter;
import com.docm.httprequest.model.Request;
import com.docm.httprequest.model.Verb;
import com.docm.httprequest.object.ParameterObject;
import com.docm.httprequest.object.SaveObject;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Romain Mathieu.
 *
 * Main class
 */
public class MainActivity extends AppCompatActivity implements CallBack
{
    public static Activity mainActivity;
    public static CallBack self;
    private OutputController outputController;
    private LinearLayout parametersLayout;
    private TabLayout tabLayout;
    private EditText requestName;
    private EditText url;
    private Spinner verbSelect;
    private EditText mimeType;
    private EditText refererEditText;
    private EditText loginEditText;
    private EditText passwordEditText;
    private static LinearLayout linkList;
    private LinearLayout moreBlock;
    private Button moreButton;
    private Context linkContext;

    // VavigationView
    private NavigationView navigationView;
    private View headerLayout;
    private TextView versionTextView;


    /**
     * Activity creation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mainActivity = this;
        self = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Left bar
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initTabs();
        initDropDown();
        initControllers();
        initListeners();
        initUIComponents();
        setVersion();
    }

    /**
     * Back button pressed
     */

    private long timeBackPressed;

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (timeBackPressed + 2000 < System.currentTimeMillis()){
            Toast.makeText(getBaseContext(), getString(R.string.back_button_to_exit), Toast.LENGTH_SHORT).show();
            timeBackPressed = System.currentTimeMillis();
            return;
        }

        super.onBackPressed();
    }

    /**
     *  dropDown list
     */
    protected void initDropDown()
    {
        Spinner spinner = (Spinner) findViewById(R.id.verbSelect);
        ArrayAdapter<Verb> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Verb.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Verb verb = (Verb) ((Spinner) findViewById(R.id.verbSelect)).getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Auto-generated method stub
            }
        });
    }

    /**
     *  Set version text in left bar
     */
    protected void setVersion()
    {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        String appName = getString(R.string.app_name);
        String versionText = appName + " v" + pInfo.versionName;
        versionTextView.setText(versionText);
    }

    /**
     *  Init tabs
     */
    protected void initTabs()
    {
        // Setup the viewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Setup the Tabs
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        // By using this method the tabs will be populated according to viewPager's count and
        // with the name from the pagerAdapter getPageTitle()

        // Deprecated, use setupWithViewPager only
        //tabLayout.setTabsFromPagerAdapter(pagerAdapter);

        // This method ensures that tab selection events update the ViewPager and page changes update the selected tab.
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     *  Init controllers
     */
    protected void initControllers()
    {
        outputController = new OutputController(tabLayout);

        // First saved requests load
        FileController fileController = new FileController(mainActivity);
        fileController.setListener(self);
        fileController.setMethod(FileController.METHOD_READ);
        fileController.execute();
    }

    /**
     *  Init listeners
     */
    protected void initListeners()
    {
        // Param Button
        Button addParamButton = (Button) findViewById(R.id.addParamButton);
        addParamButton.setOnClickListener(addParamClick);
        // Process Button
        Button processButton = (Button) findViewById(R.id.processButton);
        processButton.setOnClickListener(processClick);
        // More / Less Button
        Button moreButton = (Button) findViewById(R.id.moreButton);
        moreButton.setOnClickListener(moreClick);
        // Save Button
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveClick);
    }

    /**
     *  Init UI components
     */
    protected void initUIComponents()
    {
        parametersLayout = (LinearLayout) findViewById(R.id.parametersLayout);
        requestName = (EditText) findViewById(R.id.requestNameEditText);
        url = (EditText) findViewById(R.id.urlEditText);
        verbSelect = (Spinner) findViewById(R.id.verbSelect);
        //jsonCheckBox = (CheckBox) findViewById(R.id.jsonCheckBox);
        mimeType = (EditText) findViewById(R.id.mimeTypeEditText);
        refererEditText = (EditText) findViewById(R.id.refererEditText);
        loginEditText = (EditText) findViewById(R.id.loginEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        moreBlock = (LinearLayout) findViewById(R.id.moreBlock);
        moreButton = (Button) findViewById(R.id.moreButton);

        // Navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerLayout = navigationView.getHeaderView(0);
        versionTextView = (TextView) headerLayout.findViewById(R.id.versionText);
        linkList = (LinearLayout) headerLayout.findViewById(R.id.linkList);
        linkContext = linkList.getContext();
    }

    /**
     *  Add param click
     */
    private View.OnClickListener addParamClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            addParam();
            /*
            LinearLayout parametersLayout = (LinearLayout) findViewById(R.id.parametersLayout);
            ParameterObject paramValueObject = new ParameterObject(parametersLayout.getContext());
            parametersLayout.addView(paramValueObject, 0);
            */
        }
    };

    /**
     *  Add param
     */
    private void addParam(){
        addParam("","");
    }

    /**
     *  Add param
     */
    private void addParam(String paramText, String valueText){
        LinearLayout parametersLayout = (LinearLayout) findViewById(R.id.parametersLayout);
        ParameterObject paramValueObject = new ParameterObject(parametersLayout.getContext());
        if (!"".equals(paramText)) paramValueObject.setParamText(paramText);
        if (!"".equals(valueText)) paramValueObject.setValueText(valueText);
        parametersLayout.addView(paramValueObject, 0);
    }

    /**
     *  Process click
     */
    private View.OnClickListener processClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Request request = fillNewRequest();
            outputController.trace(getString(R.string.loading));
            HttpController httpController = new HttpController();
            httpController.setListener(self);
            httpController.execute(request);
        }
    };

    /**
     *  More click
     */
    private View.OnClickListener moreClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (moreBlock.getVisibility() == View.GONE){
                moreSetVisibility(true);
            }else{
                moreSetVisibility(false);
            }
        }
    };

    /**
     * More visibility choice
     */
    private void moreSetVisibility(boolean visible)
    {
        if (visible){
            moreBlock.setVisibility(View.VISIBLE);
            moreButton.setText(R.string.less);
        }else{
            moreBlock.setVisibility(View.GONE);
            moreButton.setText(R.string.more);
        }
    }

    /**
     *  Save click
     */
    private View.OnClickListener saveClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            outputController.trace(getString(R.string.loading));
            Request request = fillNewRequest();
            setNewSaveRequest(request);
            saveAllRequests();
        }
    };

    /**
     * Save only
     */
    public static void saveAllRequests()
    {
        FileController fileController = new FileController(mainActivity);
        fileController.setListener(self);
        fileController.setMethod(FileController.METHOD_WRITE);
        fileController.execute(getAllSavedRequests());
    }

    /**
     *  Set new save request on left panel
     *
     * @param request (required)
     */
    private void setNewSaveRequest(Request request)
    {
        SaveObject so = new SaveObject(linkContext);
        so.setListener(self);
        so.setRequest(request);
        linkList.addView(so);
    }

    /**
     *  Get all saved Requests
     *
     *  @return ArrayList<Request>
     */
    //private ArrayList<Request> getAllSavedRequests()
    private static ArrayList<Request> getAllSavedRequests()
    {
        ArrayList<Request> requests = new ArrayList<>();
        int length = linkList.getChildCount();
        for (int i=0; i<length; i++) {
            View child = linkList.getChildAt(i);
            if(child instanceof SaveObject) {
                SaveObject saveObject = (SaveObject) child;
                requests.add(saveObject.getRequest());
            }
        }

        return requests;
    }

    /**
     *  Fill new request instance with user data
     *
     *  @return Request
     */
    public Request fillNewRequest()
    {
        Request request = new Request();

        request.setRequestName(requestName.getText().toString());
        request.setUrl(url.getText().toString());
        request.setVerb((Verb) verbSelect.getSelectedItem());
        request.setMimeType(mimeType.getText().toString());
        request.setReferer(refererEditText.getText().toString());
        request.setLogin(loginEditText.getText().toString());
        request.setPassword(passwordEditText.getText().toString());
        int length = parametersLayout.getChildCount();
        for (int i=0; i<length; i++){
            View child = parametersLayout.getChildAt(i);
            if(child instanceof ParameterObject) {
                request.setParam(
                        ((ParameterObject) child).getParamText(),
                        ((ParameterObject) child).getValueText()
                );
            }
        }

        return request;
    }

    /**
     *  remove all data in main
     */
    public void clearContent()
    {
        requestName.setText("");
        url.setText("");
        //verbSelect.setId(0);
        verbSelect.setSelection(0);
        mimeType.setText("");
        refererEditText.setText("");
        loginEditText.setText("");
        passwordEditText.setText("");
        parametersLayout.removeAllViews();
        outputController.clear();
    }

    /**
     *  Fill content main with request instance data
     *
     *  @param request Request
     */
    public void fillContentWithRequest(Request request)
    {
        // Mask 'more' block
        moreSetVisibility(false);

        clearContent();
        requestName.setText(request.getRequestName());
        url.setText(request.getUrl());
        verbSelect.setSelection(request.getVerb().getPosition());
        mimeType.setText(request.getMimeType());
        refererEditText.setText(request.getReferer());
        loginEditText.setText(request.getLogin());
        passwordEditText.setText(request.getPassword());

        List<Parameter> list = request.getParams();
        int length = list.size();
        for (int i=0; i<length; i++){
            addParam(list.get(i).getName(), list.get(i).getValue());
        }
    }

    /**
     *  CallBack method, trace message on resultText
     *  For example: after HTTP request
     *
     *  @param message (required) HTTP request response
     */
    public void messageCallBack(String message)
    {
        outputController.trace(message);
    }

    /**
     *  CallBack method, show image
     *  For example: after HTTP request
     *
     *  @param bmp (required) Bitmap
     */
    public void imageCallBack(Bitmap bmp)
    {
        outputController.show(bmp);
    }

    /**
     *  CallBack method, append message on resultText
     *  For example: after saving
     *
     *  @param message (required)
     */
    public void appendCallBack(String message)
    {
        outputController.append(message);
    }

    /**
     *  CallBack after load requests
     *  Used by FileController for the first file load
     *
     *  @param requests (required)
     */
    public void loadRequestsCallBack(ArrayList<Request> requests)
    {
        int length = requests.size();
        outputController.trace(length + " " + getString(R.string.saved_requests_loaded));

        for (int i=0; i<length; i++) {
            Request request = requests.get(i);
            if (request != null) setNewSaveRequest(request);
        }
    }

    /**
     *  CallBack method, after clicking on saved request
     *
     *  @param request (required) To fill form
     */
    public void selectRequestCallBack(Request request)
    {
        fillContentWithRequest(request);
        onBackPressed();
    }
}

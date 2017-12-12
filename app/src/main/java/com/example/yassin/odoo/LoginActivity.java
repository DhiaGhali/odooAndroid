package com.example.yassin.odoo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

      /*
        //TODO: check internet connection
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!USI.checkInternet(LoginActivity.this)){
                    noInternet(true);
                }else{
                    noInternet(false);
                }
            }
        },4000);
        */
    }

    private void noInternet(boolean _noInternet) {
        if(_noInternet){
            mEmailSignInButton.setEnabled(false);
        }else{
            mEmailSignInButton.setEnabled(true);
        }
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            auth(email,password);
        }
    }

    private void auth(final String email,final  String password) {
        StringRequest request = new StringRequest(Request.Method.POST, USI.urlToConnect,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.indexOf("failed")!=-1){
                            showAlertDialogue();
                        }else {
                            extraireUserInfo(response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                checkError(error.toString()) ;
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String > params = new HashMap<>();
                params.put("email" , email);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void extraireUserInfo(String JSONData) {
        try{
            JSONObject jsonObject = new JSONObject(JSONData);
            String email = jsonObject.getString("email");
            String pass = mPasswordView.getText().toString();
            String name = jsonObject.getString("name");
            String permission ="";
            if(jsonObject.getString("achat_permission").equals("1")){
                permission="product";
            }else if(jsonObject.getString("vente_permission").equals("1")){
                permission="commande";
            }
            USI.createSession(this,email,name,pass,permission);
            if(permission.equals("product")) {
                Intent intent = new Intent(LoginActivity.this, ProductActivity.class);
                startActivity(intent);
            }else if(permission.equals("commande")){
                //TODO : to remove comment
                 Intent intent = new Intent(LoginActivity.this, CommandeActivity.class);
                 startActivity(intent);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialogue() {
        final AlertDialog dialogue = new AlertDialog.Builder(this).create();
        dialogue.setTitle(getResources().getString(R.string.error_signin));
        dialogue.setMessage(getResources().getString(R.string.error_incorrect_password));
        dialogue.setButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogue.dismiss();
                showProgress(false);
                mPasswordView.setText("");
            }
        });
        dialogue.show();
    }

    private void checkError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        if (show) {
            mProgressView.setVisibility(View.VISIBLE);
            mLoginFormView.setVisibility(View.GONE);
        }else{
            mProgressView.setVisibility(View.GONE);
            mLoginFormView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed(){}
}
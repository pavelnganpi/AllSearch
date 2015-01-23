package com.paveynganpi.allsearch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    protected Button mButton;//reference to button on the main screen
    protected EditText mImageSearchName;//reference to edit text on main screen
    protected String mEditedString;//contains %20 when ever there is a white space

    private static final String KEY_SPACE = "space";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mImageSearchName = (EditText)findViewById(R.id.imageSearchName);

        mButton = (Button)findViewById(R.id.imageSearchNameButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ImageGrid.class);

                mEditedString = mImageSearchName.getText().toString();//get the string inside text box on main screen
                mEditedString = mEditedString.replaceAll(" ","%20");//replace all white spaces with %20
                intent.putExtra(KEY_SPACE,mEditedString);
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

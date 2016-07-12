package com.chatt;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chatt.custom.CustomActivity;

/**
 * Created by mithramedia on 09/06/16.
 */
public class ChatView extends CustomActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatview);

        setTouchNClick(R.id.btnAdd);
        setTouchNClick(R.id.btnProject);
        setTouchNClick(R.id.btnCamera);
        setTouchNClick(R.id.btnSend);

        getActionBar().setLogo(R.drawable.icon_trans);
    }

    /* (non-Javadoc)
     * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
     */
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnSend)
        {
            finish();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.newchat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* (non-Javadoc)
     * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}

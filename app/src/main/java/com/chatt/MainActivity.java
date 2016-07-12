package com.chatt;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chatt.custom.CustomActivity;
import com.chatt.model.Data;
import com.chatt.ui.BroadcastList;
import com.chatt.ui.LeftNavAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * The Class MainActivity is the base activity class of the application. This
 * activity is launched after the Splash and it holds all the Fragments used in
 * the app. It also creates the Navigation Drawer on left side.
 */
public class MainActivity extends CustomActivity
{

	/** The drawer layout. */
	private DrawerLayout drawerLayout;

	/** ListView for left side drawer. */
	private ListView drawerLeft;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupContainer();
		setupDrawer();
	}

	/**
	 * Setup the drawer layout. This method also includes the method calls for
	 * setting up the Left side drawer.
	 */
	private void setupDrawer()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view)
			{
				setActionBarTitle();
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle("Chat");
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.closeDrawers();

		setupLeftNavDrawer();
	}

	/**
	 * Setup the left navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the left side drawer. You can also setup
	 * the Header and Footer contents of left drawer if you need them.
	 */
	private void setupLeftNavDrawer()
	{
		drawerLeft = (ListView) findViewById(R.id.left_drawer);


		View header = getLayoutInflater().inflate(R.layout.left_nav_header,
				null);
		TextView t1 = (TextView) header.findViewById(R.id.textView1);
		String subname = ParseUser.getCurrentUser().get("subname").toString();
		//String uname = ParseUser.getCurrentUser().getUsername().toString();
		t1.setText(subname);

		TextView t2 = (TextView) header.findViewById(R.id.textView2);
		String subname1 = ParseUser.getCurrentUser().get("Third").toString();
		//String subname = ParseUser.getCurrentUser().get("subname").toString();
		t2.setText(subname1);

		ImageView img = (ImageView) header.findViewById(R.id.imageView1);
		String imagename = ParseUser.getCurrentUser().get("country").toString();
		//int Resid = getResources().getIdentifier(imagename,"drawable",getPackageName());
		Log.d("ImageName",imagename);

		if(imagename.equals("america")){
			img.setImageResource(R.drawable.american);


		}
		else if (imagename.equals("peru"))
		{
			img.setImageResource(R.drawable.peru);
			Log.d("Img","american");
		}
		else if (imagename.equals("france"))
		{
			img.setImageResource(R.drawable.france);
			Log.d("Img","american");
		}
		else if(imagename == null) {
			img.setImageResource(R.drawable.default_icon);
			Log.d("Img","Deafult");
		}
		drawerLeft.addHeaderView(header);

		drawerLeft.setAdapter(new LeftNavAdapter(this, getDummyLeftNavItems()));
		drawerLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				drawerLayout.closeDrawers();
				launchFragment(pos);
			}
		});
		drawerLayout.openDrawer(drawerLeft);
	}

	/**
	 * This method returns a list of dummy items for left navigation slider. You
	 * can write or replace this method with the actual implementation for list
	 * items.
	 * 
	 * @return the dummy items
	 */
	private ArrayList<Data> getDummyLeftNavItems()
	{
		ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data("Streams", null,R.drawable.ic_camera));
		al.add(new Data("Chat", null, R.drawable.ic_chat));
		al.add(new Data("Notes", null, R.drawable.ic_notes));
		al.add(new Data("Groups", null, R.drawable.ic_projects));
		al.add(new Data("Settings", null, R.drawable.ic_setting));
		//al.add(new Data("About Chatt", null, R.drawable.ic_about));
		al.add(new Data("Logout", null, R.drawable.ic_logout));
		return al;
	}

	/**
	 * This method can be used to attach Fragment on activity view for a
	 * particular tab position. You can customize this method as per your need.
	 * 
	 * @param pos
	 *            the position of tab selected.
	 */
	private void launchFragment(int pos)
	{
		Fragment f = null;
		String title = null;
		if (pos == 1)
		{
			title = "Listening Streams";
			f = new BroadcastList();
		}
		else if (pos == 2)
		{
			title = "Chat";
			//f = new ChatList();
		}
		else if (pos == 3)
		{
			title = "Notes";
			//f = new NoteList();
		}
		else if (pos == 4)
		{
			title = "Groups";
			//f = new ProjectList();
		}
//		else if (pos == 6)
//		{
//
//			title = "About Chatt";
//			f = new AboutChat();
//		}
		else if (pos == 6)
		{
			//ParseUser.logOut();
			startActivity(new Intent(this, com.chatt.Login.class));
			finish();
		}
		if (f != null)
		{
			while (getSupportFragmentManager().getBackStackEntryCount() > 0)
			{
				getSupportFragmentManager().popBackStackImmediate();
			}
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, f).addToBackStack(title)
					.commit();
		}
	}

	/**
	 * Setup the container fragment for drawer layout. The current
	 * implementation of this method simply calls launchFragment method for tab
	 * position 1 as the position 0 is for List header view. You can customize
	 * this method as per your need to display specific content.
	 */
	private void setupContainer()
	{
		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged()
					{
						setActionBarTitle();
					}
				});
		launchFragment(1);
	}

	/**
	 * Set the action bar title text.
	 */
	private void setActionBarTitle()
	{
		if (drawerLayout.isDrawerOpen(drawerLeft))
		{
			getActionBar().setTitle(R.string.app_name);
			return;
		}
		if (getSupportFragmentManager().getBackStackEntryCount() == 0)
			return;
		String title = getSupportFragmentManager().getBackStackEntryAt(
				getSupportFragmentManager().getBackStackEntryCount() - 1)
				.getName();
		getActionBar().setTitle(title);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see com.newsfeeder.custom.CustomActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (getSupportFragmentManager().getBackStackEntryCount() > 1)
			{
				getSupportFragmentManager().popBackStackImmediate();
			}
			else
				finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

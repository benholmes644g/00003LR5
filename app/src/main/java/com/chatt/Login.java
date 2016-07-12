package com.chatt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chatt.custom.CustomActivity;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * The Class Login is an Activity class that shows the login screen to users.
 * The current implementation simply start the MainActivity. You can write your
 * own logic for actual login and for login using Facebook and Twitter.
 */
public class Login extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
	 */

	private EditText u_name;
	private EditText u_pass;
	private static final float ROTATE_FROM = 0.0f;
	private static final float ROTATE_TO = 359.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login1);
		Parse.enableLocalDatastore(this);

		//runlogo();


		u_name = (EditText) findViewById(R.id.editText1);
		u_pass = (EditText) findViewById(R.id.editText2);




		ParseObject.registerSubclass(AddUser.class);
		Parse.initialize(new Parse.Configuration.Builder(this)
		.applicationId("com.unanimous.studio")
		.addNetworkInterceptor(new ParseLogInterceptor())
				//.server("http://192.168.1.197:1337/parse/").build());
				.server("http://23.249.163.152:1338/parse/").build());

		setTouchNClick(R.id.btnLogin);
		//setTouchNClick(R.id.btnFb);
		//setTouchNClick(R.id.btnTw);
		//setTouchNClick(R.id.btnReg);
	}

	/* (non-Javadoc)
	 * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
	 */


	@Override
	public void onClick(View v)
	{

		super.onClick(v);
		if (v.getId() == R.id.btnLogin)
		{
			//startActivity(new Intent(this, MainActivity.class));
		//	Intent intent = new Intent(Login.this,MainActivity.class);
			ParseUser user = new ParseUser();
			
			String user_name = u_name.getText().toString();
			String pass_word = u_pass.getText().toString();

			user.logInInBackground(user_name, pass_word, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException e) {
					if(e == null){

						Toast.makeText(getApplicationContext(),"Login Successfull",Toast.LENGTH_LONG).show();
						Intent intent = new Intent(Login.this,MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
					else{
						e.printStackTrace();
						Toast.makeText(getApplicationContext(),"Unsuccessfull Attempt",Toast.LENGTH_LONG).show();
					}
				}
			});

		}


//		if(v.getId() == R.id.btnReg){
//
//			String user_name = u_name.getText().toString();
//			String pass_word = u_pass.getText().toString();
//			String status = "offline";
//
//
//			ParseUser register = new ParseUser();
//
//
//			register.setUsername(user_name);
//			register.setPassword(pass_word);
//			register.put("sttaus","offline");
//			register.put("subname","-");
//			register.put("country","america");
//			register.put("avtar","default.png");
//			register.signUpInBackground(new SignUpCallback() {
//				@Override
//				public void done(ParseException e) {
//					Toast.makeText(getApplicationContext(),"Registration Successfully",Toast.LENGTH_LONG).show();
//					Intent intent = new Intent(Login.this,MainActivity.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
//				}
//			});
//
//
//		}
	}

	public void runlogo(){

		ImageView img = (ImageView) findViewById(R.id.logo);

		RotateAnimation r; // = new RotateAnimation(ROTATE_FROM, ROTATE_TO);
		r = new RotateAnimation(ROTATE_FROM, ROTATE_TO, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		r.setDuration((long) 3000);
		r.setRepeatCount(10);
		img.startAnimation(r);

	}


}

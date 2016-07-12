package com.chatt.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chatt.R;
import com.chatt.custom.CustomFragment;
import com.chatt.model.ChatItem;

import java.util.ArrayList;

/**
 * The Class NoteList is the Fragment class that is launched when the user
 * clicks on Notes option in Left navigation drawer. It simply display a dummy list of notes.
 * You need to write actual implementation for loading and displaying notes
 */
public class NoteList extends CustomFragment
{

	/** The Note list. */
	private ArrayList<ChatItem> noteList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.note, null);

		loadNoteList();
		ListView list = (ListView) v.findViewById(R.id.list);
		list.setAdapter(new NoteAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{

			}
		});

		setTouchNClick(v.findViewById(R.id.tab1));
		setTouchNClick(v.findViewById(R.id.tab2));
		setTouchNClick(v.findViewById(R.id.btnNewChat));
		return v;
	}

	/* (non-Javadoc)
	 * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.tab1)
		{
			getView().findViewById(R.id.tab2).setEnabled(true);
			v.setEnabled(false);
		}
//		else if (v.getId() == R.id.tab2)
//		{
//			getView().findViewById(R.id.tab1).setEnabled(true);
//			v.setEnabled(false);
//		}
//		else if (v.getId() == R.id.btnNewChat)
//			startActivity(new Intent(getActivity(), NewChat.class));
	}

	/**
	 * This method currently loads a dummy list of Notes. You can write the
	 * actual implementation of loading Notes.
	 */
	private void loadNoteList()
	{
		ArrayList<ChatItem> noteList = new ArrayList<ChatItem>();
		noteList.add(new ChatItem("This is", "Demo",
				"Notes", "12:10PM", 01, true, false));
//		noteList.add(new ChatItem(
//				"Steps",
//				"iOS app developer",
//				"I agree with you and with "
//						+ "your thoughts, you are right! We need some changes in life",
//				"08:20AM @aaron", 0, true, true));
//		noteList.add(new ChatItem("To hurry up!", "Urgent!",
//				"Hey man, long time. Hows your work goin"
//						+ " on and hows your life?", "01/20/2014 @metinP", 0,
//				false, false));
//		noteList.add(new ChatItem("Evaluate with wife", "Vacations...",
//				"How are you?", "01/20/2014 @Linda123", R.drawable.user3,
//				false, false));
//		noteList.add(new ChatItem("Case Study", "Life!",
//				"Hope you and your team is doing great. I know you are"
//						+ " very busy with work", "01/22/2014 @BensonQ", 0,
//				true, false));
//		noteList.add(new ChatItem("Apratment block", "Happy hours",
//				"Yes, it's very good", "01/22/2014 @julia", 0, true, true));
//		noteList.add(new ChatItem("Dinner party", "Last party",
//				"This is last party...", "01/22/2014 @me", 0, false, false));

		this.noteList = new ArrayList<ChatItem>(noteList);
		//this.noteList.addAll(noteList);
		//this.noteList.addAll(noteList);
	}

	/**
	 * The Class CutsomAdapter is the adapter class for Note ListView. The
	 * currently implementation of this adapter simply display static dummy
	 * contents. You need to write the code for displaying actual contents.
	 */
	private class NoteAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return noteList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public ChatItem getItem(int arg0)
		{
			return noteList.get(arg0);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
			if (v == null)
				v = LayoutInflater.from(getActivity()).inflate(
						R.layout.note_item, null);

			ChatItem c = getItem(pos);
			TextView lbl = (TextView) v.findViewById(R.id.lbl1);
			lbl.setText(c.getName());

			lbl = (TextView) v.findViewById(R.id.lbl2);
			lbl.setText(c.getDate());

			lbl = (TextView) v.findViewById(R.id.lbl3);
			lbl.setText(c.getTitle());

			lbl = (TextView) v.findViewById(R.id.lbl4);
			lbl.setText(c.getMsg());

			ImageView img = (ImageView) v.findViewById(R.id.img2);
			img.setImageResource(c.isGroup() ? R.drawable.ic_group
					: R.drawable.ic_lock);

			return v;
		}

	}
}

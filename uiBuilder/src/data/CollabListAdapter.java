package data;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

import de.ur.rk.uibuilder.R;

public class CollabListAdapter extends ArrayAdapter<ParseUser> implements OnClickListener
{
	private List<ParseUser> users;
	private int layout;
	private LayoutInflater inflater;

	public CollabListAdapter(Context context, int resource,
			int textViewResourceId, List<ParseUser> objects)
	{
		super(context, resource, textViewResourceId, objects);

		users = objects;
		layout = R.layout.activity_project_manager_userlist;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Log.d("collabs in adapter", String.valueOf(users.size()));
	}

	@Override
	public ParseUser getItem(int position)
	{
		// TODO Auto-generated method stub
		return users.get(position);
	}

	@Override
	public int getPosition(ParseUser item)
	{
		// TODO Auto-generated method stub
		return super.getPosition(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout v = (LinearLayout) inflater.inflate(layout, null);
		TextView nameField = (TextView) v.findViewById(R.id.userlist_name);
		Button requestDelete = (Button) v.findViewById(R.id.userlist_delete);
		
		if (users.get(position).hasSameId(ParseUser.getCurrentUser()))
		{
			nameField.setText("You.");
			requestDelete.setVisibility(View.GONE);
		}
		else
		{
			String name = users.get(position).getUsername();
			nameField.setText(name);
			
			requestDelete.setOnClickListener(this);
			requestDelete.setTag(users.get(position));
		}
		
		return v;
	}
	

		public interface OnRemoveUserFromProjectRequestListener
		{
			void userRemoved(ParseUser removedUser);
		}

		private static OnRemoveUserFromProjectRequestListener removeFromProjectListener;

		public static void setOnRemoveUserFromProjectRequestListener(
				OnRemoveUserFromProjectRequestListener listener)
		{
			CollabListAdapter.removeFromProjectListener = listener;
		}

		@Override
		public void onClick(View userItem)
		{
			ParseUser deletedUser = (ParseUser) userItem.getTag();
			Log.d("requesting delete user", deletedUser.getEmail());
			
			removeFromProjectListener.userRemoved(deletedUser);
		}

}

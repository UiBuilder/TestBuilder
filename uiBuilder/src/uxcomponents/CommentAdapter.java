package uxcomponents;

import com.parse.ParseUser;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class CommentAdapter extends CursorAdapter
{
	private int userNameIdx, dateIdx, commentIdx, userParseIdIdx;
	private LayoutInflater inflater;
	
	private ImageView userImage;
	private TextView userName;
	private TextView userComment;

	public CommentAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor commentCursor)
	{
		userParseIdIdx = commentCursor.getColumnIndexOrThrow(ScreenProvider.KEY_COMMENTS_USERID);
		dateIdx = commentCursor.getColumnIndexOrThrow(ScreenProvider.KEY_COMMENT_DATE);
		commentIdx = commentCursor.getColumnIndexOrThrow(ScreenProvider.KEY_COMMENTS_COMMENT);
		userNameIdx = commentCursor.getColumnIndexOrThrow(ScreenProvider.KEY_COLLAB_NAME);
		
		String userId = commentCursor.getString(userParseIdIdx);
		userComment.setText(commentCursor.getString(commentIdx));
		userName.setText(commentCursor.getString(userNameIdx));
		
		if (userId.equalsIgnoreCase(ParseUser.getCurrentUser().getObjectId()))
		{
			userImage.setBackgroundColor(context.getResources().getColor(R.color.white));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		View comment = inflater.inflate(R.layout.comment_list_item, parent, false);
		
		userImage = (ImageView) comment.findViewById(R.id.comment_user_icon);
		userName = (TextView) comment.findViewById(R.id.comment_user_name);
		userComment = (TextView) comment.findViewById(R.id.comment_user_text);
		
		return comment;
	}

}

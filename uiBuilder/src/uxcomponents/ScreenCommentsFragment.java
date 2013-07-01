package uxcomponents;

import java.util.ArrayList;

import cloudmodule.CloudConstants;

import com.parse.ParseUser;

import uxcomponents.ProjectSectionsFragment.sectionSelectedListener;

import helpers.Log;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import data.ScreenProvider;
import de.ur.rk.uibuilder.R;

public class ScreenCommentsFragment extends Fragment implements OnClickListener, LoaderCallbacks<Cursor>
{
	public static final String VIEWTYPE = "viewtype";
	private static final boolean TYPE_DISPLAY = false;
	private static final boolean TYPE_COMMENTING = true;
	
	private View root;
	private LinearLayout rootLayout;
	private ListView commentList;
	private RelativeLayout commentingArea;
	private LoaderManager manager;
	private CommentAdapter adapter;
	
	private Button sendComment;
	private EditText comment;
	
	private int screenId;
	
	private ArrayList<Comment> comments;
	private ArrayAdapter<Comment> tempadapter;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Log.d("sections Fragment", "activity created called");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("sections Fragment", "onCreateView called");

		Bundle values = getArguments();
		
		screenId = values.getInt(ScreenProvider.KEY_ID);
		boolean type = values.getBoolean(VIEWTYPE);
		
		if (root == null)
		{
			setupUI(inflater, container);
		}
		if (type == TYPE_DISPLAY)
		{
			//commentingArea.setVisibility(View.GONE);
		}
		else if (type == TYPE_COMMENTING)
		{
			
		}
		setupDatabaseConnection();
		
		return root;
	}

	private void setupDatabaseConnection()
	{
		manager = getLoaderManager();
		adapter = new CommentAdapter(getActivity(), null, true);
		
		manager.initLoader(ScreenProvider.LOADER_ID_COMMENTS, null, this);
	}

	/**
	 * @param inflater
	 * @param container
	 */
	private void setupUI(LayoutInflater inflater, ViewGroup container)
	{
		root = inflater.inflate(R.layout.layout_screen_comment_fragment_root, container, false);
		rootLayout = (LinearLayout) root;
		commentList = (ListView) root.findViewById(R.id.screen_comments_list);
		commentList.addHeaderView(inflater.inflate(R.layout.layout_screen_comment_fragment_headerxml, null));
		commentingArea = (RelativeLayout) commentList.findViewById(R.id.screen_comments_commenting_area);
		
		sendComment = (Button) commentingArea.findViewById(R.id.screen_comments_send);
		comment = (EditText) commentingArea.findViewById(R.id.screen_comments_comment);
		
		sendComment.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		Comment c = new Comment("me", comment.getText().toString());
		//comments.add(c);
		
		ContentValues values = new ContentValues();
		values.put(ScreenProvider.KEY_COMMENTS_COMMENT, comment.getText().toString());
		values.put(ScreenProvider.KEY_COMMENTS_USERID, ParseUser.getCurrentUser().getObjectId());
		values.put(ScreenProvider.KEY_COLLAB_NAME, ParseUser.getCurrentUser().getString(CloudConstants.USER_DISPLAY_NAME));
		values.put(ScreenProvider.KEY_COMMENTS_ASSOCIATED_SCREEN, screenId);
		comment.setText("");
		
		ContentResolver resolver = getActivity().getContentResolver();
		resolver.insert(ScreenProvider.CONTENT_URI_COMMENTS, values);
		manager.restartLoader(ScreenProvider.LOADER_ID_COMMENTS, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		String selection = ScreenProvider.KEY_COMMENTS_ASSOCIATED_SCREEN + " = " + "'" + String.valueOf(screenId) + "'";
		
		return new CursorLoader(getActivity(), ScreenProvider.CONTENT_URI_COMMENTS, null, selection, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1)
	{
		adapter.swapCursor(arg1);
		adapter.notifyDataSetChanged();
		commentList.setAdapter(adapter);
		
		commentListener.newCommentsLoaded(arg1.getCount());
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0)
	{
		adapter.swapCursor(null);
		
	}
	
	public interface commentsUpdatedListener
	{
		void newCommentsLoaded(int number);
	}

	private static commentsUpdatedListener commentListener;

	public static void setCommentsUpdatedListener(
			commentsUpdatedListener listener)
	{
		ScreenCommentsFragment.commentListener = listener;
	}
}

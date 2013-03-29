package editmodules;

import uibuilder.EditmodeFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import editmodules.ExpansionListener.onToggleExpansionListener;

public abstract class Module implements onToggleExpansionListener
{
	protected Context context; /** the activity context */
	protected LayoutInflater inflater; /** the inflater used by the subclasses to inflate their ui*/
 	
	
	/**
	 * <b>Constructor</b>
	 * setupUi and setListeners are called implicitly when instances of subclasses call the super implementation.
	 * each box must be expandable, so the listener is set in the superclass constructor.
	 * @param fragment the EditmodeFragment containing the box
	 */
	public Module(EditmodeFragment fragment)
	{
		this.context = fragment.getActivity();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		setupUi();
		setListeners();
		
		ExpansionListener.setOnToggleExpansionListener(this);
	}
	
	/**
	 * Each box should setup its user-interface in this method
	 */
	protected abstract void setupUi();
	
	/**
	 * Listeners for user interaction and system events should be set up here
	 */
	protected abstract void setListeners();
	
	/**
	 * This method is called when the current context requires a module of the specific type
	 * @param inProgress the selected item on the drawing area
	 * @return a fully set up layout containing the user interface for the specific module
	 */
	public abstract LinearLayout getInstance(View inProgress);
	
	/**
	 * User interface elements which indicate current states of the selected item should adapt 
	 * their properties here.
	 */
	protected abstract void adaptToContext();
	
	
}

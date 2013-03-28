package editmodules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import editmodules.ExpansionListener.onToggleExpansionListener;

public abstract class Module implements onToggleExpansionListener
{
	protected Context context;
	protected LayoutInflater inflater;
 	
	public Module(Context context)
	{
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		setupUi();
		setListeners();
		
		ExpansionListener.setOnToggleExpansionListener(this);
	}
	
	protected abstract void setupUi();
	
	protected abstract void setListeners();
	
	public abstract LinearLayout getInstance(View inProgress);
	
	protected abstract void adaptToContext();
	
	
}

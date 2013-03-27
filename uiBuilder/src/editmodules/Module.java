package editmodules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import editmodules.ExpansionListener.onToggleExpansionListener;

public abstract class Module implements onToggleExpansionListener
{
	private Context context;
	protected LayoutInflater inflater;
 	
	public Module(Context context)
	{
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setupUi();
		ExpansionListener.onToggleExpansionListener(this);
	}
	
	protected abstract void setupUi();
	
	public abstract View getInstance(View inProgress);
	
}

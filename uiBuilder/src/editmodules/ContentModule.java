package editmodules;

import uibuilder.EditmodeFragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import data.SampleAdapter;
import de.ur.rk.uibuilder.R;

/**
 * Provides the interface to change the the sample content of a grid or list view.
 * For information about the instantiation and general concept behind this
 * @see Module
 * 
 * @author funklos
 *
 */
public class ContentModule extends Module
{
	private LinearLayout box;
	
	private Button chooseHipster;
	private Button chooseBacon;
	
	private SampleAdapter samples;

	public ContentModule(EditmodeFragment context)
	{
		super(context);
		samples = new SampleAdapter(super.context);
	}

	@Override
	public void getValues()
	{
		// TODO Auto-generated method stub
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		// TODO Auto-generated method stub
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_choose_content, null);
		
		chooseHipster = (Button) box.findViewById(R.id.content_choose_hipster);
		chooseBacon = (Button) box.findViewById(R.id.content_choose_bacon);
	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box, super.context));
		
		chooseHipster.setOnClickListener(new ContentSelectedListener());
		chooseBacon.setOnClickListener(new ContentSelectedListener());
	}
	
	@Override
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
		adaptToContext();
		
		return box;
	}

	@Override
	protected void adaptToContext()
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * 
	 * @author funklos
	 * 
	 */
	private class ContentSelectedListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			int id = v.getId();

			switch (id)
			{
			case R.id.content_choose_hipster:
			case R.id.content_choose_bacon:
				samples.setSampleContent(container, id);
			}

		}
	}

}

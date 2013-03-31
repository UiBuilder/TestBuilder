package editmodules;

import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import data.ObjectValues;
import data.SampleAdapter;
import de.ur.rk.uibuilder.R;

public class ContentModule extends Module
{
	private LinearLayout box;
	private View requesting;
	
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
		box.setOnClickListener(new ExpansionListener(box));
		
		chooseHipster.setOnClickListener(new ContentSelectedListener());
		chooseBacon.setOnClickListener(new ContentSelectedListener());
	}

	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		
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
			Bundle objectBundle = (Bundle) requesting.getTag();
			int id = v.getId();

			switch (id)
			{
			case R.id.content_choose_hipster:
			case R.id.content_choose_bacon:
				samples.setSampleContent(requesting, id);
//				objectBundle.putInt(ObjectValues.EXAMPLE_CONTENT, id);
			}

		}
	}

}

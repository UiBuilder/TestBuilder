package editmodules;

import uibuilder.EditmodeFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import data.ObjectValues;
import de.ur.rk.uibuilder.R;

public class StarCountModule extends Module
{
	SeekBar starBar, ratingSlider;

	private LinearLayout box;

	private View requesting;

	public StarCountModule(EditmodeFragment context)
	{
		super(context);
	}

	@Override
	public void getValues()
	{
		adaptToContext();
	}

	@Override
	protected void setupUi()
	{
		box = (LinearLayout) super.inflater.inflate(R.layout.editmode_entry_stars_count, null);

		starBar = (SeekBar) box.findViewById(R.id.star_count_seekbar);
		ratingSlider = (SeekBar) box.findViewById(R.id.star_rating_seekbar);
		
		starBar.setMax(9);
		ratingSlider.setMax(10);
	}

	@Override
	public LinearLayout getInstance(View inProgress)
	{
		requesting = inProgress;
		adaptToContext();

		return box;
	}

	private class RatingModuleListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{
			Bundle valuesBundle = (Bundle) requesting.getTag();
			switch (seekBar.getId())
			{
			case R.id.star_count_seekbar:
				
				progress += 1;
				
				valuesBundle.putInt(ObjectValues.STARS_NUM, progress);
				
				((RatingBar) ((RelativeLayout) requesting).getChildAt(0)).setNumStars(progress);
				ratingSlider.setMax(progress);

				Log.d("StarcountSeekbar", "gotValue for progress: " + progress
						+ 1);
				break;

			case R.id.star_rating_seekbar:
				
				valuesBundle.putInt(ObjectValues.RATING, progress);

				Log.d("RatingSeekbar", "gotValue for progress: " + progress);
				((RatingBar) ((ViewGroup) requesting).getChildAt(0)).setRating(progress);
				break;
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub

		}
	}

	@Override
	protected void adaptToContext()
	{
		Bundle valuesBundle = (Bundle) requesting.getTag();
		
		starBar.setProgress(valuesBundle.getInt(ObjectValues.STARS_NUM)-1);
		ratingSlider.setProgress(valuesBundle.getInt(ObjectValues.RATING));


	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box));
		RatingModuleListener ratingListener = new RatingModuleListener();

		starBar.setOnSeekBarChangeListener(ratingListener);
		ratingSlider.setOnSeekBarChangeListener(ratingListener);
	}

}

package editmodules;

import uibuilder.EditmodeFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
			switch (seekBar.getId())
			{
			case R.id.star_count_seekbar:
				((RatingBar) ((ViewGroup) requesting).getChildAt(0)).setNumStars(progress + 1);
				ratingSlider.setMax(progress + 1);

				Log.d("StarcountSeekbar", "gotValue for progress: " + progress
						+ 1);
				break;

			case R.id.star_rating_seekbar:
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
		ratingSlider.setProgress((int) (((RatingBar) ((ViewGroup) requesting).getChildAt(0)).getRating()));

		starBar.setProgress(((RatingBar) ((ViewGroup) requesting).getChildAt(0)).getNumStars() - 1);

	}

	@Override
	protected void setListeners()
	{
		// TODO Auto-generated method stub
		box.setOnClickListener(new ExpansionListener(box));
		RatingModuleListener ratingListener = new RatingModuleListener();

		starBar.setOnSeekBarChangeListener(ratingListener);
	}

}

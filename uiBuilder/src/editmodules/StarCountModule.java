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

/**
 * Provides the interface to adjust the ratingBar. For information about the
 * instantiation and general concept behind this
 * 
 * @see Module
 * 
 * @author jonesses
 * 
 */
public class StarCountModule extends Module
{
	private SeekBar starBar, ratingSlider;

	private LinearLayout box;

	private Bundle valuesBundle;

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
		starBar.setMax(7);
		ratingSlider.setMax(7);
	}

	@Override
	public LinearLayout getInstance(View container, View item)
	{
		this.container = (RelativeLayout) container;
		this.item = item;
		
		valuesBundle = (Bundle) container.getTag();
		
		adaptToContext();
		
		return box;
	}
	/*
	 * Using two separate Listeners for virtually the same type of action may
	 * seem counter-intuitive but for some reason delivers more predictable
	 * results. Switching the seekbar id doesn't do the job properly here.
	 */
	private class StarCountListener implements OnSeekBarChangeListener
	{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			progress += 1;

			valuesBundle.putInt(ObjectValues.STARS_NUM, progress);

			((RatingBar) item).setNumStars(valuesBundle.getInt(ObjectValues.STARS_NUM));
			ratingSlider.setMax(progress);

			Log.d("StarcountSeekbar", "gotValue for progress: " + progress + 1);

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{

		}

	}

	private class RatingListener implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
		{
			Log.d("RatingSeekbar", "gotValue for progress: " + progress);
			((RatingBar) item).setRating(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			valuesBundle.putInt(ObjectValues.RATING, Math.round(((RatingBar) item).getRating()));

		}
	}

	@Override
	protected void adaptToContext()
	{

		ratingSlider.setMax(valuesBundle.getInt(ObjectValues.STARS_NUM));
		starBar.setProgress(valuesBundle.getInt(ObjectValues.STARS_NUM) - 1);
		ratingSlider.setProgress(valuesBundle.getInt(ObjectValues.RATING));

		((RatingBar) item).setRating(valuesBundle.getInt(ObjectValues.RATING));

	}

	@Override
	protected void setListeners()
	{
		box.setOnClickListener(new ExpansionListener(box, super.context));
		StarCountListener starCountListener = new StarCountListener();
		RatingListener ratingListener = new RatingListener();
		starBar.setOnSeekBarChangeListener(starCountListener);
		ratingSlider.setOnSeekBarChangeListener(ratingListener);
	}

}

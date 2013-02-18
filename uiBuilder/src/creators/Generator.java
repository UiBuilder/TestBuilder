package creators;

import manipulators.Manipulator;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Generator
{

		private int idCount; /** Variable zur dynamischen Vergabe laufender IDs */
		private Manipulator manipulator;
		private Context context;
		/**
		 * Konstruktor
		 */
		public Generator(Context ref) 
		{
			idCount = 1;
			context = ref;
			manipulator = new Manipulator(ref);
		}

		public Manipulator getMani()
		{
			return manipulator;
		}
		/**
		 * Methode zur Generierung eines neuen TextView-Objekts.
		 * Default Eigenschaften werden gesetzt.
		 * @return Neuer TextView
		 */
		protected TextView newTextview() 
		{
			return null;
		}

		/**
		 * Methode zur Generierung eines neuen Button-Objekts.
		 * Default Eigenschaften werden gesetzt.
		 * 
		 * @return Neuer Button 
		 */
		protected Button newButton() 
		{
			Button generatedB = new Button(context){@Override
			public boolean performClick()
			{
				// TODO Auto-generated method stub
				return //super.performClick();
						true;
			}};
			
			
			generatedB.setText("Button");
			generatedB.setId(idCount);
			idCount++;
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			
			generatedB.setLayoutParams(params);
			//generatedB.setX(100);
			//generatedB.setY(100);
			generatedB.setEnabled(true);
			
			generatedB.setOnLongClickListener(manipulator);
			
			return generatedB;
		}
}


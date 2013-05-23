package helpers;



public class OptionsHolder
{
	public String name, description;
	
	public OptionsHolder (String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	public static OptionsHolder[] getOptions (String[] nameString, String[] descString)
	{
		OptionsHolder[] options = new OptionsHolder[nameString.length];
		
		for (int i=0; i<nameString.length; i++)
		{
			options[i] = new OptionsHolder(nameString[i], descString[i]);
		}
		return options;
	}
}

package data;

import java.io.File;

import android.os.Environment;

/**
 * If data has to be written to the filesystem, this class provides 
 * the default filepaths.
 * @author android developers photobyintent example
 *
 */
public final class AlbumDirFactory
{

	private static final String CAMERA_DIR = "/dcim/";
	private static final String PREVIEW_DIR = "/uiBuilder/";

	/**
	 * is called when a picture was taken by the user and has to be saved to the default directory
	 * @param albumName
	 * @return
	 */
	public File getAlbumStorageDir(String albumName) 
	{
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName 
		);
	}
	
	/**
	 * added by @author funklos following the pattern of @author android developers
	 * to fetch the filepath for the screenshots which are displayed in the manager activity
	 * @param previews
	 * @return
	 */
	public File getPreviewStorageDir(String previews) 
	{
		return new File (
				Environment.getExternalStorageDirectory()
				+ PREVIEW_DIR
				+ previews 
		);
	}
}

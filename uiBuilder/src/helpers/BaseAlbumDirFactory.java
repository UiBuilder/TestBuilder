package helpers;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @author android developers photobyintent example
 *
 */
public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";
	private static final String PREVIEW_DIR = "/uiBuilder/";

	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName 
		);
	}
	
	public File getPreviewStorageDir(String previews) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ PREVIEW_DIR
				+ previews 
		);
	}
}

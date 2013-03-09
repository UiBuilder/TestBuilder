package helpers;

import java.io.File;

/**
 * 
 * @author android developers photobyintent example
 *
 */

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}

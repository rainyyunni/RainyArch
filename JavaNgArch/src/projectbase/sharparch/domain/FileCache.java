package projectbase.sharparch.domain;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import projectbase.utils.ArgumentNullException;

public class FileCache {
	// / <summary>
	// / Deserializes a data file into an Object of type {T}.
	// / </summary>
	// / <typeparam name = "T">Type of Object to deseralize and
	// return.</typeparam>
	// / <param name = "path">Full path to file containing seralized
	// data.</param>
	// / <returns>If Object is successfully deseralized, the Object of type {T},
	// / otherwise null.</returns>
	// / <exception cref = "ArgumentNullException">Thrown if the path parameter
	// is null or empty.</exception>
	@SuppressWarnings("unchecked")
	public static <T> T RetrieveFromCache(String path) {
		if (path == null || path.isEmpty()) {
			throw new ArgumentNullException("path");
		}

		try {

			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			T obj = (T) ois.readObject();
			ois.close();
			return obj;
		} catch (Exception e) {
			// Return null if the Object can't be deseralized
			return null;
		}
	}

	// / <summary>
	// / Serialize the given Object of type {T} to a file at the given path.
	// / </summary>
	// / <typeparam name = "T">Type of Object to serialize.</typeparam>
	// / <param name = "obj">Object to serialize and store in a file.</param>
	// / <param name = "path">Full path of file to store the serialized
	// data.</param>
	// / <exception cref = "ArgumentNullException">Thrown if obj or path
	// parameters are null.</exception>
	public static <T> void StoreInCache(T obj, String path) throws IOException {
		if (obj == null) {
			throw new ArgumentNullException("obj");
		}

		if (path == null || path.isEmpty()) {
			throw new ArgumentNullException("path");
		}

		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(obj);
		oos.close();
	}
}

package eu.els.prio.importer.efl.cxml.revues;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class TestUtil {

	public static File copyFileFromClasspathToDir(ClassLoader classLoader, String fileName, String fileNamePrefix,
			File dir2ImportFrom) throws IOException {
		InputStream resourceAsStream = classLoader.getResourceAsStream(fileName);
		String destinationFileName = fileName.replace(fileNamePrefix, "");
		File destinationFile = new File(dir2ImportFrom, destinationFileName);
		FileUtils.copyInputStreamToFile(resourceAsStream, destinationFile);
		return destinationFile;
	}

}

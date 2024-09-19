package itstep.learning.services.files;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.random.FileNameGenerator;
import itstep.learning.services.stream.StringReader;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Singleton
public class LocalFileService implements FileService {
    private final String uploadPath;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");


    @Inject
    public LocalFileService(StringReader stringReader) {
        Map<String, String> ini = new HashMap<>();
        try( InputStream rs = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("files.ini")
        ) {
            String[] lines = stringReader.read( rs ).split("\n");
            for(String line : lines) {
                String[] parts = line.split("=");
                ini.put( parts[0].trim(), parts[1].trim() );
            }
        }
        catch (IOException ex) {
            System.err.println( ex.getMessage() );
            throw new RuntimeException( ex );
        }
        this.uploadPath = ini.get("upload_path");
    }

    @Override
    public String upload(FileItem fileItem) {
        String formFileName = fileItem.getName();
        int dotPosition = formFileName.lastIndexOf('.');
        String extension = formFileName.substring( dotPosition );

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            return null;
        }

        String filename;
        File file;
        do{
            filename = UUID.randomUUID() + extension;
            file = new File( this.uploadPath, filename );
        } while( file.exists() );

        try {
            fileItem.write( file );
        }
        catch (Exception ex) {
            System.err.println( ex.getMessage() );
            return null;
        }
        return filename;
    }

    @Override
    public OutputStream download(String filename) {
        return null;
    }
}

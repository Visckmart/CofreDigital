import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexHandler {
    
    static Charset readerCharset = Charset.forName("US-ASCII");
    
    List<FileInfo> parseIndex(Path indexFilePath) {
        ArrayList<FileInfo> index = new ArrayList<FileInfo>();
        try (BufferedReader reader = Files.newBufferedReader(indexFilePath, readerCharset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                FileInfo fi = new FileInfo(line);
                index.add(fi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}

package General;
import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Utilities.FileInfo;

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

    List<FileInfo> parseIndexContent(byte[] indexFileContent) {
        ArrayList<FileInfo> index = new ArrayList<FileInfo>();
        String[] lines = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(indexFileContent)).toString().split("\n");
        for (String line : lines) {
            FileInfo fi = new FileInfo(line);
            index.add(fi);
        }
        return index;
    }
    
    static List<FileInfo> testIndexHandler() {
        IndexHandler ih = new IndexHandler();
        Path p = FileSystems.getDefault().getPath("", "indice_exemplo.txt");
    
        List<FileInfo> index = ih.parseIndex(p);
        for (FileInfo fileInfo : index) {
          fileInfo.nomeOriginal += Character.toString((char)(new Random().nextInt(26) + 65));
        //   System.out.println(fileInfo);
          //System.out.println(fileInfo.checkAccess("Joao", "0"));
        }
        return index;
      }
}

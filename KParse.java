import java.io.IOException;
import org.parsers.kotlin.*;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class KParse {
    static FileSystem fileSystem = FileSystems.getDefault();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            usage();
        }
        for (String arg : args) {
            Path path = fileSystem.getPath(arg);
            try {
                parseFile(path, args.length==1);
                System.out.println("File " + path + " parsed successfully.");
            } catch (ParseException pe) {
                System.out.println("Failed on " + path);
                pe.printStackTrace();
            }
        }
    }

    static void usage() {
        System.out.println("Usage java KParse <files>");
    }

    static void parseFile(Path path, boolean dump) throws IOException {
        KotlinParser parser = new KotlinParser(path);
        parser.KotlinFile();
        Node root = parser.rootNode();
        if (dump) root.dump();
    }
}
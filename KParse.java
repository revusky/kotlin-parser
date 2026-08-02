import java.io.IOException;
import org.parsers.kotlin.*;

FileSystem fileSystem = FileSystems.getDefault();

public void main(String[] args) throws IOException {
    if (args.length == 0) {
        parseFile(fileSystem.getPath("Hello.kt"));
    }
    else for (String arg : args) {
        Path path = fileSystem.getPath(arg);
        try {
            parseFile(path);
            System.out.println("File " + path + " parsed successfully.");
        } catch (ParseException pe) {
            System.out.println("Failed on " + path);
            pe.printStackTrace();
        }
    }
}

void parseFile(Path path) throws IOException{
    KotlinParser parser = new KotlinParser(path);
    parser.KotlinFile();
    Node root = parser.rootNode();
    root.dump();
}
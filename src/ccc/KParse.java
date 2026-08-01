import java.io.IOException;

import kotlinparser.*;

FileSystem fileSystem = FileSystems.getDefault();

public void main(String[] args) throws IOException {
    //Path path = fileSystem.getPath(args[0]);
    Path path = fileSystem.getPath("Hello.kt");
    parseFile(path);
}

void parseFile(Path path) throws IOException{
    KotlinParser parser = new KotlinParser(path);
    parser.KotlinFile();
    Node root = parser.rootNode();
    root.dump();
}
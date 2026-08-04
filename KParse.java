import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.nio.file.Path;
import org.parsers.kotlin.KotlinParser;
import org.parsers.kotlin.Node;

/**
 * A test harness for parsing Kotlin files from the command line.
 */
public class KParse {
    static List<Node> roots = new ArrayList<>();
    static private List<Path> paths = new ArrayList<Path>(),
                              failures = new ArrayList<Path>(),
                              successes = new ArrayList<Path>();
    static private boolean parallelParsing, retainInMemory, quiet;
    static private FileSystem fileSystem = FileSystems.getDefault();

    static public void main(String args[]) throws IOException {
        for (String arg : args) {
            Path path = null;
            if (arg.equals("-p")) {
                System.out.println("Will parse in multiple threads.");
                parallelParsing = true;
                roots = Collections.synchronizedList(roots);
                failures = Collections.synchronizedList(failures);
                successes = Collections.synchronizedList(successes);
                continue;
            }
            if (arg.equals("-q")) {
                quiet = true;
                continue;
            }
            if (arg.equals("-r")) {
                retainInMemory = true;
                continue;
            }
            path = fileSystem.getPath(arg);
            if (!Files.exists(path)) {
                System.err.println("File " + path + " does not exist.");
                continue;
            }
            addPaths(path, paths);
        }
        if (paths.isEmpty()) {
            usage();
            return;
        }
        long startTime = System.currentTimeMillis();
        Stream<Path> stream = parallelParsing
                               ? paths.parallelStream()
                               :  paths.stream();
        stream.forEach(path -> parseFile(path));
//        for (Path path : failures) {
//            System.out.println("Parse failed on: " + path);
//        }
        System.out.println("\nParsed " + successes.size() + " files successfully");
        System.out.println("Failed on " + failures.size() + " files");
        System.out.println("\nDuration: " + (System.currentTimeMillis() - startTime) + " milliseconds");
        if (!failures.isEmpty()) System.exit(-1);
    }

    static void addPaths(Path path, List<Path> paths) throws IOException {
        Files.walk(path).forEach(p->{
            if (!Files.isDirectory(p)) {
                if (p.toString().endsWith(".kt")) {
                    paths.add(p);
                }
                else if (p.toString().endsWith(".jar") || p.toString().endsWith(".zip")) {
                    try {
                        FileSystem zfs = FileSystems.newFileSystem(p, (ClassLoader) null);
                        p = zfs.getRootDirectories().iterator().next();
                        addPaths(p, paths);
                    }
                    catch (IOException ioe) {
                        ioe.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        });
    }

    static public void parseFile(Path path) {
        try {
            KotlinParser parser = new KotlinParser(path);
            if (!quiet) System.out.print("Parsing " + path);
            parser.KotlinFile();
            Node root = parser.rootNode();
            if (retainInMemory) roots.add(root);
            if (paths.size()==1) {
                root.dump("");
            }
            if (!quiet) System.out.println(" Success");
            successes.add(path);
            if (successes.size() % 1000 == 0) {
                System.out.println("Successfully parsed " + successes.size() + " files...");
            }
        }
        catch (Throwable t) {
          failures.add(path);
          System.out.println(" Failed");
          if (paths.size()==1) {
              t.printStackTrace(System.out);
          }
        }
    }

    static public void usage() {
        System.out.println("Usage: java KParse <sourcefiles or directories>");
        System.out.println("If you just pass it one Kotlin source file, it dumps the AST");
        System.out.println("Use the -p flag to set whether to parse in multiple threads");
        System.out.println("Use the -q flag for quieter output");
        System.out.println("Use the -r flag to retain all the parsed AST's in memory");
        System.exit(0);
    }
}

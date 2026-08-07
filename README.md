# CongoCC grammar for Kotlin

This repository contains a CongoCC grammar for the [Kotlin programming language](https://kotlinlang.org).

The commands to download, build, and test it are:

```bash
git clone https://github.com/revusky/kotlin-parser
cd kotlin-parser
ant test
```
Requirements are Apache Ant and a JDK (Java 17 or higher). (Note that JDK 17 or higher is the requirement to run the CongoCC tool. The generated parser only requires a Java 8 or higher runtime.)

You can run the test harness against any set of Kotlin source files you want. Just:

```bash
cd build
java KParse directories_or_files
```

If you pass it a directory, it goes over the directory recursively and parses all the `.kt` files therein. If you pass it a single file, it dumps the AST on completion. If you pass it `-p` on the command line, the test harness works in multiple threads, which is a lot faster, though that depends, mostly on how many processor cores your machine has.

## Test Suite

As of this writing, the generated parser can parse *in their entirety* the following 4 codebases: [Ktor](https://github.com/ktorio/ktor), [Signal-Android](https://github.com/signalapp/Signal-Android), [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines), and [kotlinx.serialization](httpshttps://github.com/Kotlin/kotlinx.serialization://github.com/Kotlin/kotlinx.serialization). This comprises a test suite of 7,935 source files making up just over 1 million lines of code. On my own main work machine (nothing spectacular) it takes about 17 seconds to parse the entire test suite -- around 9 seconds if running multithreaded with the `-p` flag.

The generated parser is entirely self-contained. It has no dependencies other than the Java runtime.

## History

Initial work on this Kotlin grammar commenced on 31 July 2026 and the grammar was completed (in the sense of successfully parsing the entire test suite) on 7 August 2026. So far, this has been a one-man effort.

The best venue for discussion is https://discuss.congocc.org/


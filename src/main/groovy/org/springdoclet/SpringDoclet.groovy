package org.springdoclet

import com.sun.javadoc.Doclet
import com.sun.javadoc.RootDoc

class SpringDoclet extends Doclet {
  private static String OUTPUT_DIRECTORY = "-d"
  private static String[][] options

  public static boolean start(RootDoc root) {
    ErrorReporter.setErrorReporter(root)

    options = root.options()

    def outputFile = getOutputFile()

    def collectors = getCollectors()

    ClassProcessor processor = new ClassProcessor()
    processor.process root.classes(), collectors

    HtmlWriter writer = new HtmlWriter()
    writer.writeOutput outputFile, collectors

    return true
  }

  private static getCollectors() {
    return [ new ComponentCollector(), new RequestMappingCollector() ]
  }

  private static File getOutputFile() {
    File path = new File(getOption(OUTPUT_DIRECTORY) ?: '.')
    if (!path.exists())
      path.mkdirs()

    def file = new File(path, "spring-summary.html")
    file.delete()
    file.createNewFile()

    return file
  }

  public static int optionLength(String option) {
    if (option.equals(OUTPUT_DIRECTORY)) {
      return 2;
    }
    return 0;
  }

  private static String getOption(String optionName) {
    for (option in options) {
      if (option[0] == optionName) {
        return option[1];
      }
    }
    return null;
  }
}

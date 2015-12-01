def infile = new File(args[0])

def outfile;
def hasOut=false;

if (args.size() > 1 && args[1] != null) {
   outfile = new File(args[1]);
   hasOut=true;
} else {
   outfile = new File(args[0]);
   hasOut=true;
}

def lines = infile.readLines()
boolean first = true;

def sw = new StringWriter();
infile.eachLine() {line, lineNo ->
    if(line.trim().startsWith("public static ")
      && line.trim().endsWith(";")) {
       if (!line.contains("final ") && !line.contains("{")
          && !line.contains(",")) {
	    line = line.replace(" static ", " static final ");
            if (first) {
              println ("Changed " + infile.getAbsolutePath());
              first = false;
            }
       }
    }
   sw.append(line+'\n');
}

if (hasOut == true) {
   if (outfile.exists()) outfile.delete();
   outfile.withWriter('UTF-8') { writer ->
       writer.write(sw.toString())
  }
} else {
   println(sw.toString());
}


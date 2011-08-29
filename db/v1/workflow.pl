#!/usr/bin/perl 



$a = qq{
|          1 | ChIP SEQ                     | ChIP SEQ                     |
|          2 | Microarray ChIP              | Microarray ChIP              |
|          3 | Gene Expression SEQ          | Gene Expression SEQ          |
|          4 | Microarray Gene Expression   | Microarray Gene Expression   |
|          5 | HELP TAG                     | HELP TAG                     |
|          6 | Microarray HELP              | Microarray HELP              |
|          7 | CGH                          | CGH                          |
|          8 | RESEQUENCING                 | RESEQUENCING                 |
|          9 | AMPLICON SEQ                 | AMPLICON SEQ                 |
|         10 | SEQ CAP                      | SEQ CAP                      |
|         11 | Digital Expression Profiling | Digital Expression Profiling |
|         12 | RNA SEQ                      | RNA SEQ                      |
|         13 | miRNA SEQ                    | miRNA SEQ                    |
|         14 | CONTROL (SEQ)                | CONTROL (SEQ)                |
|         15 | CONTROL (MICROARRAY)         | CONTROL (MICROARRAY)         |
|         16 | DE NOVO SEQ                  | DE NOVO SEQ                  |
|         17 | MATE PAIR SEQ                | MATE PAIR SEQ                |
|         18 | DIRECTIONAL RNA SEQ          | DIRECTIONAL RNA SEQ          |
|         19 | OTHER SEQ (DNA SAMPLES)      | OTHER SEQ (DNA SAMPLES)      |
|         20 | OTHER SEQ (RNA SAMPLES)      | OTHER SEQ (RNA SAMPLES)      |
}; 

foreach $line (split /\n/, $a) {
  @a = split(/\s*\|\s*/, $line);
  next if ! $a[1];
  $name = "\L$a[2]";
  $name =~ s/[^A-Za-z ]//g;
  $name =~ s/ (.)/\U$1/g;

  print qq|update workflow set iname = '$name' where workflowid = $a[1];\n|;
}


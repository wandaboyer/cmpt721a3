Description Logic Concept Subsumption.
James Twigg, 301229679
Wanda Boyer, 301242166

==Input Format==
Well formed Brachman and Levesque DL formulae.
Note well that the format on page 185 of the text is inconsistent regarding roles.
Here, an ALL statement must be of the form
	[ALL :<role> <concept>]
whereas an EXISTS must be of the form
	[EXISTS <integer> <role>]
paying specific attention to the colon.

The statement type keywords "ALL", "AND" and "EXISTS" are case sensitive.
Atomic statements are also case sensitive; "Teacher" != "teacher".

It is recommended that a DL Statement should not span multiple lines.

Correctly formatted input files for the cases given on p. 185 of the text are supplied.

==Environment and Execution==
This program was written in Java 7 on Ubuntu 13.04.

To compile, extract the archive and invoke `javac -d . src/cmpt721A3/*.java`

To run, invoke `java cmpt721A3.Subsumption inputFile`

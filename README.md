# ExpOse
A tool for inferring worst-case time complexity by an automated empirical study

[![asciicast](https://asciinema.org/a/22969.png)](https://asciinema.org/a/22969)

To use ExpOse to analyze your own algorithm, extend the DoublingExperiment absract class.  
Provide a DoubleN method that causes the input size to double, and a TimedTest method that returns the runtime on the input.

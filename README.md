# ExpOse
A tool for inferring worst-case time complexity by an automated empirical study

[![asciicast](https://asciinema.org/a/22969.png)](https://asciinema.org/a/22969)

To use ExpOse to analyze your own algorithm, extend the ```DoublingExperiment``` abstract class.
Provide a ```DoubleN``` method that causes the input size to double, and a ```TimedTest``` method that returns the runtime on the input.

Here are a few commands to run ExpOse on some included sorting algorithms. FIrst, compile and set your classpath.
```
ant compile
export CLASSPATH="lib/*:bin:."
```
Run ExpOse on bubble sort:
```
java edu.allegheny.expose.examples.sort.SortingExperiment bubble
```
Use the ```--verbose``` flag for more output:
```
java edu.allegheny.expose.examples.sort.SortingExperiment bubble --verbose
```

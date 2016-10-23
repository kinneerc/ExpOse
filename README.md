# ExpOse
## Tuning Branch
The tuning branch is a feature branch that introduces two new features to ExpOse, with the idea of automatically arriving at sensible values for two of the tool's parameters: the number of trials run for each input size N, and the tolerance value that heuristically determines when the experiment should terminate.

To tune the tolerance value, a collection of example algorithms are evaluated using ExpOse for some `trials` number of times.  If ExpOse fails to arrive at the correct order of growth for a certain `accuracy` threshold, then the tolerance value is decreased, and the example algorithms are evaluated again.

To tune the number of trials, ExpOse calculates the runtime for the provided algorithm for some initial number of trials, and then computes the coefficient of variation of the runtime values. The number of trials is increased until the coefficient of variation falls below a certain threshold.

The usage of the tool is the same.

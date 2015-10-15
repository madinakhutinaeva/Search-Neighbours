# Neighbour search using kd-tree
KD-tree based implementation of neigbour search. Neighbour is defined as a point within two distances to the nearest neighbour point. 
KD-tree algorithm is from Algorithms 4th Edition by R. Sedgewick.

Usage:  
Programm accepts space separated values file as an argument.  
Example:
```
0.345535 0.34243
0.432434 0.343423
0.342344 0.102302
0.626840 0.032344
0.933343 0.34549
0.64888 0.94344
```

Output is list of 
point coordinates, distance to neareast neigbour, number of neigbours for this point.  
Example output:
```
(0.345535, 0.34243) radius: 0.0869046733495961  neighbours: 1
(0.342344, 0.102302) radius: 0.2401492012583011  neighbours: 3
(0.432434, 0.343423) radius: 0.0869046733495961  neighbours: 1
(0.62684, 0.032344) radius: 0.2929711517880216  neighbours: 4
(0.933343, 0.34549) radius: 0.4381831880903238  neighbours: 5
(0.64888, 0.94344) radius: 0.6378630505092767  neighbours: 5
```
 

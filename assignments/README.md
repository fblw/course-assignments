## Description

A small collection of algorithms and assignments implemented in university and onlince courses.

- algorithms/
    - dijkstra.py\*
    - kruskal.py\*
    - strassen.py\*
- mit/6.0001/
    - ps4/
    - ps5/

\* only with German comments
## Usage

### dijkstra.py

Finds the shortest path from a starting node to a target node. 

Download the file algorithms/dijkstra.py and execute:
```python
% python dijkstra.py

Dijkstra test case: start at 4, go to 2
Total cost:
7
```


### kruskal.py

Finds the minimum spanning tree (MST) for a given undirected weighted graph $G = (V,E,W)$. Returns the sum of the edges of the MST.

Download the file algorithms/kruskal.py and execute:
```python
% python kruskal.py

Kruskal Test
MST overall weight:
8
```

It will calculate the sum of all edges of the MST for one test case. 
The correspoding graph for the test case in matrix form is: 

$$\begin{matrix} 
    -1 & 8 & 2 & 1\\
    8 & -1 & 6 & 5\\
    2 & 6 & -1 & 3\\
    1 & 5 & 3 & -1
\end{matrix}$$

### strassen.py

Divide and conquer algorithm for matrix multiplication.

Download the file algorithms/strassen.py and execute:
```python
% python strassen.py

#### Matrizenmultiplikation nach Strassen ####

Parameter a:
|3 2 1|
|1 0 2|
Parameter b:
|1 2|
|0 1|
|4 0|

+ Implementierung nur für quadratsiche Matrizen definiert
 => Erweitere Matrizendimensionen auf nächste Zweierpotenz...
 => n=4
 => Befülle Matrizen mit Nullen...

Parameter a:
|3 2 1 0|
|1 0 2 0|
|0 0 0 0|
|0 0 0 0|
Parameter b:
|1 2 0 0|
|0 1 0 0|
|4 0 0 0|
|0 0 0 0|

+ Berechne rekursiv C...

|c11 c12|
|c21 c22|

...mit c11 = p + s - t + v
Parameter p:
|3 8|
|1 2|
Parameter s:
|0 0|
|0 0|
Parameter t:
|0 0|
|0 0|
Parameter v:
|4 0|
|8 0|
...mit c12 = r + t
Parameter r:
|0 0|
|0 0|
Parameter t:
|0 0|
|0 0|
...mit c21 = q + s
Parameter q:
|0 0|
|0 0|
Parameter s:
|0 0|
|0 0|
...mit c22 = p + r - q + u
Parameter p:
|3 8|
|1 2|
Parameter r:
|0 0|
|0 0|
Parameter q:
|0 0|
|0 0|
Parameter u:
|4 0|
|8 0|

 => c11, c12, c21, c22:

|7 8|
|9 2|

|0 0|
|0 0|

|0 0|
|0 0|

|0 0|
|0 0|

+ Ergebnismatrix vor Anpassung von Zeilen- und Spaltenanzahl:

|7 8 0 0|
|9 2 0 0|
|0 0 0 0|
|0 0 0 0|

+ Ergebnis der Multiplikation:

|7 8|
|9 2|
```

### Folders ps4, ps5

Folders with solutions to problem sets 4 and 5 for the [6.0001 Introduction to Computer Science and Programming in Python](https://ocw-origin.odl.mit.edu/courses/electrical-engineering-and-computer-science/6-0001-introduction-to-computer-science-and-programming-in-python-fall-2016/index.htm) course at MIT OpenCourseWare.
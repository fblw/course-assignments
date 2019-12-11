## Description

Some additional data structures in C and Java.

### Usage

[Linked list](https://en.wikipedia.org/wiki/Linked_list) in C

Download the file data-structures/linked_list.c and on MacOS first compile the source code and then run the executable file:

```c
% clang -Wall -o linked_list linked_list.c

% ./linked_list


#### TEST A ####
Deklarierte Liste: leer
Länge: 0

Füge Elemente zur Liste hinzu...
Initialisierte Liste: 5-432-1
Länge: 5

Entferne das erste Elemente...
Liste: -432-1

Entferne das Elemente an Position 2...
Liste: -43-1

Entferne das Elemente an Position 10...
Rückgabewert: -1
Liste: -43-1

Gib das Elemente an Position 0 zurück...
Erstes Element: -4

Entferne das Element 3...
Liste: -4-1

Entferne das Elemente -97...
Rückgabewert: -1
Liste: -4-1

Entferne alle Elemente...
Liste: leer


#### TEST B ####
Füge neue Elemente zur Liste hinzu...
Liste: -54-3-21

Sortiere Elemente mit m=0...
-5-3-214

Sortiere Elemente mit m=-1...
12345
```

[Circular buffer](https://en.wikipedia.org/wiki/Circular_buffer) in Java

Download the file data-structures/Ring.java and run:

```java
% java Ring.java

✔ Neuer Ring<Boolean> mit Capacity 20
✔ Neuer Ring<Integer> mit Capacity 4
✔ Neuer Ring<Double> mit Capacity 0

✔ Teste Methoden für Ring<Integer>

add	 : 	Ring(8)
add	 : 	Ring(8, 3)
add	 : 	Ring(8, 3, 5)
set	 : 	Ring(3, 5, 7)
add	 : 	Ring(3, 5, 7, 4)
size	 : 	4
back	 : 	Ring(4, 3, 5, 7)
back	 : 	Ring(7, 4, 3, 5)
remove	 : 	Ring(4, 3, 5)
remove	 : 	Ring(3, 5)
remove	 : 	Ring(5)
remove	 : 	Ring()
size	 : 	0

✔ Teste Methoden für Ring<Boolean>

add	 : 	Ring(true)
add	 : 	Ring(true, false)
size	 : 	2
back	 : 	Ring(false, true)
set	 : 	Ring(true, true)
remove	 : 	Ring(true)
remove	 : 	Ring()
size	 : 	0

✔ Teste Fehler in Methode für Ring<Double>

add	 : 	full ring throws IllegalArgumentException
remove	 : 	empty ring throws NoSuchElementException
back	 : 	empty ring throws NoSuchElementException
set	 : 	empty ring throws NoSuchElementException

✔ Neuer Ring<Ring<String>> mit Capacity 5x5

✔ Ausgabe für Ring<Ring<String>>

Ring(Ring(R0E0, R0E1, R0E2, R0E3, R0E4), Ring(R1E0, R1E1, R1E2, R1E3, R1E4), Ring(R2E0, R2E1, R2E2, R2E3, R2E4), Ring(R3E0, R3E1, R3E2, R3E3, R3E4), Ring(R4E0, R4E1, R4E2, R4E3, R4E4))

✔ Teste statische Methoden für Ring<Integer>

add X-mal	 : 	Ring(1, 2, 7, 4, 7, 8, 7)
removAll, 7	 : 	Ring(1, 2, 4, 8)
removAll, 1	 : 	Ring(2, 4, 8)
removAll, 4	 : 	Ring(2, 8)
removeAll, 2	 : 	Ring(8)

✔ Teste merge für zwei Ringe Ring<Integer>

Ring(11, 33, 4, 5, 6, 7, 8, 9, 10)
+Ring(48, 42, 12, 24, 36)
=Ring(11, 33, 4, 5, 6, 7, 8, 9, 10, 48, 42, 12, 24, 36)

```


% -*- Mode: Prolog -*-

mother(ann,henry).                 /** [M1] */
mother(ann,mary).                  /** [M2] */
mother(jane,mark).                 /** [M3] */
mother(jane,francis).              /** [M4] */
mother(annette,jonathan).          /** [M5] */
mother(mary,bill).                 /** [M6] */
mother(janice,louise).             /** [M7] */
mother(lucy,janet).                /** [M8] */
mother(louise,caroline).           /** [M9] */
mother(louise,martin).             /** [M10] */

father(henry,jonathan).            /** [F1] */
father(john,mary).                 /** [F2] */
father(francis,william).           /** [F3] */
father(francis,louise).            /** [F4] */
father(john,mark).                 /** [F5] */
father(gavin,lucy).                /** [F6] */
father(john,francis).              /** [F7] */
father(martin,david).              /** [F8] */
father(martin,janet).              /** [F9] */

parent(victoria,george).           /** [P1] */
parent(victoria,edward).           /** [P2] */
parent(X,Y):-write('mother?'),nl,mother(X,Y),
	     write('mother!'),nl.  /** [P3] */
parent(A,B):-write('father?'),nl,father(A,B),
	     write('father!'),nl.  /** [P4] */
parent(elizabeth,charles).         /** [P5] */
parent(elizabeth,andrew).          /** [P6] */

rich(jane).                        /** [R1] */
rich(john).                        /** [R2] */
rich(gavin).                       /** [R3] */
rich_father(X,Y):-rich(X),father(X,Y). /** [RF1] */


/** Usages:

1. Demonstration of backtracking

| ?- parent(john,Child),write('The child is '),write(Child),nl.
parent(john,Child),write('The child is '),write(Child),nl.
mother?
father?
father!
The child is mary

Child = mary ? ;
father!
The child is mark

Child = mark ? ;
father!
The child is francis

Child = francis

yes

===

| ?- rich_father(A,B).
rich_father(A,B).

A = john
B = mary ? ;

A = john
B = mark ? ;

A = john
B = francis ? ;

A = gavin
B = lucy

(1 ms) yes

*/

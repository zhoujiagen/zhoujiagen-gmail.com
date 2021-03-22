% -*- Mode: Prolog -*-

go:- X is 30, Y is 5, Z is X + Y + X * Y, write(Z).


increase(N, M) :- M is N+1.

/** Some examples:

| ?- X is 30,Y is 5,Z is X+Y+X*Y.
X is 30,Y is 5,Z is X+Y+X*Y.

X = 30
Y = 5
Z = 185

yes
| ?- X is 7,X is 6+1.
X is 7,X is 6+1.

X = 7

yes
| ?- 10 is 7+13-11+9.
10 is 7+13-11+9.

no
| ?- 18 is 7+13-11+9.
18 is 7+13-11+9.

===

| ?- increase(4,X).
increase(4,X).

X = 5

yes


*/

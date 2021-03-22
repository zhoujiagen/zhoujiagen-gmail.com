% -*- Mode: Prolog -*-

/****************************************************************************
* 扩展Prolog


op(700,xfx,iss).

阶乘: fac/1
op(150,xf,fac).

平方: ** /2
op(120,yfx,**).

****************************************************************************/

factorial(1,1) :- !.
factorial(N,Y) :- N1 is N-1, factorial(N1, Y1), Y is N*Y1.

Y iss N fac :- N1 iss N, factorial(N1, Y), !.
Y iss A+B :- Y is A+B, !.
Y iss A-B :- Y is A-B, !.
Y iss A*B :- Y is A*B, !.
Y iss A/B :- Y is A/B, !.
Y iss A//B :- Y is A//B, !.
Y iss A^B :- Y is A^B, !.
Y iss +A :- Y is +A, !.
Y iss -A :- Y is -A, !.
Y iss sqrt(A) :- A1 iss A, Y is sqrt(A1), !.
Y iss A**B :- A1 iss A, B1 iss B, Y is A1*A1+B1*B1, !.
Y iss X :- Y is X, !.     /** catch all caluse. */

/**
| ?- Y iss 6+4*3-2.
Y iss 6+4*3-2.

Y = 16

yes
| ?- Y iss 6+sqrt(25)-2.
Y iss 6+sqrt(25)-2.

Y = 9.0

(1 ms) yes

===
| ?- Y iss 6 fac.
Y iss 6 fac.

Y = 720

yes
| ?- Y iss (3+2) fac.
Y iss (3+2) fac.

Y = 120

yes

===
| ?- Y iss 3**2.
Y iss 3**2.
uncaught exception: error(existence_error(procedure,(iss)/2),top_level/0)
| ?- ['extensions.pl'].
['extensions.pl'].
compiling /Users/zhoujiagen/workspace/polyglot-languages/prolog/extensions.pl for byte code...
/Users/zhoujiagen/workspace/polyglot-languages/prolog/extensions.pl compiled, 67 lines read - 4835 bytes written, 6 ms

(1 ms) yes
| ?- Y iss 3**2.
Y iss 3**2.

Y = 9.0

yes

===
| ?- Y iss (3 fac)**(4 fac).
Y iss (3 fac)**(4 fac).

Y = 612

yes


*/






% -*- Mode: Prolog -*-

/** 谓词转换为操作符. */


/** execute in ?-
op(150, xfy, likes).
op(150, xf, is_female).
op(150, xf, isa_cat).
op(150, xfy, owns).
*/

john likes X:- X is_female, X owns Y, Y isa_cat.

is_female(mary).
owns(mary, fido).
isa_cat(fido).

/**

| ?- john likes X.
john likes X.

X = mary

yes
*/


test(X) :- X > 0, write(positive), nl.
test(0) :- write(zero), nl.
test(X) :- X < 0, write(negative), nl.

/**

| ?- test(1).
test(1).
positive

true ? 


yes
| ?- test(0).
test(0).
zero

true ? 


yes
| ?- test(-1).
test(-1).
negative

yes

*/


/** Relational Operators:

数值相等性和比较: =:=, =\=, >, >=, <, =<

| ?- 88+15-3 =:= 110-5*2.
88+15-3 =:= 110-5*2.

yes
| ?- 100 =\= 99.
100 =\= 99.

yes
| ?- 100 > 99.
100 > 99.

yes
| ?- 100 >= 99.
100 >= 99.

yes
| ?- 100 < 99.
100 < 99.

no
| ?- 100 =< 99.
100 =< 99.

no

项相等性: ==, \==

| ?- likes(X, prolog) == likes(X, prolog).
likes(X, prolog) == likes(X, prolog).

yes

| ?- likes(X, prolog) == likes(Y, prolog).
likes(X, prolog) == likes(Y, prolog).

no
| ?- x == x.
x == x.

yes
| ?- x == y.
x == y.

no

| ?- X == 0.
X == 0.

no
| ?- 6+4 == 3+7.
6+4 == 3+7.

no

no
| ?- pred1(X) \== pred1(X).
pred1(X) \== pred1(X).

no
| ?- pred1(X) \== pred1(Y).
pred1(X) \== pred1(Y).

yes

*/


checkeven(N):- M is N // 2, N =:= 2*M.

/**
| ?- checkeven(12).
checkeven(12).

yes
| ?- checkeven(23).
checkeven(23).

no
*/


/** 逻辑操作符

not provable: \+

| ?- \+ isa_cat(fido).
\+ isa_cat(fido).

no
| ?- \+ isa_cat(fred).
\+ isa_cat(fred).

yes
| ?- X = 0, \+ X is 0.
X = 0, \+ X is 0.

no
| ?- X = 0, X is 0.
X = 0, X is 0.

X = 0

(1 ms) yes


yes
| ?- \+ isa_cat(fred).
\+ isa_cat(fred).

yes
| ?- \+ \+ isa_cat(fred).
\+ \+ isa_cat(fred).

no
| ?- \+ \+ \+ isa_cat(fred).
\+ \+ \+ isa_cat(fred).

yes

*/


/** 分号操作符: ;

| ?- 6 < 3; 7 is 5 + 2.
6 < 3; 7 is 5 + 2.

yes
| ?- 6*6 =:= 36; 10 = 8+3.
6*6 =:= 36; 10 = 8+3.

true ? 


yes

*/

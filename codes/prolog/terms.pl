% -*- Mode: Prolog -*-

/****************************************************************************
* 处理项
****************************************************************************/

/** =../1: 列表与项之间的转换

| ?- X=..[colour, red].
X=..[colour, red].

X = colour(red)

yes
| ?- data(6,green,mypred(24,blue)) =.. L.
data(6,green,mypred(24,blue)) =.. L.

L = [data,6,green,mypred(24,blue)]

yes
*/

/** call/1

| ?- call(write('hello world')).
call(write('hello world')).
hello world

yes

*/

/** functor/3

| ?- functor(city(london,england,europe),Function,Arity).
functor(city(london,england,europe),Function,Arity).

Arity = 3
Function = city

yes
*/

/** arg/3

| ?- arg(3,person(mary,jones,doctor,london),X).
arg(3,person(mary,jones,doctor,london),X).

X = doctor

yes

*/


unify(CT1,CT2) :-
    functor(CT1,Funct1,Arity1),
    functor(CT2,Funct2,Arity2),
    compare(CT1,CT2,Funct1,Arity1,Funct2,Arity2).

compare(CT1,CT2,F,0,F,0).                      /* Same atom*/
compare(CT1,CT2,F,0,F1,0) :- fail.             /* Different atoms */
compare(CT1,CT2,F,A,F,A) :- unify2(CT1,CT2),!.
compare(CT1,CT2,_,_,_,_) :- fail.

unify2(CT1,CT2) :- CT1=..[F|L1],CT2=..[F|L2],!,paircheck(L1,L2).

paircheck([],[]).
paircheck([A|L1],[A|L2]) :- paircheck(L1,L2).

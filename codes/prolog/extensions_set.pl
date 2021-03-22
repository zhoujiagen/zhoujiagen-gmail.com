% -*- Mode: Prolog -*-

/****************************************************************************
* 扩展Prolog: 集合

op(710,xfx,sis).
op(200,yfx,and).
op(200,yfx,or).
****************************************************************************/


Y sis A and B :- findall(X, (member(X,A), member(X,B)), Y), !.
Y sis A or B :- findall(X, (member(X,A); member(X,B)), Y), !.
/** not 改为使用 \+ */
Y sis A - B :- findall(X, (member(X,A), \+(member(X,B))), Y), !.

/**
| ?- X=[a,b,c,d],Y=[e,b,f,c,g], A sis X and Y, B sis X or Y, C sis X - Y.
X=[a,b,c,d],Y=[e,b,f,c,g], A sis X and Y, B sis X or Y, C sis X - Y.

A = [b,c]
B = [a,b,c,d,e,b,f,c,g]
C = [a,d]
X = [a,b,c,d]
Y = [e,b,f,c,g]

yes

*/

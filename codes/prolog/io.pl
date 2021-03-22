% -*- Mode: Prolog -*-


/** 输出: write/1, nl/0, writeq/1

| ?- write(26), nl.
write(26), nl.
26

yes
| ?- write('26'), nl.
write('26'), nl.
26

yes
| ?- writeq(26), nl.
writeq(26), nl.
26

yes
| ?- writeq('26'), nl.
writeq('26'), nl.
'26'

yes
*/


/** 读取项: read/1

| ?- read(X).
read(X).
jim.
jim.

X = jim

yes
| ?- X=fred, read(X).
X=fred, read(X).
fred.
fred.

X = fred

yes
*/


/** 输出和读取字符: put/1, get0/1, get/1

| ?- put(97).
put(97).
a

yes
| ?- put(122).
put(122).
z

yes


| ?- get0(N).
get0(N).
a
a

N = 97

yes
| ?- get(N).
get(N).
         a
         a

N = 97

yes


*/


go(Total) :- count(0, Total).
count(OldCount, Result) :- get0(X), process(X, OldCount, Result).
process(42, OldCount, OldCount). /** 遇到字符: *. */
process(X, OldCount, Result) :- X =\= 42, New is OldCount+1, count(New, Result).

/**
| ?- go(T).
go(T).
The time has come the walrus said*
The time has come the walrus said*

T = 33 ? 

yes
| ?- go(T).
go(T).
*
*

T = 0 ? 

yes
*/

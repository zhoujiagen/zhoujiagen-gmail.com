% -*- Mode: Prolog -*-

/****************************************************************************
* 修改Prolog数据库: 添加和删除子句.
*
* assertz/1, asserta/1
* retract/1, retractall/1
****************************************************************************/

/**

assertz(mypred(first)).
assertz(mypred(second)).
assertz(mypred(third)).
assertz(mypred(fourth)).

listing(mypred).

asserta(mypred(new1)).
listing(mypred).

mypred(X).

retract(mypred(first)).
listing(mypred).

retractall(mypred(_)).
listing(mypred).

===

uncaught exception: error(existence_error(procedure,dynamic/1),top_level/0)
| ?- assertz(mypred(first)).
assertz(mypred(first)).

yes
| ?- assertz(mypred(second)).
assertz(mypred(second)).

yes
| ?- assertz(mypred(third)).

assertz(mypred(third)).

yes
| ?- assertz(mypred(fourth)).
assertz(mypred(fourth)).

yes
| ?- listing(mypred).
listing(mypred).

% file: user_input

mypred(first).
mypred(second).
mypred(third).
mypred(fourth).

yes
| ?- asserta(mypred(new1)).
asserta(mypred(new1)).

yes
| ?- listing(mypred).
listing(mypred).

% file: user_input

mypred(new1).
mypred(first).
mypred(second).
mypred(third).
mypred(fourth).

yes
| ?- mypred(X).
mypred(X).

X = new1 ? ;

X = first ? ;

X = second ? ;

X = third ? ;

X = fourth

yes
| ?- retract(mypred(first)).
retract(mypred(first)).

(1 ms) yes
| ?- listing(mypred).
listing(mypred).

% file: user_input

mypred(new1).
mypred(second).
mypred(third).
mypred(fourth).

yes
| ?- retractall(mypred(_)).

retractall(mypred(_)).

yes
| ?- listing(mypred).
listing(mypred).

% file: user_input


yes

*/

setup :- seeing(S), see('dbs_input.txt'),
	 read_data,
	 write('Data read'), nl,
	 seen, see(S).
read_data :- read(A), process(A).
process(end).
process(A) :- read(B), read(C), read(D), read(E),
	      assertz(person(A,B,C,D,E)), read_data.

/**
| ?- setup.
setup.
Data read

true ? 


yes
| ?- listing(person).
listing(person).

% file: user_input

person(john, smith, 45, london, doctor).
person(martin, williams, 33, birmingham, teacher).
person(henry, smith, 26, manchester, plumber).
person(jane, wilson, 62, london, teacher).
person(mary, smith, 29, glasgow, surveyor).

yes 
*/

remove(Forename, Surname) :- retract(person(Forename, Surname,_,_,_)).

/**
| ?- remove(henry, smith).
remove(henry, smith).

yes
| ?- listing(person).
listing(person).

% file: user_input

person(john, smith, 45, london, doctor).
person(martin, williams, 33, birmingham, teacher).
person(jane, wilson, 62, london, teacher).
person(mary, smith, 29, glasgow, surveyor).

yes
*/

change(Forename, Surname, New_Profession) :-
    person(Forname, Surname, Age, City, Profession),
    retract(person(Forename, Surname, Age, City, Profession)),
    assertz(person(Forname, Surname, Age, City, New_Profession)).

/**
| ?- listing(person).
listing(person).

% file: user_input

person(john, smith, 45, london, doctor).
person(martin, williams, 33, birmingham, teacher).
person(henry, smith, 26, manchester, plumber).
person(jane, wilson, 62, london, teacher).
person(mary, smith, 29, glasgow, surveyor).

yes
| ?- change(jane, wilson, architect).
change(jane, wilson, architect).

true ? 


yes
| ?- listing(person).
listing(person).

% file: user_input

person(john, smith, 45, london, doctor).
person(martin, williams, 33, birmingham, teacher).
person(henry, smith, 26, manchester, plumber).
person(mary, smith, 29, glasgow, surveyor).
person(jane, wilson, 62, london, architect).

yes
*/


output_data :-
    telling(T), tell('dbs_output.txt'),
    write_data, told, tell(T),
    write('Data written'), nl.
write_data :- person(A,B,C,D,E),
	      write(A), write('. '),
	      write(B), write('. '),
	      write(C), write('. '),
	      write(D), write('. '),
	      write(E), write('.'), nl,
	      fail.
write_data :- write('end.'), nl.

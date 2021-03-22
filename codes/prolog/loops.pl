% -*- Mode: Prolog -*-


/****************************************************************************
* 固定次数
****************************************************************************/

loop(0).
loop(N) :- N > 0, write('The value is: '), write(N), nl,
	   M is N-1, loop(M).

/**

| ?- loop(3).
loop(3).
The value is: 3
The value is: 2
The value is: 1

true ? 


(1 ms) yes
*/

output_values(Last, Last) :- write(Last), nl,
			     write('end of example'), nl.
output_values(First, Last) :- First =\= Last, write(First), nl,
			      N is First+1, output_values(N, Last).

/**
| ?- output_values(1,3).
output_values(1,3).
1
2
3
end of example

true ? 


yes
*/


sum(1, 1).
sum(N, Sum) :- N > 1, N1 is N-1, sum(N1, S1), Sum is S1+N.

/**
| ?- sum(100, N).
sum(100, N).

N = 5050 ? 


yes
*/


write_squares(1) :- write(1), nl.
write_squares(N) :- N > 1, N1 is N-1, write_squares(N1), Nsq is N*N, write(Nsq), nl.

/**
| ?- write_squares(5).
write_squares(5).
1
4
9
16
25

true ? 


(1 ms) yes
*/

/****************************************************************************
* 直到条件满足
****************************************************************************/

/** 使用递归. */

echo_start :- echo_loop(start).
echo_loop(end).
echo_loop(X) :- X \= end, write('Type end to end: '), read(Word),
		write('Input was '), write(Word), nl,
		echo_loop(Word).

/**
| ?- echo_start.
echo_start.
Type end to end: jiangsu.
jiangsu.
Input was jiangsu
Type end to end: end.
end.
Input was end

true ? 

yes
*/


valid(yes).
valid(no).

get_answer(Ans) :- write('Enter answer to question'), nl,
		   get_answer2(Ans).
get_answer2(Ans) :- write('answer yes or no: '),
		    read(A),
		    /** 使用分号: 并操作符. */
		    (
			(valid(A), Ans=A, write('Answer is '), write(A), nl);
			get_answer2(Ans)
		    ).

/**
| ?- get_answer(Ans).
get_answer(Ans).
Enter answer to question
answer yes or no: Yes.
Yes.
Answer is yes

Ans = yes ? 

yes
| ?-  get_answer(Ans).
 get_answer(Ans).
Enter answer to question
answer yes or no: noop.
noop.
answer yes or no: no.
no.
Answer is no

Ans = no ? 

(1 ms) yes
*/


/** 使用谓词repeat. 

调用时总是成功的; 回溯时也总是成功.
将求值目标的顺序从 '从右至左'(回溯时) 改为 '从左至右'.
*/

get_asnwer_repeat(Ans) :-
    write('Enter answer to question'), nl,
    repeat, write('answer yes or no: '), read(Ans),
    valid(Ans), write('Answer is '), write(Ans), nl.

/**
| ?- get_asnwer_repeat(Ans).
get_asnwer_repeat(Ans).
Enter answer to question
answer yes or no: noop.
noop.
answer yes or no: no.
no.
Answer is no

Ans = no ? 

yes
*/

/** 演示菜单功能. */
menu :- nl, write('MENU'), nl,
	write('a. A'), nl,
	write('b. B'), nl,
	write('c. C'), nl,
	write('d. End'), nl,
	read(Choice), nl, choice(Choice).

choice(a) :- write('A chosen'), menu.
choice(b) :- write('B chosen'), menu.
choice(c) :- write('C chosen'), menu.
choice(d) :- write('Goodbye!'), nl.
choice(_) :- write('Try again'), menu.

/**
| ?- menu.
menu.

MENU
a. A
b. B
c. C
d. End
x
x
.
.

Try again
MENU
a. A
b. B
c. C
d. End
a.
a.

A chosen
MENU
a. A
b. B
c. C
d. End
d.
d.

Goodbye!

true ? 

yes
*/

/****************************************************************************
* 失败时回溯
****************************************************************************/

/** 搜索Prolog数据库. */

dog(fido).
dog(fred).
dog(jonathan).

/** 使用fail强制回溯. */
alldogs :- dog(X), write(X), write(' is a dog'), nl,
	   fail.
alldogs.

/**
| ?- alldogs.
alldogs.
fido is a dog
fred is a dog
jonathan is a dog

yes
*/

/** 过滤. */
person(john,smith,45,london,doctor).
person(martin,williams,33,birmingham,teacher).
person(henry,smith,26,manchester,plumber).
person(jane,wilson,62,london,teacher).
person(mary,smith,29,glasgow,surveyor).

allteachers :- person(Forename, Surname, _, _, teacher),
	       write(Forename), write(' '), write(Surname), nl,
	       fail.
allteachers.

/**
| ?- allteachers.
allteachers.
martin williams
jane wilson

yes
*/

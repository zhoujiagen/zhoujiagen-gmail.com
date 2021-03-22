% -*- Mode: Prolog -*-

/****************************************************************************
* 字符串处理 
****************************************************************************/

/** 字符串与列表: name/2. */

/**
| ?- name('Prolog Example', L).
name('Prolog Example', L).

L = [80,114,111,108,111,103,32,69,120,97,109,112,108,101]

yes
| ?- L= [80,114,111,108,111,103,32,69,120,97,109,112,108,101], name(A, L). 
L= [80,114,111,108,111,103,32,69,120,97,109,112,108,101], name(A, L). 

A = 'Prolog Example'
L = [80,114,111,108,111,103,32,69,120,97,109,112,108,101]

yes
*/

/** 拼接字符串. */

join2(S1,S2,NewS) :-
    name(S1,L1), name(S2,L2),
    append(L1,L2,L),
    name(NewS,L).

/**
| ?- join2('prolog', 'example', S).
join2('prolog', 'example', S).

S = prologexample

yes
*/

join3(S1,S2,S3,NewS) :-
    join2(S1,S2,S),
    join2(S,S3,NewS).

/**
| ?- join3('This is ', 'an ', 'example', S). 
join3('This is ', 'an ', 'example', S). 

S = 'This is an example'

yes
*/


/** 移除字符串空白. */

/**
| ?- name(' ', L).
name(' ', L).

L = [32]

yes
*/


/* 移除左端空白. */
trim_l([A|L], L1) :- A =< 32, trim_l(L, L1).
trim_l([A|L], [A|L]) :- A > 32.

/** 移除右端空白. */
trim_r(L, L1) :- reverse(L, LRev), trim_l(LRev, L2), reverse(L2, L1).

trim_lr(L, L1) :- trim_l(L, L2), trim_r(L2, L1).

trim(S, NewS) :- name(S,L), trim_lr(L, L1), name(NewS, L1).

/**
| ?- trim('     hello world       ', S).
trim('     hello world       ', S).

S = 'hello world' ? 


yes
*/

/** 构造字符串. */

/**
| ?- name(S, [10]).
name(S, [10]).

S = '\n'

yes
*/
read_line(S) :- read_line1([], L), name(S, L), !.
read_line1(OldL, L) :- get0(X), process(OldL, X, L).
process(OldL, 10, OldL).
process(OldL, X, L) :- append(OldL, [X], L1), read_line1(L1, L).

/**
| ?- read_line(S).
read_line(S).
this is an example ,.C*-/#@ - Note no quotes needed and no final full stop
this is an example ,.C*-/#@ - Note no quotes needed and no final full stop

S = 'this is an example ,.C*-/#@ - Note no quotes needed and no final full stop'

(1 ms) yes
*/

read_file_line(File, S) :- see(File), read_line1([], L), name(S,L), !, seen.

/**
| ?- read_file_line('strings_input.txt', S).
read_file_line('strings_input.txt', S).

S = 'This is an example of'

yes
*/

/** 搜索和拆分字符串. */

separate(L, Before, After) :- append(Before, [32|After], L), !.
find_next(L) :- separate(L,Before,After), proc(Before,After).
find_next(L) :- write('Last item is '), name(S,L), write(S), nl.
proc(Before, After) :- write('Next item is '), name(S, Before), write(S), nl,
		       find_next(After).
split_up(S) :- name(S,L), find_next(L).

/**

| ?-  separate([26,42,32,18,56,32,19,24],Before,After).
 separate([26,42,32,18,56,32,19,24],Before,After).

After = [18,56,32,19,24]
Before = [26,42]

yes
| ?- separate([24,98,45,72],Before,After).
separate([24,98,45,72],Before,After).

no


uncaught exception: error(existence_error(procedure,splitup/1),top_level/0)
| ?- split_up('The time has come the walrus said').
split_up('The time has come the walrus said').
Next item is The
Next item is time
Next item is has
Next item is come
Next item is the
Next item is walrus
Last item is said

true ? 


yes
*/

start_list(L1,L2) :- append(L1, X, L2).                  /** L1是否在L2中头部出现. */
included_list(L1, []) :- !, fail.
included_list(L1, L2) :- start_list(L1, L2).
included_list(L1, [A|L2]) :- included_list(L1, L2).

check_it(L, PList, present) :- included_list(PList, L).  /** PList是否在L中出现. */
check_it(_,_,absent).

check_prolog(X) :- read_line(S), name(S,L),
		   name('Prolog', PList), check_it(L, PList, X), !.

/**
| ?- check_prolog(X).
check_prolog(X).
Logic Programming with Prolog
Logic Programming with Prolog

X = present

yes
| ?-  check_prolog(X).
 check_prolog(X).
Mercury Venus Earth Mars Jupiter Saturn Uranus Neptune Pluto
Mercury Venus Earth Mars Jupiter Saturn Uranus Neptune Pluto

X = absent

yes
*/


/**
split/4: split(S, Separator, Left, Right)

*/

split(S, Sep, Sep, Right) :- name(Sep, L1), name(S, L3), append(L1,L2,L3), name(Right, L2), !.
split(S, Sep, Left, Sep) :- name(Sep, L2), name(S, L3), append(L1,L2,L3), name(Left, L1), !.
split(S, Sep, Left, Right) :- name(S, L3), append(Lleft, Lrest, L3),
			      name(Sep, L4), append(L4, Lright, Lrest),
			      name(Left, Lleft),
			      name(Right, Lright), !.
split(S,_,S,'') :- !.

/**

字符串以分隔符开始
| ?- split('my name is John Smith','my name is ',Left,Right).
split('my name is John Smith','my name is ',Left,Right).

Left = 'my name is '
Right = 'John Smith'

yes

字符串以分隔符结束
| ?- split('my name is John Smith','John Smith',Left,Right).
split('my name is John Smith','John Smith',Left,Right).

Left = 'my name is '
Right = 'John Smith'

yes

分隔符在字符串中多次出现
| ?- split('my name is my name is John Smith','is',Left,Right).
split('my name is my name is John Smith','is',Left,Right).

Left = 'my name '
Right = ' my name is John Smith'

yes

字符串中无分隔符
| ?- split('my name is John Smith','Bill Smith',Left, Right).
split('my name is John Smith','Bill Smith',Left, Right).

Left = 'my name is John Smith'
Right = ''

(1 ms) yes
*/

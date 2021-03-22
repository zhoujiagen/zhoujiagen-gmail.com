% -*- Mode: Prolog -*-

/**
tell/1, told/0, telling/1
see/1, seen/0, seeing/1
*/

/** 10为行结束或文件记录结束字符值.  */
readline :- get0(X), process(X).
process(10).
process(X) :- X =\= 10, put(X), nl, readline.

/**
(1 ms) yes
| ?- readline.
readline.
Prolog test
Prolog test
P
r
o
l
o
g
 
t
e
s
t
*/


read_terms(InFile, OutFile) :-
    see(InFile), tell(OutFile),
    read(T1), write(T1), nl,
    read(T2), write(T2), nl,
    seen, told.

/**

*_input.txt为已有文件, *_output.txt为输出时创建的新文件.

| ?- read_terms('io_file_term_input.txt', 'io_file_term_output.txt').
read_terms('io_file_term_input.txt', 'io_file_term_output.txt').

yes
*/

/** restore io stream: S, T. */
read_terms_with_restore(InFile, OutFile) :-
    seeing(S), see(InFile),
    telling(T), tell(OutFile),
    read(T1), write(T1), nl,
    read(T2), write(T2), nl,
    seen, see(S), told, tell(T).

/**
| ?-  read_terms_with_restore('io_file_term_input.txt', 'io_file_term_output.txt').
 read_terms_with_restore('io_file_term_input.txt', 'io_file_term_output.txt').

yes
*/

/** 拷贝输入到文件中. */
copy_chars(OutFile) :- telling(T), tell(OutFile),
		       copy_characters, told, tell(T).
copy_characters :- get0(N), cc_process(N).
cc_process(33). /** 遇到字符: !. */
cc_process(N) :- N =\= 33, put(N), copy_characters.

/**
| ?- copy_chars('io_file_copy_chars_output.txt').
copy_chars('io_file_copy_chars_output.txt').
abcxyz!
abcxyz!

true ? 

yes
*/

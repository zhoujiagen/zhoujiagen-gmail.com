% -*- Mode: Prolog -*-

/** 从已有文件读取. */
go_file(Vowels) :- see('io_vowel_input.txt'), count(0, Vowels), seen.

go(Vowels) :- count(0, Vowels).
count(OldVowels, TotalVowels) :- get0(X), process(X, OldVowels, TotalVowels).

process(42, OldVowels, OldVowels).  /** 遇到字符: *. */
process(X, OldVowels, TotalVowels) :-
    X =\= 42, processChar(X, OldVowels, New), count(New, TotalVowels).

processChar(X, OldVowels, New) :- vowel(X), New is OldVowels+1.
processChar(X, OldVowels, OldVowels).

vowel(65).   /* A */
vowel(69).   /* E */
vowel(73).   /* I */
vowel(79).   /* O */
vowel(85).   /* U */
vowel(97).   /* a */
vowel(101).  /* e */
vowel(105).  /* i */
vowel(111).  /* o */
vowel(117).  /* u */


/**

| ?- go(Vowels).
go(Vowels).
In the beginning was the word*
In the beginning was the word*

Vowels = 8 ? 

yes
| ?- go(Vowels).
go(Vowels).
pqrst*
pqrst*

Vowels = 0 ? 

yes
*/

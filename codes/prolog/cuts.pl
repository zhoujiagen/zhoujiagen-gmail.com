% -*- Mode: Prolog -*-

/****************************************************************************
* 避免回溯: !/0

首次求值时总是成功的; 回溯时总是失败的.

****************************************************************************/

larger_incorrect(A,B,A) :- A > B.
larger_incorrect(A,B,B).

larger(A,B,A) :- A > B, !.
larger(A,B,B).

/**
uncaught exception: error(existence_error(procedure,larger_incorrec/3),top_level/0)
| ?- larger_incorrect(8,6,X).
larger_incorrect(8,6,X).

X = 8 ? ;

X = 6

yes
| ?- larger(8,6,X).
larger(8,6,X).

X = 8

yes
*/


bird(sparrow).
bird(eagle).
bird(duck).
bird(crow).
bird(ostrich).    /** 鸵鸟. */
bird(puffin).
bird(swan).
bird(albatross).
bird(starling).
bird(owl).
bird(kingfisher).
bird(thrush).

can_fly(ostrich) :- !, fail. /** 鸵鸟不会飞. */
can_fly(X) :- bird(X).

/**
| ?- can_fly(ostrich).
can_fly(ostrich).

no
| ?- can_fly(sparrow).
can_fly(sparrow).

yes
*/


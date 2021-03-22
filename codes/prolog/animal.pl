% -*- Mode: Prolog -*-

dog(fido).
dog(rover).
dog(henry).
cat(felix).
cat(michael).
cat(jane).
animal(X):-dog(X).

/**

| ?- dog(X).
dog(X).

X = fido ? ;

X = rover ? ;

X = henry

yes
| ?- animal(fido).
animal(fido).

yes
| ?- animal(felix).
animal(felix).

no


| ?- listing(dog).
listing(dog).

% file: /Users/zhoujiagen/workspace/polyglot-languages/prolog/animal.pl

dog(fido).
dog(rover).
dog(henry).

yes

*/

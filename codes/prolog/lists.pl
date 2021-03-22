% -*- Mode: Prolog -*-

/****************************************************************************
* 列表处理 
****************************************************************************/


/** Examples:
| ?- X=alpha,Y=27,Z=[alpha,beta],write('List is: '),write([X,Y,Z]),nl.
X=alpha,Y=27,Z=[alpha,beta],write('List is: '),write([X,Y,Z]),nl.
List is: [alpha,27,[alpha,beta]]

X = alpha
Y = 27
Z = [alpha,beta]

yes
| ?- write([alpha|[beta,gamma,delta]]),nl.
write([alpha|[beta,gamma,delta]]),nl.
[alpha,beta,gamma,delta]

yes
| ?- write([alpha,beta,gamma|[delta]]),nl.
write([alpha,beta,gamma|[delta]]),nl.
[alpha,beta,gamma,delta]

yes
*/


/** 分解列表. */
writeall([]).
writeall([A|L]) :- write(A), nl, writeall(L).

/**
| ?- writeall([alpha,'this is a string',20,[a,b,c]]).
writeall([alpha,'this is a string',20,[a,b,c]]).
alpha
this is a string
20
[a,b,c]

yes
*/


write_english([]).
write_english([[City, england]|L]) :- write(City), nl,
				     write_english(L).
write_english([_|L]) :- write_english(L).

/**
| ?- write_english([[london,england],[paris,france],[berlin,germany],[portsmouth,england],
[bristol,england],[edinburgh,scotland]]).
write_english([[london,england],[paris,france],[berlin,germany],[portsmouth,england],
[bristol,[bristol,england],[edinburgh,scotland]]).
london
portsmouth
bristol

true ? 


yes
*/


replace([_|L], [first|L]).

/**
| ?- replace([1,2,3,4,5],L).
replace([1,2,3,4,5],L).

L = [first,2,3,4,5]

yes
| ?- replace([[a,b],[c,d],[e,f,g]], L).
replace([[a,b],[c,d],[e,f,g]], L).

L = [first,[c,d],[e,f,g]]

yes
*/


/** member/2

| ?- member(a, [a,b,c]).
member(a, [a,b,c]).

true ? 


yes
| ?- member(mypred(a,b,c),[q,r,s,mypred(a,b,c),w]).
member(mypred(a,b,c),[q,r,s,mypred(a,b,c),w]).

true ? 


yes
| ?- member(x,[]).
member(x,[]).

no
| ?- member([1,2,3],[a,b,[1,2,3],c]).
member([1,2,3],[a,b,[1,2,3],c]).

true ? 


yes
| ?- member(X,[a,b,c]).
member(X,[a,b,c]).

X = a ? ;

X = b ? ;

X = c

(1 ms) yes
*/


/** length/2

| ?- length([a,b,c,d],X).
length([a,b,c,d],X).

X = 4

yes
| ?- length([[a,b,c],[d,e,f],[g,h,i]],L).
length([[a,b,c],[d,e,f],[g,h,i]],L).

L = 3

yes
| ?- length([], L).
length([], L).

L = 0

yes
| ?- length([a,b,c],3).
length([a,b,c],3).

yes
| ?- length([a,b,c],4).
length([a,b,c],4).

no
| ?- N is 3,length([a,b,c],N).
N is 3,length([a,b,c],N).

N = 3

yes
*/


/** reverse/2

| ?- reverse([1,2,3,4],L).
reverse([1,2,3,4],L).

L = [4,3,2,1]

yes
| ?- reverse(L,[1,2,3,4]).
reverse(L,[1,2,3,4]).

L = [4,3,2,1]

yes
| ?- reverse([[dog,cat],[1,2],[bird,mouse],[3,4,5,6]],L).
reverse([[dog,cat],[1,2],[bird,mouse],[3,4,5,6]],L).

L = [[3,4,5,6],[bird,mouse],[1,2],[dog,cat]]

yes
| ?- reverse([1,2,3,4],[4,3,2,1]).
reverse([1,2,3,4],[4,3,2,1]).

yes
| ?- reverse([1,2,3,4],[3,2,1]).
reverse([1,2,3,4],[3,2,1]).

no
*/


/** append/3

| ?- append([1,2,3,4],[5,6,7,8,9],L).
append([1,2,3,4],[5,6,7,8,9],L).

L = [1,2,3,4,5,6,7,8,9]

yes
| ?- append([],[1,2,3],L).
append([],[1,2,3],L).

L = [1,2,3]

yes
| ?- append([[a,b,c],d,e,f],[g,h,[i,j,k]],L).
append([[a,b,c],d,e,f],[g,h,[i,j,k]],L).

L = [[a,b,c],d,e,f,g,h,[i,j,k]]

yes
| ?- append(L1,L2,[1,2,3,4,5]).
append(L1,L2,[1,2,3,4,5]).

L1 = []
L2 = [1,2,3,4,5] ? ;

L1 = [1]
L2 = [2,3,4,5] ? ;

L1 = [1,2]
L2 = [3,4,5] ? ;

L1 = [1,2,3]
L2 = [4,5] ? ;

L1 = [1,2,3,4]
L2 = [5] ? ;

L1 = [1,2,3,4,5]
L2 = []

yes
*/

find_max([X|L], Max) :- find_max_(L, Max, X).
find_max_([], CurMax, CurMax).
find_max_([A|L], Max, CurMax) :- A > CurMax, find_max_(L, Max, A).
find_max_([A|L], Max, CurMax) :- A =< CurMax, find_max_(L, Max, CurMax).

/**
| ?-  find_max([3,2,1,9,5], Max). 
 find_max([3,2,1,9,5], Max). 

Max = 9 ? 

yes
| ?-  find_max([], Max). 
 find_max([], Max). 

no
*/


/** findall/3

findall(Template, Goal, Instances)
*/

person(john,smith,45,london).
person(mary,jones,28,edinburgh).
person(michael,wilson,62,bristol).
person(mark,smith,37,cardiff).
person(henry,roberts,23,london).

/**
| ?- findall(S, person(_,S,_,_), L).
findall(S, person(_,S,_,_), L).

L = [smith,jones,wilson,smith,roberts]

yes

| ?-  findall([Forename, Surname], person(Forename, Surname, _,_), L).
 findall([Forename, Surname], person(Forename, Surname, _,_), L).

L = [[john,smith],[mary,jones],[michael,wilson],[mark,smith],[henry,roberts]]

yes

| ?-  findall([londoner,A,B], person(A,B,_,london), L).
 findall([londoner,A,B], person(A,B,_,london), L).

L = [[londoner,john,smith],[londoner,henry,roberts]]

yes
*/


age(john,45).
age(mary,28).
age(michael,62).
age(henry,23).
age(george,62).
age(bill,17).
age(martin,62).

oldest(L) :- findall(A, age(_,A), AgeList),
	     find_max(AgeList, Oldest),
	     findall(Name, age(Name, Oldest), L).

/**
| ?- oldest(L).
oldest(L).

L = [michael,george,martin] ? 


yes
*/

find_under_30(L) :- findall(Name, (age(Name, A), A < 30), L).

/**
| ?- find_under_30(L).
find_under_30(L).

L = [mary,henry,bill]

yes
*/

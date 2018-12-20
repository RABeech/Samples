%----- Ties
tie(cupids).
tie(happy_faces).
tie(leprechauns).
tie(reindeer).

%----- People
person(crow).
person(evans).
person(hurley).
person(speigler).

%----- Relatives
relative(daughter).
relative(father_in_law).
relative(sister).
relative(uncle).

solve :-

tie(CrowTie), tie(EvansTie), tie(HurleyTie), tie(SpeiglerTie),
all_different([CrowTie,EvansTie,HurleyTie,SpeiglerTie]),

relative(CrowRelative), relative(EvansRelative),
relative(HurleyRelative), relative(SpeiglerRelative),
all_different([CrowRelative,EvansRelative,HurleyRelative,SpeiglerRelative]),

Answers = [ [crow,CrowTie,CrowRelative], [evans,EvansTie,EvansRelative],
			[hurley,HurleyTie,HurleyRelative], [speigler,SpeiglerTie,SpeiglerRelative] ],


%(a) The tie with the grinning leprachauns wasnt a present from a daughter.
\+ member([_,leprechauns,daughter], Answers),

%(b) Mr. Crow is not reindeer or happy face
\+ member([crow,reindeer,_], Answers),
\+ member([crow,happy_faces,_], Answers),

%(c)Speiglers tie was not a present from his uncle
\+ member([speigler,_,uncle], Answers),

%(d)happy face not from sister
\+ member([_,happy_faces,sister], Answers),

%(e)evans and speigler own leprechauns and was from father in law, in some way
(member([evans,leprechauns,_], Answers), member([speigler,_,father_in_law], Answers) ;
member([speigler,leprechauns,_], Answers), member([evans,_,father_in_law], Answers)),

%(f) hurley got tie from sister
member([hurley,_,sister], Answers),

tell(crow,CrowTie,CrowRelative),
tell(evans,EvansTie,EvansRelative),
tell(hurley,HurleyTie,HurleyRelative),
tell(speigler,SpeiglerTie,SpeiglerRelative).

all_different([H|T]) :- member(H,T), !, fail.
all_different([_|T]) :- all_different(T).
all_different([_]).

tell(X,Y,Z) :-
	write('Mr. '), write(X), write(' got the '), write(Y),
	write(' tie from his '), write(Z), write('.'), nl.

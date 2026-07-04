# GitFlow

## les branches
branche develop : sert a gérer les differents avancements du developpement

les differentes fonctionnaliéts de developpements en cours seront placés dans la branche de type feature/<fonctionnalité>. Merge dans branche develop lorsque terminé.

Lorsqu'une version exploitable est définie, un merge se réalise vers la branche release en y associant un tag de version.


## le tag
le tag de version sur branche release se comporte de la façon suivante :
<AAAA>.<NN>

où
* AAAA = Année en cours
* NN = incrementation (depuis 0). reinitialise si  Changement d'année

par exemple 2026.03
[![Get help on Codementor](https://cdn.codementor.io/badges/get_help_github.svg)](https://www.codementor.io/egillespie?utm_source=github&utm_medium=button&utm_term=egillespie&utm_campaign=github)

Pegger - Java
=============================

Pegger is a 2-player turn-based strategy game that I invented one early morning while playing
with my toddler daughter and her peg-hammering toy.

The game is simple enough that I am using it as my first attempt to create a web-based game
and I am both learning a variety of technologies and patterns as I go as well as writing a
tutorial series with weekly step-by-step instructions on
[my blog](http://technicalrex.com/tag/pegger/?order=asc).

## Rules to Play

Pegger is a two-player turn-based strategy game that is played on a
[2x4 pound-a-peg toy](http://amzn.com/B00005LOXV). The game setup is easy: take one pair of
colored pegs and the hammer and give them to the nearest toddler. Hang on to all of the
remaining pieces. Arrange the remaining pegs (green, red, and yellow in my case) into the
starting positions as shown below. The white circles indicate empty holes, all other colored
circles indicate where a colored peg should be placed in the board.

![Starting Layout](http://technicalrex.com/img/posts/pegger/peggerinitialstate.png)

The goal of Pegger is to be the first player to move a red or green peg into a hole adjacent
to the other peg of the same color. Yellow pegs do not count towards the victory condition,
they are considered neutral.

Victory is achieved by taking turns moving pegs. A turn consists of either moving a colored
peg into an adjacent hole or by jumping one peg over an adjacent peg and into a hole. For
example, in the starting configuration shown above the first player may move the green peg
from hole 5 into hole 6, move the red peg from hole 1 into hole 3, move the yellow peg from
hole 2 into hole 3, and so on. A peg must be moved during the player's turn.

The only other constraint when taking a turn is that you cannot undo your opponent's most
recent turn. For example, if the first player moves the green peg from hole 5 into hole 6
then the second player cannot move the green peg from hole 6 back into hole 5.

# Mayan Calculation
*Classic Puzzle Medium*

Tags: Strings, Radix

## The Goal
Perform basic arithmetical operations between two mayan numbers in Base 20.

## Rules

Mayan numerical system contains 20 numerals like:

| 0                                             | 1                                             | 2                                             | ...                                           |
|:---------------------------------------------:|:---------------------------------------------:|:---------------------------------------------:|:--------------------------------------------- |
| <pre>.00.<br/>0..0<br/>.00.<br/>....<br/></pre> | <pre>0...<br/>....<br/>....<br/>....<br/></pre> | <pre>00..<br/>....<br/>....<br/>....<br/></pre> |

Larger numbers are handled by just putting the digits below each other. 

## Game Input
### Input
* **Line 1:** the width `L` and height `H` of a mayan numeral.
* **`H` next lines:** the ASCII representation of the 20 mayan numerals. Each line is `(20 x L)` characters long.
* **Next line:** the amount of lines `S1` of the first number. (Note, `S1` has to be divisible by `H` to contain valid mayan numerals)
* **`S1` next lines:** the first number, each line having `L` characters.
* **Next line:** the amount of lines `S2` of the second number.
* **`S2` next lines:** the second number, each line having `L` characters.
* **Last line:** the operation to carry out: `*`, `/`, `+`, or `-`

### Output
The result of the operation in mayan numeration, `H` lines per section, each line having `L` characters.

### Constraints
* 0 < L, H < 100
* 0 < S1, S2 < 1000
* The remainder of a division is always 0.
* The mayan numbers given as input will not exceed 2^63.

## Example
### Input
```
4 4
.oo.o...oo..ooo.oooo....o...oo..ooo.oooo____o...oo..ooo.oooo____o...oo..ooo.oooo
o..o................____________________________________________________________
.oo.........................................____________________________________
................................................................________________
4
o...
....
....
....
4
o...
....
....
....
+
```
### Output
```
oo..
....
....
....
```

## Sources
Kotlin: [MayanCalculation.kt](MayanCalculation.kt) ([MayanCalculationTest.kt](../../test/kotlin/MayanCalculationTest.kt))

## URL
<https://www.codingame.com/ide/puzzle/mayan-calculation>

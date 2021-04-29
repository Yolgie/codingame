# Byte Pair Encoding
*Classic Puzzle Medium*

Tags: Encoding, Compression

## Goal

Calculate the byte pair encoding of an input string.

Byte pair encoding is a basic data compression algorithm. Starting with an input string, we repeatedly replace the most common pair of consecutive **bytes** (characters) with a new, **unused** byte. We will refer to these replacement bytes as **non-terminal bytes** (represented by upper-case letters) and bytes from the original input string as **terminal bytes** (represented by lower-case letters).

The algorithm terminates when no pair of consecutive bytes is repeated anywhere in the modified string. Since each iteration reduces the length of the string by at least 2, the algorithm will definitely terminate.

We will use the recursive version of the algorithm, in which replaced pairs can include non-terminal characters. (Note: this algorithm generates a context-free grammar: https://en.wikipedia.org/wiki/Context-free_grammar)

For a more detailed explanation, see https://en.wikipedia.org/wiki/Byte_pair_encoding

If, at an iteration step in the algorithm, there is more than one byte pair with highest frequency, we choose the first (**leftmost**) pair.

For the non-terminal characters, we start with Z and work our way backwards through the alphabet.

We need to keep track of the replacement "rules" (and their order) so that the original string can be reconstructed.

## Example
Input string: aaabdaaabac

Step 1:
1. the most common byte pair is aa (note: we only count (and replace) non-overlapping repetitions, so there are 2 occurrences of this byte pair)
1. first non-terminal character = Z => replace all instances of aa with Z
1. new rule: Z = aa
1. new string: ZabdZabac

Step 2:
1. the most common byte pair is Za (note: Za and ab both occur twice, so we choose the leftmost)
1. second non-terminal character = Y => replace all instances of Za with Y
1. new rule: Y = Za
1. new string: YbdYbac

Step 3:
1. the most common byte pair is Yb (2 occurrences)
1. third non-terminal character = X => replace all instances of Yb with X
1. new rule: X = Yb
1. new string: XdXac

There are now no repeated byte pairs, so the algorithm terminates.
The final string is `XdXac`, and the production rules are (note: order is important):
```
Z = aa
Y = Za
X = Yb
```

Note that in general either of the characters on the right-hand side of a rule can be terminal or non-terminal, so this grammar is not regular or even linear.

Due to a CG limitation the input is given to you split into several chunks. First combine these chunks into a single-line string, then perform the above algorithm.

## Game Input
### Input
* **First line:** 2 integers `N` `M`: the number of lines `N` to follow, and the width `M` of each line.
* **Next N lines:** the INPUT_STRING split into chunks of width `M`

### Output
* **First line:** the encoded OUTPUT_STRING
* **Next lines:** the production rules in the form `Z = c1c2`, where `Z` is a non-terminal character and the `ci` are either terminal or non-terminal characters.

### Constraints
* 2 <= `N` * `M` = length of INPUT_STRING <=1000

## Example
### Input
```
1 11
aaabdaaabac
```
### Output
```
XdXac
Z = aa
Y = Za
X = Yb
```

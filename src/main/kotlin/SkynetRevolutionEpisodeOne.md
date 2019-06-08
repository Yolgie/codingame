# Skynet Revolution Episode 1

*Classic Puzzle Medium*

Tags: Graphs, Interactive Loop

## The Goal
Cut links in a graph to prevent an AI to reach a destination.

## Rules

For each test you are given:
* A map of the network.
* The position of the exit gateways.
* The starting position of the Skynet agent.
* Nodes can only be connected to up to a single gateway.

Each game turn:
1. First off, you sever one of the given links in the network.
1. Then the Skynet agent moves from one Node to another accessible Node.

## Victory Conditions
The Skynet agent cannot reach an exit gateway
 
## Lose Conditions
The Skynet agent has reached a gateway

## Game Input
### Input
#### Initialization input
* **Line 1:** 3 integers `N` `L` `E`
  - `N`, the total number of nodes in the level, including the gateways.
  - `L`, the number of links in the level.
  - `E`, the number of exit gateways in the level.
* **Next `L` lines:** 2 integers per line (`N1`, `N2`), indicating a link between the nodes indexed `N1` and `N2` in the network.
* **Next `E` lines:** 1 integer `EI` representing the index of a gateway node.

#### Input for one game turn
* **Line 1:** 1 integer `SI`, which is the index of the node on which the Skynet agent is positioned this turn.

#### Output for one game turn
* **A single line** comprised of two integers `C1` and `C2` separated by a space. `C1` and `C2` are the indices of the nodes you wish to sever the link between.

### Constraints
* 2 ≤ `N` ≤ 500
* 1 ≤ `L` ≤ 1000
* 1 ≤ `E` ≤ 20
* 0 ≤ `N1`, `N2` < `N`
* 0 ≤ `SI` < `N`
* 0 ≤ `C1`, `C2` < `N`
* Response time per turn ≤ 150ms

## Example
### Initial Input
```
4 4 1
0 1
0 2
1 3
2 3
3
```
### Loop
Loop Input
``` 
0
```
Response
``` 
1 3
```
Loop Input
``` 
2
```
Response
``` 
2 3
```
**Success!**

## Sources
Kotlin: [SkynetRevolution.kt](SkynetRevolution.kt) ([SkynetRevolutionTest.kt](../../../test/kotlin/SkynetRevolutionTest.kt))

## URL
<https://www.codingame.com/ide/puzzle/skynet-revolution-episode-1>

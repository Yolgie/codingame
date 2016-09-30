package at.cnoize.codingame.horseracingduals;

import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * CodinGame Classic Puzzle Easy - Horse-racing Duals
 * Copyright (C) 2016 Matthias 'Yolgie' Holzinger {@literal <yolgie@cnoize.at>}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt();

        PriorityQueue<Integer> horses = new PriorityQueue<>();

        for (int i = 0; i < N; i++) {
            horses.add(in.nextInt());
        }

        Integer prev = horses.poll();
        Integer next = horses.poll();
        int diff = next - prev;

        do {
            diff = Math.min(diff, next - prev);
            prev = next;
            next = horses.poll();
        } while (next != null);

        System.out.println(diff);
    }
}
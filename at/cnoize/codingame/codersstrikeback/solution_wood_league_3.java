package at.cnoize.codingame.codersstrikeback;

import java.util.Scanner;

/**
 * CodinGame Bot Programming - Coders Strike Back
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

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        Point currentPosition;
        Point nextCheckpoint;

        // game loop
        while (true) {
            currentPosition = new Point(in.nextInt(), in.nextInt());
            nextCheckpoint = new Point(in.nextInt(), in.nextInt());

            int thrust = 90;

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // Edit this line to output the target position
            // and thrust (0 <= thrust <= 100)
            // i.e.: "x y thrust"

            System.out.println(nextCheckpoint.toString(thrust));


        }
    }

    private static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        double getDistance(Point other) {
            int x = Math.abs(this.x - other.x);
            int y = Math.abs(this.y - other.y);
            return Math.sqrt(x * x + y * y);
        }

        public String toString(int thrust) {
            return String.format("%d %d %d", this.x, this.y, thrust);
        }
    }
}

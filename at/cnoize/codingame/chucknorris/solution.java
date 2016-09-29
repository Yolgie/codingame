package at.cnoize.codingame.chucknorris;

import java.util.Scanner;

/**
 * CodinGame Classic Puzzle Easy - Chuck Norris
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
        String MESSAGE = in.nextLine();
        String binaryString = getBinaryString(MESSAGE);
        StringBuilder result = new StringBuilder();

        System.err.println(MESSAGE);
        System.err.println(binaryString);

        char previous = binaryString.charAt(0);
        int count = 0;

        for (char current : binaryString.toCharArray()) {
            System.err.println(String.format("%s %d %s", previous, count, current));

            if (previous == current) {
                count++;
            } else {
                result.append(convertChar(previous, count));

                previous = current;
                count = 1;
            }
        }
        result.append(convertChar(previous, count));
        result.deleteCharAt(result.lastIndexOf(" "));

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        System.out.println(result.toString());
    }

    public static String getBinaryString(String message) {
        StringBuilder binaryString = new StringBuilder();
        StringBuilder part;

        for (char c : message.toCharArray()) {
            part = new StringBuilder();
            part.append(Integer.toBinaryString(c));
            while (part.length() < 7) {
                part.insert(0, '0');
            }
            binaryString.append(part);
        }

        return binaryString.toString();
    }

    public static StringBuilder getZeroes(int count) {
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            result.append('0');
        }
        result.append(' ');
        return result;
    }

    public static StringBuilder convertChar(char c, int count) {
        StringBuilder result = new StringBuilder();
        switch (c) {
            case '0':
                result.append(getZeroes(2));
                break;
            case '1':
                result.append(getZeroes(1));
                break;
            default:
        }

        result.append(getZeroes(count));

        return result;
    }
}
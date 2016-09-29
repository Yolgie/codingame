package at.cnoize.codingame.balancedternaryencode;

import java.util.Scanner;

/**
 * CodinGame Community Puzzle - Balanced ternary computer: encode
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
        int DEC = in.nextInt();
        int remainder;
        boolean negative = false;

        if (DEC < 0) {
            negative = true;
            DEC = DEC * (-1);
        }

        StringBuilder result = new StringBuilder();

        if (DEC == 0) {
            result.append(0);
        }

        while (DEC > 0) {
            remainder = DEC % 3;
            DEC = DEC / 3;

            switch (remainder) {
                case 0:
                    result.append(0);
                    break;
                case 1:
                    if (!negative) {
                        result.append(1);
                    } else {
                        result.append('T');
                    }
                    break;
                case 2:
                    DEC++;
                    if (!negative) {
                        result.append('T');
                    } else {
                        result.append(1);
                    }
                    break;
            }
        }

        System.out.println(result.reverse().toString());
    }
}

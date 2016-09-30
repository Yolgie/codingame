package at.cnoize.codingame.mimetype;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CodinGame Classic Puzzle Easy - MIME Type
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
        int N = in.nextInt(); // Number of elements which make up the association table.
        int Q = in.nextInt(); // Number Q of file names to be analyzed.

        Map<String, String> associationTable = new HashMap<>(N);

        for (int i = 0; i < N; i++) {
            associationTable.put(in.next().toLowerCase(), in.next());
        }

        in.nextLine();
        for (int i = 0; i < Q; i++) {
            String filename = in.nextLine(); // One file name per line.
            int extensionStart = filename.lastIndexOf('.');
            String extension = filename.lastIndexOf('.') >= 0 ? filename.substring(filename.lastIndexOf('.') + 1).toLowerCase() : null;

            // For each of the Q filenames, display on a line the corresponding MIME type. If there is no corresponding type, then display UNKNOWN.
            if (associationTable.containsKey(extension)) {
                System.out.println(associationTable.get(extension));
            } else {
                System.out.println("UNKNOWN");
            }
        }
    }
}

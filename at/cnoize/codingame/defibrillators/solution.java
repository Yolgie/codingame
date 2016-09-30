package at.cnoize.codingame.defibrillators;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CodinGame Classic Puzzle Easy - Defibrillators
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

    private static class Location {
        String name;
        Double longitude;
        Double latitude;

        Double getDistance(Location otherLocation) {
            Double x = (otherLocation.longitude - this.longitude) * Math.cos((this.latitude + otherLocation.latitude) / 2);
            Double y = (otherLocation.latitude - this.latitude);
            Double d = Math.sqrt(x * x + y * y) * 6371;
            return d;
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Location userLocation = new Location();
        Double closestLocation = Double.MAX_VALUE;
        userLocation.longitude = parseDouble(in.next());
        userLocation.latitude = parseDouble(in.next());
        int N = in.nextInt();
        Map<Double, Location> locations = new HashMap<>(N);
        in.nextLine();
        for (int i = 0; i < N; i++) {
            Location location = parseDefib(in.nextLine());
            Double distance = userLocation.getDistance(location);
            locations.put(distance, location);
            if (distance < closestLocation) {
                closestLocation = distance;
            }
        }
        // To debug: System.err.println("Debug messages...");
        System.out.println(locations.get(closestLocation).name);
    }

    private static Location parseDefib(String defib) {
        Location location = new Location();
        String[] defibValues = defib.split(";");
        location.name = defibValues[1];
        location.longitude = parseDouble(defibValues[4]);
        location.latitude = parseDouble(defibValues[5]);
        return location;
    }

    private static Double parseDouble(String string) {
        return Double.parseDouble(string.replace(',', '.'));
    }
}

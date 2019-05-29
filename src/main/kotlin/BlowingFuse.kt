import java.util.*

class BlowingFuse {

    data class Appliance(val consumption: Int, val turnedOn: Boolean = false)

    fun solve() {
        val input = Scanner(System.`in`)
        val n = input.nextInt()
        val m = input.nextInt()
        val c = input.nextInt()
        var appliances = readAppliances(input, n)
        val buttonClicks = readButtonClicks(input, m)


        System.err.println("$n $m $c")

        var maxConsumption = 0
        for (id in buttonClicks) {
            appliances = turnOnAppliance(id, appliances)
            maxConsumption = maxOf(maxConsumption, calcConsumption(appliances))
            System.err.println(maxConsumption)
        }

        if (maxConsumption > c) {
            println("Fuse was blown.")
        } else {
            println("Fuse was not blown.")
            println("Maximal consumed current was $maxConsumption A.")
        }
    }

    fun calcConsumption(appliances: List<Appliance>): Int {
        return appliances
            .map { appliance -> if (appliance.turnedOn) appliance.consumption else 0 }
            .sum()
    }

    fun turnOnAppliance(id: Int, appliances: List<Appliance>): List<Appliance> {
        return appliances.mapIndexed { index, appliance ->
            if (index == id - 1)
                appliance.copy(turnedOn = !appliance.turnedOn)
            else
                appliance
        }
    }

    fun readAppliances(input: Scanner, count: Int): List<Appliance> {
        val appliances = mutableListOf<Appliance>()
        for (i in 1 until count + 1) {
            appliances += Appliance(input.nextInt())
        }
        return appliances.toList()
    }

    fun readButtonClicks(input: Scanner, count: Int): List<Int> {
        val buttonClicks = mutableListOf<Int>()
        for (i in 1 until count + 1)
            buttonClicks += input.nextInt()
        return buttonClicks.toList()
    }
}

fun main() {
    BlowingFuse().solve()
}

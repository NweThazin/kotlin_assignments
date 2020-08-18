package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        this.allDrivers.minus(this.trips.map { it.driver })

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val passengerMaps = this.trips.flatMap { it.passengers }.groupBy { it }
    return this.allPassengers.filter { passenger -> passengerMaps[passenger]?.size ?: 0 >= minTrips }.toSet()
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        this.trips.groupBy { it.driver }[driver]?.flatMap { it.passengers }?.groupBy { it }
                ?.filter { it.value.size > 1 }?.keys ?: setOf()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    //first is discount and second is not discount
    val discountAndNonDiscountPair =
            this.trips.partition { it.discount != null && it.discount != 0.0 }

    val passengerListWithDiscount =
            discountAndNonDiscountPair.first.map { it.passengers }.flatten().groupBy { it }
    val passengerListWithoutDiscount =
            discountAndNonDiscountPair.second.map { it.passengers }.flatten().groupBy { it }

    val tempList = mutableListOf<Passenger>()
    passengerListWithDiscount.map {
        val noDiscountPassenger = passengerListWithoutDiscount[it.key]
        if (it.value.size > (noDiscountPassenger?.size ?: 0)) {
            tempList.add(it.key)
        }
    }

    return tempList.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return if (this.trips.isEmpty()) {
        null
    } else {
        val maxDuration =
                this.trips.map { it.duration }.groupingBy { it }.eachCount().maxBy { it.key }?.key
                        ?: return null
        val tempMap = mutableMapOf<IntRange, Int>()
        for (i in 0..maxDuration step 10) {
            val intRange = IntRange(i, i + 9)
            val intRangeCount = this.trips.filter { it.duration in intRange }.count()
            tempMap[intRange] = intRangeCount
        }

        val maxCountRange = tempMap.maxBy { it.value }

        maxCountRange?.key
    }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    val driverMaps = this.trips.groupBy { it.driver }
            .mapValues { it.value.sumByDouble { park -> park.cost } }.toList()
            .sortedByDescending { it.second }.toMap()
    val totalCost = driverMaps.values.sumByDouble { it }

    val driver20Percentage = (allDrivers.size * 0.2).toInt()
    var driverCount = 0
    driverMaps.map {
        driverCount++
        val totalSum = driverMaps.toList().take(driverCount).sumByDouble { it.second }
        val percentage = (totalSum / totalCost) * 100
        if (percentage >= 80.0) {
            return driverCount <= driver20Percentage
        }
    }

    return false
}
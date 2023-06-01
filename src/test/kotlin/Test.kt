import com.github.tanokun.efcammosort.AmmoType

fun main() {

    val name = "§e5.56x45mm Warmageddon"
        .replace("§e", "A_")
        .replace(".", "_")
        .replace("x", "X")
        .replace(" ", "_")
        .uppercase()

    println(name)
    val items = arrayListOf(
        Pair("A_5_56X45M_M995", 10),
        Pair("A_5_7X28M_SS190", 40),
        Pair("A_45_ACP_MATCH_FMJ", 20),
        Pair("A_45_ACP_AP", 20),
        Pair("A_45_ACP_AP", 40),
        Pair("A_5_45X39M_PPBS", 40)
    )

    val putItems = HashMap<AmmoType, Int>()

    items.forEach {
        putItems[AmmoType.valueOf(it.first)] = (putItems[AmmoType.valueOf(it.first)] ?: 0) + it.second
    }

    val sortedItems = ArrayList<Pair<AmmoType, Int>>()

    putItems
        .toSortedMap { o1, o2 -> return@toSortedMap if (o1.han < o2.han) -1 else 1 }
        .forEach { en ->
            val stack = en.value / en.key.byMaterial.max
            val fraction = en.value % en.key.byMaterial.max
            for (i in 1..stack)
                sortedItems.add(Pair(en.key, en.key.byMaterial.max))
            if (fraction != 0) sortedItems.add(Pair(en.key, fraction))
        }
}
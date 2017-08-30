package bracket

import kotlin.js.Math

class TreeNode<T>(value: T) {
    var value: T = value

    var parent: TreeNode<T>? = null

    var children: MutableList<TreeNode<T>> = mutableListOf()

    fun addLeft(node: TreeNode<T>?) {
        node?.let {
            children.add(0, it)
            it.parent = this
        }
        require(children.size <= 2)
        println("Adicionado left")
    }

    fun addRight(node: TreeNode<T>?) {
        node?.let {
            children.add(1, it)
            it.parent = this
        }
        require(children.size <= 2)
        println("Adicionado right")
    }

    override fun toString(): String {
        var s = "${value}"
        if (!children.isEmpty()) {
            s += " {" + children.map { it.toString() } + " }"
        }
        return s
    }
}

sealed class Partida {
    data class Finished(val time1: Time, val time2: Time, val resultado: Resultado) : Partida()
    data class Scheduled(val time1: Time, val time2: Time, val resultado: Resultado?) : Partida()
    object Empty : Partida() {
        override fun toString(): String {
            return "Partida:Empty"
        }
    }
}

data class Resultado(val time1Pontos: Int, val time2Pontos: Int)
data class Time(val nome: String, val codigo: String)
data class Bracket(val root: TreeNode<Partida>) {
    override fun toString(): String {
        return root.toString()
    }
}

fun createBracket(times: List<Time>): Bracket {
    require(times.size >= 2)
    val root: TreeNode<Partida> ?
    when {
        times.size == 2  -> {
            root = TreeNode(Partida.Scheduled(times[0], times[1], null))
        }
        else -> {
            val teamsList = times.toMutableList()
            while (teamsList.size % 4 != 0) teamsList.add(Time("W.O", "WO-" + Math.random()))
            root = buildTree(teamsList.toList().iterator(), 0 , teamsList.size / 2)
            println("Times: " + teamsList.toString())
            println("Numer of matches: " + teamsList.size)
            println(root)
        }
    }

    return Bracket(root!!)

}

fun buildTree(iterator: Iterator<Time>, index: Int, minNumeroJogos: Int): TreeNode<Partida>? {
    //each height of the three = number of games is 2 ^ index
    //i.e Finals(index = 0) has 1 game (2^0), Semifinals (index: 1 ) has 2 games (2^1).
    val numOfMatches = Math.pow(2.0, index.toDouble()).toInt()
    return when {
        numOfMatches > minNumeroJogos -> { null }
        numOfMatches == minNumeroJogos -> TreeNode(Partida.Scheduled(iterator.next(), iterator.next(), null))
        else -> {
            val root: TreeNode<Partida> = TreeNode(Partida.Empty)
            root.addLeft(buildTree(iterator, index + 1, minNumeroJogos))
            root.addRight(buildTree(iterator, index + 1, minNumeroJogos))
            root
        }
    }
}

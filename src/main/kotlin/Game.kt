import kotlin.math.min

class Game(val attemptCount: Int, val pegCount: Int, val colorCount: Int) {
    constructor(parameters: Parameters) : this(parameters.attemptCount, parameters.pegCount, parameters.colorCount)

    private val correctCode = List(pegCount) { kotlin.random.Random.nextInt(colorCount) }
    private val correctOccurrences = countOccurrences(correctCode)
    var result: Result = Result.UNFINISHED
        private set
    private var attemptsMade = 0

    data class Parameters(val attemptCount: Int = 8, val pegCount: Int = 4, val colorCount: Int = 6)

    data class Response(val positionMatches: Int, val colorMatches: Int)

    enum class Result {
        WIN, LOSE, UNFINISHED
    }

    private fun countOccurrences(code: List<Int>): Map<Int, Int> =
        code.groupBy { it }.mapValues { it.value.size }

    fun makeGuess(code: List<Int>): Response {
        val positionMatches = code.zip(correctCode).count { it.first == it.second }
        attemptsMade += 1
        if (positionMatches == pegCount)
            result = Result.WIN
        else if (attemptsMade == attemptCount)
            result = Result.LOSE
        val colorMatches =
            countOccurrences(code).map { (color, count) -> min(count, correctOccurrences.getOrDefault(color, 0)) }.sum()
        return Response(positionMatches, colorMatches - positionMatches)
    }
}
package za.co.mohlare.app.data

/** One subject and the learner's mark for it, as a percentage (0–100). */
data class SubjectMark(val subject: String, val percent: Int)

/** The result of an APS calculation, with what was and wasn't counted. */
data class ApsResult(
    val total: Int,
    val counted: List<SubjectMark>,
    val notCounted: List<SubjectMark>,
) {
    /** Standard APS needs six subjects (not Life Orientation) to be complete. */
    val isComplete: Boolean get() = counted.size == 6
}

object Aps {

    const val LIFE_ORIENTATION = "Life Orientation"

    /** NSC achievement level (1–7) for a percentage. */
    fun levelFor(percent: Int): Int = when {
        percent >= 80 -> 7
        percent >= 70 -> 6
        percent >= 60 -> 5
        percent >= 50 -> 4
        percent >= 40 -> 3
        percent >= 30 -> 2
        else -> 1
    }

    fun isLifeOrientation(subject: String) = subject.trim().equals(LIFE_ORIENTATION, ignoreCase = true)

    /**
     * Standard APS: the levels of the best six subjects, not counting Life Orientation.
     * Used by UJ, UP and many other institutions.
     * Wits, UCT, Stellenbosch, NMU and some others count differently; those rules come later,
     * per university, from Firestore.
     */
    fun standard(marks: List<SubjectMark>): ApsResult {
        val (lifeOrientation, academic) = marks.partition { isLifeOrientation(it.subject) }
        val ranked = academic.sortedByDescending { it.percent }
        val counted = ranked.take(6)
        val notCounted = ranked.drop(6) + lifeOrientation
        return ApsResult(
            total = counted.sumOf { levelFor(it.percent) },
            counted = counted,
            notCounted = notCounted,
        )
    }
}

/** Which set of results the marks come from. */
val ResultTerms = listOf(
    "Grade 11 final results",
    "Grade 12 March results",
    "Grade 12 June results",
    "Grade 12 September (trial) results",
    "Grade 12 final results",
)

private val NscLanguages = listOf(
    "Afrikaans", "English", "isiNdebele", "isiXhosa", "isiZulu", "Sepedi",
    "Sesotho", "Setswana", "siSwati", "Tshivenda", "Xitsonga",
)

/** NSC subjects learners can pick from. Languages appear as Home Language and First Additional Language. */
val NscSubjects: List<String> = buildList {
    NscLanguages.forEach { add("$it Home Language") }
    NscLanguages.forEach { add("$it First Additional Language") }
    addAll(
        listOf(
            "Mathematics", "Mathematical Literacy", "Technical Mathematics", Aps.LIFE_ORIENTATION,
            "Accounting", "Agricultural Sciences", "Business Studies", "Civil Technology",
            "Computer Applications Technology", "Consumer Studies", "Dance Studies", "Design",
            "Dramatic Arts", "Economics", "Electrical Technology", "Engineering Graphics and Design",
            "Geography", "History", "Hospitality Studies", "Information Technology", "Life Sciences",
            "Mechanical Technology", "Music", "Physical Sciences", "Religion Studies",
            "Technical Sciences", "Tourism", "Visual Arts",
        )
    )
}

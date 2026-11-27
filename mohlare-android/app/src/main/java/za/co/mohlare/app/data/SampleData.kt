package za.co.mohlare.app.data

/*
 * SAMPLE DATA ONLY. These names, marks, dates and APS numbers are placeholders for building the UI.
 * Replace them with real data from Firestore (see the plan doc, "Data model").
 */

data class Deadline(
    val day: String,
    val month: String,
    val title: String,
    val subtitle: String,
    val dueLabel: String,
    val urgent: Boolean,
)

data class Course(
    val university: String,
    val name: String,
    val detail: String,
    val qualifies: Boolean,
    val badge: String,
)

data class Bursary(
    val name: String,
    val provider: String,
    val covers: List<String>,
    val closes: String,
    val checked: String,
    val closingSoon: Boolean,
)

enum class AppStatus(val label: String) {
    PLANNING("Planning"),
    SUBMITTED("Submitted"),
    NEEDS_DOCUMENTS("Needs documents"),
    ACCEPTED("Accepted"),
}

data class Application(val name: String, val closes: String, val status: AppStatus)

data class DocumentItem(val name: String, val ready: Boolean)

object SampleData {

    val deadlines = listOf(
        Deadline("01", "AUG", "University of Limpopo", "BSc Computer Science · closes", "12 days", true),
        Deadline("15", "AUG", "Engineering bursary", "Sample provider · closes", "26 days", false),
        Deadline("30", "SEP", "University of Pretoria", "BSc Information Technology · closes", "2 months", false),
    )

    val courses = listOf(
        Course("UNIVERSITY OF LIMPOPO", "BSc Computer Science", "Needs APS 30 · Maths level 5 · 3 years", true, "You qualify"),
        Course("UNIVERSITY OF PRETORIA", "BSc Information Technology", "Needs APS 32 · Maths level 5 · 3 years", true, "You qualify"),
        Course("WITS UNIVERSITY", "BCom Accounting", "Needs APS 36 · Maths level 5 · 3 years", false, "2 points short"),
    )

    val bursaries = listOf(
        Bursary("Engineering bursary", "Sample provider", listOf("Tuition", "Accommodation", "Books"), "15 Aug", "3 days ago", true),
        Bursary("Teaching bursary", "Sample provider", listOf("Tuition", "Monthly allowance"), "30 Sep", "1 week ago", false),
        Bursary("Health sciences bursary", "Sample provider", listOf("Full cost", "Work-back"), "31 Oct", "2 weeks ago", false),
    )

    val applications = listOf(
        Application("UL · BSc Computer Science", "Closes 1 Aug", AppStatus.SUBMITTED),
        Application("Engineering bursary", "Closes 15 Aug", AppStatus.NEEDS_DOCUMENTS),
        Application("UP · BSc Information Technology", "Closes 30 Sep", AppStatus.PLANNING),
        Application("TUT · Diploma in IT", "Closes 30 Sep", AppStatus.ACCEPTED),
    )

    val documents = listOf(
        DocumentItem("Certified ID copy", true),
        DocumentItem("Grade 11 final results", true),
        DocumentItem("Parent or guardian ID", true),
        DocumentItem("Proof of household income", false),
    )
}

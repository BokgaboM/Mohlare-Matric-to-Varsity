package za.co.mohlare.app.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/** A learner's profile, stored at users/{uid} in Firestore. */
data class UserProfile(
    val name: String,
    val grade: Int,
    val province: String,
    val language: String,
    val under18: Boolean,
    val guardianConsent: Boolean,
    val consentAccepted: Boolean,
    val marks: List<SubjectMark> = emptyList(),
    val marksTerm: String = "",
) {
    /** Standard APS from the learner's saved marks. */
    val aps: ApsResult get() = Aps.standard(marks)
    val hasMarks: Boolean get() = marks.isNotEmpty()
    val firstName: String get() = name.trim().substringBefore(' ')
    val initials: String
        get() = name.trim().split(' ').filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }
    val gradeAndProvince: String get() = "Grade $grade · $province"
}

val Provinces = listOf(
    "Eastern Cape", "Free State", "Gauteng", "KwaZulu-Natal", "Limpopo",
    "Mpumalanga", "North West", "Northern Cape", "Western Cape",
)

class UserRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    private fun userDoc(uid: String) = db.collection("users").document(uid)

    /** Emits the profile whenever it changes, or null if the learner hasn't made one yet. */
    fun profile(uid: String): Flow<UserProfile?> = callbackFlow {
        val registration = userDoc(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot?.takeIf { it.exists() }?.toUserProfile())
        }
        awaitClose { registration.remove() }
    }

    suspend fun saveProfile(uid: String, profile: UserProfile) {
        val data = mapOf(
            "name" to profile.name.trim(),
            "grade" to profile.grade,
            "province" to profile.province,
            "language" to profile.language,
            "under18" to profile.under18,
            "guardianConsent" to profile.guardianConsent,
            "consentAccepted" to profile.consentAccepted,
            "consentAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp(),
        )
        userDoc(uid).set(data, SetOptions.merge()).await()
    }

    /** Saves the learner's marks and the standard APS worked out from them. */
    suspend fun saveMarks(uid: String, marks: List<SubjectMark>, term: String) {
        val data = mapOf(
            "marks" to marks.map { mapOf("subject" to it.subject, "percent" to it.percent) },
            "marksTerm" to term,
            "aps" to Aps.standard(marks).total,
            "updatedAt" to FieldValue.serverTimestamp(),
        )
        userDoc(uid).set(data, SetOptions.merge()).await()
    }

    /**
     * Deletes the learner's profile document.
     * TODO: when applications and saved items are added, delete those sub-collections too.
     */
    suspend fun deleteProfile(uid: String) {
        userDoc(uid).delete().await()
    }
}

private fun DocumentSnapshot.toUserProfile() = UserProfile(
    name = getString("name") ?: "",
    grade = getLong("grade")?.toInt() ?: 12,
    province = getString("province") ?: "",
    language = getString("language") ?: Languages.first(),
    under18 = getBoolean("under18") ?: false,
    guardianConsent = getBoolean("guardianConsent") ?: false,
    consentAccepted = getBoolean("consentAccepted") ?: false,
    marks = (get("marks") as? List<*>).orEmpty().mapNotNull { item ->
        val map = item as? Map<*, *> ?: return@mapNotNull null
        val subject = map["subject"] as? String ?: return@mapNotNull null
        val percent = (map["percent"] as? Number)?.toInt() ?: return@mapNotNull null
        SubjectMark(subject, percent)
    },
    marksTerm = getString("marksTerm") ?: "",
)

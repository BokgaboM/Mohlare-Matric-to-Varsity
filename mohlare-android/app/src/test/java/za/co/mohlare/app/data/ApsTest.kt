package za.co.mohlare.app.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Run with: Gradle panel → app → Tasks → verification → test (or right-click this file → Run). */
class ApsTest {

    @Test
    fun levelsMatchNscBands() {
        assertEquals(7, Aps.levelFor(80))
        assertEquals(7, Aps.levelFor(100))
        assertEquals(6, Aps.levelFor(79))
        assertEquals(5, Aps.levelFor(60))
        assertEquals(4, Aps.levelFor(50))
        assertEquals(3, Aps.levelFor(40))
        assertEquals(2, Aps.levelFor(30))
        assertEquals(1, Aps.levelFor(29))
        assertEquals(1, Aps.levelFor(0))
    }

    @Test
    fun lifeOrientationIsNeverCounted() {
        val marks = listOf(
            SubjectMark("Life Orientation", 95),
            SubjectMark("Sepedi Home Language", 72),
            SubjectMark("English First Additional Language", 65),
            SubjectMark("Mathematics", 58),
            SubjectMark("Physical Sciences", 61),
            SubjectMark("Life Sciences", 70),
            SubjectMark("Geography", 49),
        )
        val result = Aps.standard(marks)
        // 6 + 5 + 4 + 5 + 6 + 3 = 29
        assertEquals(29, result.total)
        assertTrue(result.isComplete)
        assertTrue(result.notCounted.any { it.subject == "Life Orientation" })
    }

    @Test
    fun onlyBestSixAreCounted() {
        val marks = listOf(
            SubjectMark("A", 85), SubjectMark("B", 75), SubjectMark("C", 65),
            SubjectMark("D", 55), SubjectMark("E", 45), SubjectMark("F", 35),
            SubjectMark("G", 20),
        )
        // 7 + 6 + 5 + 4 + 3 + 2 = 27; G (level 1) is dropped
        assertEquals(27, Aps.standard(marks).total)
    }

    @Test
    fun fewerThanSixSubjectsIsIncomplete() {
        val result = Aps.standard(listOf(SubjectMark("Mathematics", 70)))
        assertEquals(6, result.total)
        assertFalse(result.isComplete)
    }
}

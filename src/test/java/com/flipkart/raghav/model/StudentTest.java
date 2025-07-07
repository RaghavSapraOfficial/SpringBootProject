package com.flipkart.raghav.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Student Model Tests")
class StudentTest {

    private Student student1;
    private Student student2;
    private Student student3;

    @BeforeEach
    void setUp() {
        student1 = new Student(1, "John Doe", 85);
        student2 = new Student(2, "Jane Smith", 92);
        student3 = new Student(1, "John Doe", 85);
    }

    @Test
    @DisplayName("Should create Student with no-args constructor")
    void testNoArgsConstructor() {
        Student student = new Student();
        assertNotNull(student);
        assertEquals(0, student.getId());
        assertNull(student.getName());
        assertEquals(0, student.getMarks());
    }

    @Test
    @DisplayName("Should create Student with all-args constructor")
    void testAllArgsConstructor() {
        Student student = new Student(1, "Test Student", 95);
        assertEquals(1, student.getId());
        assertEquals("Test Student", student.getName());
        assertEquals(95, student.getMarks());
    }

    @Test
    @DisplayName("Should set and get id correctly")
    void testIdGetterAndSetter() {
        Student student = new Student();
        student.setId(100);
        assertEquals(100, student.getId());
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterAndSetter() {
        Student student = new Student();
        student.setName("Alice Johnson");
        assertEquals("Alice Johnson", student.getName());
    }

    @Test
    @DisplayName("Should set and get marks correctly")
    void testMarksGetterAndSetter() {
        Student student = new Student();
        student.setMarks(88);
        assertEquals(88, student.getMarks());
    }

    @Test
    @DisplayName("Should return true for equal objects")
    void testEqualsWithEqualObjects() {
        assertEquals(student1, student3);
    }

    @Test
    @DisplayName("Should return false for different objects")
    void testEqualsWithDifferentObjects() {
        assertNotEquals(student1, student2);
    }

    @Test
    @DisplayName("Should return false when comparing with null")
    void testEqualsWithNull() {
        assertNotEquals(null, student1);
    }

    @Test
    @DisplayName("Should return false when comparing with different type")
    void testEqualsWithDifferentType() {
        assertNotEquals(student1, "Not a Student object");
    }

    @Test
    @DisplayName("Should return false when id is different")
    void testEqualsWithDifferentId() {
        Student differentId = new Student(999, "John Doe", 85);
        assertNotEquals(student1, differentId);
    }

    @Test
    @DisplayName("Should return false when name is different")
    void testEqualsWithDifferentName() {
        Student differentName = new Student(1, "Different Name", 85);
        assertNotEquals(student1, differentName);
    }

    @Test
    @DisplayName("Should return false when marks is different")
    void testEqualsWithDifferentMarks() {
        Student differentMarks = new Student(1, "John Doe", 100);
        assertNotEquals(student1, differentMarks);
    }

    @Test
    @DisplayName("Should return same hash code for equal objects")
    void testHashCodeWithEqualObjects() {
        assertEquals(student1.hashCode(), student3.hashCode());
    }

    @Test
    @DisplayName("Should return different hash codes for different objects")
    void testHashCodeWithDifferentObjects() {
        assertNotEquals(student1.hashCode(), student2.hashCode());
    }

    @Test
    @DisplayName("Should return consistent hash code")
    void testHashCodeConsistency() {
        int hashCode1 = student1.hashCode();
        int hashCode2 = student1.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Should return meaningful string representation")
    void testToString() {
        String expected = "Student(id=1, name=John Doe, marks=85)";
        assertEquals(expected, student1.toString());
    }

    @Test
    @DisplayName("Should handle null name in toString")
    void testToStringWithNullName() {
        Student student = new Student(1, null, 85);
        String result = student.toString();
        assertTrue(result.contains("name=null"));
    }

    @Test
    @DisplayName("Should handle zero values correctly")
    void testZeroValues() {
        Student student = new Student(0, "", 0);
        assertEquals(0, student.getId());
        assertEquals("", student.getName());
        assertEquals(0, student.getMarks());
    }

    @Test
    @DisplayName("Should handle negative values")
    void testNegativeValues() {
        Student student = new Student(-1, "Negative Test", -10);
        assertEquals(-1, student.getId());
        assertEquals("Negative Test", student.getName());
        assertEquals(-10, student.getMarks());
    }

    @Test
    @DisplayName("Should handle large values")
    void testLargeValues() {
        Student student = new Student(Integer.MAX_VALUE, "Large Test", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, student.getId());
        assertEquals("Large Test", student.getName());
        assertEquals(Integer.MAX_VALUE, student.getMarks());
    }
}
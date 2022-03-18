// FILE: test.kt

data class D(
    val i: Int,
    val s: String
)

data class E(
    val i: Int,
    val s: String
) {
    override fun toString() = "OK"
    override fun equals(other: Any?) = false
    override fun hashCode() = 42
    fun copy() = E(i, s)
}

fun box() {
    val d = D(1, "a")
    d.equals(D(1, "a"))
    d.hashCode()
    d.toString()
    val (i, s) = d
    d.copy()
    val e = E(1, "a")
    e.equals(E(1, "a"))
    e.hashCode()
    e.toString()
    val (s2, i2) = e
    e.copy()
}

// EXPECTATIONS
// test.kt:19 box
// test.kt:3 <init>

// EXPECTATIONS JVM_IR
// test.kt:4 <init>
// test.kt:5 <init>
// test.kt:3 <init>

// EXPECTATIONS
// test.kt:19 box
// test.kt:20 box
// test.kt:3 <init>

// EXPECTATIONS JVM_IR
// test.kt:4 <init>
// test.kt:5 <init>
// test.kt:3 <init>

// EXPECTATIONS
// test.kt:20 box
// test.kt:21 box
// test.kt:22 box
// test.kt:23 box
// test.kt:4 component1
// test.kt:23 box
// test.kt:5 component2
// test.kt:23 box
// test.kt:24 box

// EXPECTATIONS JVM_IR
// test.kt:3 <init>
// test.kt:4 <init>
// test.kt:5 <init>

// EXPECTATIONS
// test.kt:3 <init>
// test.kt:-1 copy
// test.kt:24 box
// test.kt:25 box

// EXPECTATIONS JVM_IR
// test.kt:8 <init>
// test.kt:9 <init>
// test.kt:10 <init>

// EXPECTATIONS
// test.kt:8 <init>
// test.kt:25 box
// test.kt:26 box

// EXPECTATIONS JVM_IR
// test.kt:8 <init>
// test.kt:9 <init>
// test.kt:10 <init>

// EXPECTATIONS
// test.kt:8 <init>
// test.kt:26 box
// test.kt:13 equals
// test.kt:26 box
// test.kt:27 box
// test.kt:14 hashCode
// test.kt:27 box
// test.kt:28 box
// test.kt:12 toString
// test.kt:28 box
// test.kt:29 box
// test.kt:9 component1
// test.kt:29 box
// test.kt:10 component2
// test.kt:29 box
// test.kt:30 box
// test.kt:15 copy

// EXPECTATIONS JVM_IR
// test.kt:8 <init>
// test.kt:9 <init>
// test.kt:10 <init>

// EXPECTATIONS
// test.kt:8 <init>
// test.kt:15 copy
// test.kt:30 box
// test.kt:31 box

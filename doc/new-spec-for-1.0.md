Spec for 1.0
===

Since Kotlin 1.1 release, this library build is broken.
The fragile character of the version 0.1 is due to its character heavily depending on Kotlin reflection.
To become more stable library, changing the architecture to one more matches to Kotlin.

Architecture of version 0.1
---

The architecture of v 0.1 is `pull` architecture, which scans class files, inspects them, collects methods from their
meta-information, creates instances from their constructor objects and finally invokes tests.
These all operations are belonging to reflection so that
a version up of kotlin-reflection always threaten the operations.

```kotlin
class SampleProperties {
  @Property
  val positiveIntAlwaysGreaterThan0 = forAll(positiveInt).satisfy { it > 0 }
}
```

Architecture of version 1.0
---

The version 1.0 will reduce the dependency on ths kotlin-reflection as much as possible.
And it will make its architecture as `push` architecture, a user implements property check interface,
and call runners from it.

```kotlin
object SampleProperties: Descriptor {
  override val properties: List<Property> get() =
    listOf(positiveIntAlwaysGreaterThan0(), negativeIntAlwaysLessThan0())

  fun positiveIntAlwaysGreaterThan0(): Property {
    return prop("positive int always greater than 0")
        { positiveInt } satisfy { it > 0 }
  }
  fun negativeIntAlwaysLessThan0(): Property = prop("negative") { negativeInt } satisfy { it < 0 }
}
```


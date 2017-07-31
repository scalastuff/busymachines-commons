# busymachines-commons-core

This module is vanilla scala _*only*_, cross-compiled against versions `2.11.11`, `2.12.3`, and `2.13.0-M2`.

Here you find very basic buildings blocks for structuring your exceptions in meaningful ways, and very basic types. Generally, we are very conservative in what we put here, and this core will become stable really fast.

## Failures (exceptions) and Errors

This library provides a `DSL` (although it's a bit of a stretch to call it that) to define, and instantiate semantically rich failures.
[Look at the scala doc in failures.scala](src/main/scala/com/busymachines/core/exceptions/failures.scala) for more information.
Essentially we have 5 types of failures (with a plural counterpart to each):

* `NotFoundFailure`
* `ForbiddenFailure`
* `DeniedFailure`
* `InvalidInputFailure`
* `ConflictFailure`

Which can be used in two differing styles.

#### Quick failure style

```scala
val option: Option[String] = ??? //...
option.getOrElse(throw NotFoundFailure)
option.getOrElse(throw NotFoundFailure("this specific option, instead of generic"))

```

#### Long term failure style
```scala
object RevolutionaryDomainFailures {
  //create a stable, and unique ID
  case object CannotBeDone extends FailureID { val name = "rd_001" }
}

case class SolutionNotFoundFailure(problem: String, attempts: Seq[String]) extends NotFoundFailure(
  s"Solution to problem $problem not found."
) {
  override def id: FailureID = RevolutionaryDomainFailures.CannotBeDone

  //you can currently associate (String, String), (String, Seq[String])
  //this compiles because of the implicit conversions in the DSL
  //these implicit conversions can be removed when Dotty comes out
  override def parameters: Parameters = Map(
    "problem" -> problem,
    "attempts" -> attempts
  )
}

object Main {
  //...
  val solutionToPVSNP: Option[Boolean] = ???
  solutionToPVSNP.getOrElse(throw SolutionNotFoundFailure("P vs. NP", Seq("1", "2", "3")))

  val solutionToHaltingProblem: Option[Boolean] = ???
  solutionToHaltingProblem.getOrElse(throw SolutionNotFoundFailure("Halting Problem", Seq("stop", "12")))
}
```

Similarly, we have predefined errors, but they are not as semantically rich.
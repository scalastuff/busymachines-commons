/**
  * Copyright (c) 2017-2018 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.effects.sync

import busymachines.core.Anomaly

import scala.collection.generic.CanBuildFrom
import scala.util.{Failure, Success}

/**
  *
  * @author Lorand Szakacs, lsz@lorandszakacs.com, lorand.szakacs@busymachines.com
  * @since 27 Jan 2018
  *
  */
trait TryTypeDefinitons {
  type Try[T] = scala.util.Try[T]

  val Try: scala.util.Try.type = scala.util.Try
}

object TrySyntax {

  /**
    *
    */
  trait Implicits {
    implicit def bmcTryCompanionObjectOps(obj: Try.type): CompanionObjectOps =
      new CompanionObjectOps(obj)

    implicit def bmcTryReferenceOps[T](value: Try[T]): ReferenceOps[T] =
      new ReferenceOps(value)

    implicit def bmcTryNestedOptionOps[T](nopt: Try[Option[T]]): NestedOptionOps[T] =
      new NestedOptionOps(nopt)

    implicit def bmcTryNestedResultOps[T](result: Try[Result[T]]): NestedResultOps[T] =
      new NestedResultOps(result)

    implicit def bmcTryBooleanOps(test: Boolean): BooleanOps =
      new BooleanOps(test)

    implicit def bmcTryNestedBooleanOps(test: Try[Boolean]): NestedBooleanOps =
      new NestedBooleanOps(test)
  }

  /**
    *
    */
  final class CompanionObjectOps(val obj: Try.type) extends AnyVal {

    /**
      * N.B. pass only pure values. Otherwise use [[Try.apply]]
      */
    @scala.inline
    def pure[T](value: T): Try[T] =
      TryOps.pure(value)

    /**
      * N.B. pass only pure values. Otherwise use [[Try.apply]]
      */
    @scala.inline
    def success[T](value: T): Try[T] =
      TryOps.success(value)

    /**
      * Failed effect but with an [[Anomaly]]
      */
    @scala.inline
    def fail[T](bad: Anomaly): Try[T] =
      TryOps.fail(bad)

    /**
      * Failed effect but with a [[Throwable]]
      */
    @scala.inline
    def failThr[T](bad: Throwable): Try[T] =
      TryOps.failThr(bad)

    /**
      * Failed effect but with an [[Anomaly]]
      */
    @scala.inline
    def failure[T](bad: Anomaly): Try[T] =
      TryOps.failure(bad)

    /**
      * Failed effect but with a [[Throwable]]
      */
    @scala.inline
    def failureThr[T](bad: Throwable): Try[T] =
      TryOps.failureThr(bad)

    @scala.inline
    def unit: Try[Unit] =
      TryOps.unit

    /**
      * Lift this [[Option]] and transform it into a failed effect if it is [[None]]
      */
    def fromOption[T](opt: Option[T], ifNone: => Anomaly): Try[T] =
      TryOps.fromOption(opt, ifNone)

    /**
      * Lift this [[Option]] and transform it into a failed effect if it is [[None]]
      */
    def fromOptionThr[T](opt: Option[T], ifNone: => Throwable): Try[T] =
      TryOps.fromOptionThr(opt, ifNone)

    /**
      * Lift this [[Either]] and transform its left-hand side into a [[Anomaly]] and sequence it within
      * this effect, yielding a failed effect.
      */
    def fromEither[L, R](either: Either[L, R], transformLeft: L => Anomaly): Try[R] =
      TryOps.fromEither(either, transformLeft)

    /**
      * Lift this [[Either]] and  sequence its left-hand-side [[Throwable]] within this effect
      * if it is a [[Throwable]].
      */
    def fromEitherThr[L, R](either: Either[L, R])(implicit ev: L <:< Throwable): Try[R] =
      TryOps.fromEitherThr(either)(ev)

    /**
      * Lift this [[Either]] and transform its left-hand side into a [[Throwable]] and sequence it within
      * this effect, yielding a failed effect.
      */
    def fromEitherThr[L, R](either: Either[L, R], transformLeft: L => Throwable): Try[R] =
      TryOps.fromEitherThr(either, transformLeft)

    /**
      *
      * Lift the [[Result]] in this effect
      * [[Incorrect]] becomes a failed effect
      * [[Correct]] becomes a pure effect
      *
      */
    def fromResult[T](result: Result[T]) =
      TryOps.fromResult(result)

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      */
    def cond[T](test: Boolean, good: => T, bad: => Anomaly): Try[T] =
      TryOps.cond(test, good, bad)

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      */
    def condThr[T](test: Boolean, good: => T, bad: => Throwable): Try[T] =
      TryOps.condThr(test, good, bad)

    /**
      * @return
      *   effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      */
    def condWith[T](test: Boolean, good: => Try[T], bad: => Anomaly): Try[T] =
      TryOps.condWith(test, good, bad)

    /**
      * @return
      *   effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      */
    def condWithThr[T](test: Boolean, good: => Try[T], bad: => Throwable): Try[T] =
      TryOps.condWithThr(test, good, bad)

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def flatCond[T](test: Try[Boolean], good: => T, bad: => Anomaly): Try[T] =
      TryOps.flatCond(test, good, bad)

    /**
      * @return
      *   effect resulted from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def flatCondThr[T](test: Try[Boolean], good: => T, bad: => Throwable): Try[T] =
      TryOps.flatCondThr(test, good, bad)

    /**
      * @return
      *   effect resulted from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def flatCondWith[T](test: Try[Boolean], good: => Try[T], bad: => Anomaly): Try[T] =
      TryOps.flatCondWith(test, good, bad)

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def flatCondWithThr[T](test: Try[Boolean], good: => Try[T], bad: => Throwable): Try[T] =
      TryOps.flatCondWithThr(test, good, bad)

    /**
      * @return
      *   Failed effect, if the boolean is true
      */
    def failOnTrue(test: Boolean, bad: => Anomaly): Try[Unit] =
      TryOps.failOnTrue(test, bad)

    /**
      * @return
      *   Failed effect, if the boolean is true
      */
    def failOnTrueThr(test: Boolean, bad: => Throwable): Try[Unit] =
      TryOps.failOnTrueThr(test, bad)

    /**
      * @return
      *   Failed effect, if the boolean is false
      */
    def failOnFalse(test: Boolean, bad: => Anomaly): Try[Unit] =
      TryOps.failOnFalse(test, bad)

    /**
      * @return
      *   Failed effect, if the boolean is false
      */
    def failOnFalseThr(test: Boolean, bad: => Throwable): Try[Unit] =
      TryOps.failOnFalseThr(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is true, or if the original effect is failed
      */
    def flatFailOnTrue(test: Try[Boolean], bad: => Anomaly): Try[Unit] =
      TryOps.flatFailOnTrue(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is true, or if the original effect is failed
      */
    def flatFailOnTrueThr(test: Try[Boolean], bad: => Throwable): Try[Unit] =
      TryOps.flatFailOnTrueThr(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is false, or if the original effect is failed
      */
    def flatFailOnFalse(test: Try[Boolean], bad: => Anomaly): Try[Unit] =
      TryOps.flatFailOnFalse(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is false, or if the original effect is failed
      */
    def flatFailOnFalseThr(test: Try[Boolean], bad: => Throwable): Try[Unit] =
      TryOps.flatFailOnFalseThr(test, bad)

    /**
      * Sequences the given [[Anomaly]] if Option is [[None]] into this effect
      *
      * The failure of this effect takes precedence over the given failure
      */
    def unpackOption[T](nopt: Try[Option[T]], ifNone: => Anomaly): Try[T] =
      TryOps.unpackOption(nopt, ifNone)

    /**
      * Sequences the given [[Throwable]] if Option is [[None]] into this effect
      *
      * The failure of this effect takes precedence over the given failure
      */
    def unpackOptionThr[T](nopt: Try[Option[T]], ifNone: => Throwable): Try[T] =
      TryOps.unpackOptionThr(nopt, ifNone)

    /**
      * Sequences the failure of the [[Incorrect]] [[Result]] into this effect.
      *
      * The failure of this effect takes precedence over the failure of the [[Incorrect]] value.
      */
    def unpackResult[T](value: Try[Result[T]]): Try[T] =
      TryOps.unpackResult(value)

    /**
      * !!! USE WITH CARE !!!
      *
      * Throws exceptions into your face
      *
      */
    def asOptionUnsafe[T](value: Try[T]): Option[T] =
      TryOps.asOptionUnsafe(value)

    /**
      * !!! USE WITH CARE !!!
      *
      * Throws exceptions into your face
      *
      */
    def asListUnsafe[T](value: Try[T]): List[T] =
      TryOps.asListUnsafe(value)

    /**
      * Returns `Left` with `Throwable` if this is a `Failure`, otherwise returns `Right` with `Success` value.
      */
    def asEither[T](value: Try[T]): Either[Throwable, T] =
      value.toEither

    /**
      *
      * Tranforms this Try into a [[Result]]. If the [[Throwable]] of
      * the [[scala.util.Failure]] case is also an [[Anomaly]] then
      * its type is preserved, and simply mapped to a [[Incorrect]].
      *
      * If it is not an [[Anomaly]] then it is wrapped in a
      * [[busymachines.core.CatastrophicError]]. So be careful with
      * this transformation.
      *
      */
    def asResult[T](value: Try[T]): Result[T] =
      TryOps.asResult(value)

    /**
      * !!! USE WITH CARE !!!
      *
      * Will throw exceptions in your face if the underlying effect is failed
      */
    def unsafeGet[T](value: Try[T]): T =
      TryOps.unsafeGet(value)

    //===========================================================================
    //============================== Transformers ===============================
    //===========================================================================

    /**
      * Used to transform both the "pure" part of the effect, and the "fail" part. Hence the name
      * "bi" map, because it also allows you to change both branches of the effect, not just the
      * happy path.
      */
    def bimap[T, R](value: Try[T], good: T => R, bad: Throwable => Anomaly): Try[R] =
      TryOps.bimap(value, good, bad)

    /**
      * Similar to the overload, but the [[Correct]] branch of the result is used to change the "pure" branch of this
      * effect, and [[Incorrect]] branch is used to change the "fail" branch of the effect.
      *
      * The overload that uses [[Throwable]] instead of [[Anomaly]]
      */
    def bimapThr[T, R](value: Try[T], good: T => R, bad: Throwable => Throwable): Try[R] =
      TryOps.bimapThr(value, good, bad)

    /**
      *
      * Given the basic two-pronged nature of this effect.
      * the ``good`` function transforms the underlying "pure" (correct, successful, etc) if that's the case.
      * the ``bad`` function transforms the underlying "failure" part of the effect into a "pure" part.
      *
      * Therefore, by using ``morph`` you are defining the rules by which to make the effect into a successful one
      * that does not short-circuit monadic flatMap chains.
      *
      * e.g:
      * {{{
      *   val f: Future[Int] = Future.fail(InvalidInputFailure)
      *   Future.morph(f, (i: Int) => i *2, (t: Throwable) => 42)
      * }}}
      *
      * Undefined behavior if you throw exceptions in the method. DO NOT do that!
      */
    def morph[T, R](value: Try[T], good: T => R, bad: Throwable => R): Try[R] =
      TryOps.morph(value, good, bad)

    /**
      *
      * Explicitely discard the contents of this effect, and return [[Unit]] instead.
      *
      * N.B. thecomputation captured within this effect are still executed,
      * it's just the final value that is discarded
      *
      */
    def discardContent[_](value: Try[_]): Try[Unit] =
      TryOps.discardContent(value)

    //=========================================================================
    //=============================== Traversals ==============================
    //=========================================================================

    /**
      * see:
      * https://typelevel.org/cats/api/cats/Traverse.html
      *
      * {{{
      *   def indexToFilename(i: Int): Try[String] = ???
      *
      *   val fileIndex: List[Int] = List(0,1,2,3,4)
      *   val fileNames: Try[List[String]] = Try.traverse(fileIndex){ i =>
      *     indexToFilename(i)
      *   }
      * }}}
      */
    def traverse[A, B, C[X] <: TraversableOnce[X]](col: C[A])(fn: A => Try[B])(
      implicit
      cbf: CanBuildFrom[C[A], B, C[B]]
    ): Try[C[B]] = TryOps.traverse(col)(fn)

    /**
      * see:
      * https://typelevel.org/cats/api/cats/Traverse.html
      *
      * Specialized case of [[traverse]]
      *
      * {{{
      *   def indexToFilename(i: Int): Try[String] = ???
      *
      *   val fileNamesTry: List[Try[String]] = List(0,1,2,3,4).map(indexToFileName)
      *   val fileNames:    Try[List[String]] = Try.sequence(fileNamesTry)
      * }}}
      */
    def sequence[A, M[X] <: TraversableOnce[X]](in: M[Try[A]])(
      implicit
      cbf: CanBuildFrom[M[Try[A]], A, M[A]]
    ): Try[M[A]] = TryOps.sequence(in)

  }

  /**
    *
    */
  final class ReferenceOps[T](val value: Try[T]) extends AnyVal {

    /**
      * !!! USE WITH CARE !!!
      *
      * Throws exceptions into your face
      *
      */
    def asOptionUnsafe(): Option[T] =
      TryOps.asOptionUnsafe(value)

    /**
      * !!! USE WITH CARE !!!
      *
      * Throws exceptions into your face
      *
      */
    def asListUnsafe(): List[T] =
      TryOps.asListUnsafe(value)

    /**
      * Returns `Left` with `Throwable` if this is a `Failure`, otherwise returns `Right` with `Success` value.
      */
    def asEither: Either[Throwable, T] =
      value.toEither

    /**
      *
      * Tranforms this Try into a [[Result]]. If the [[Throwable]] of
      * the [[scala.util.Failure]] case is also an [[Anomaly]] then
      * its type is preserved, and simply mapped to a [[Incorrect]].
      *
      * If it is not an [[Anomaly]] then it is wrapped in a
      * [[busymachines.core.CatastrophicError]]. So be careful with
      * this transformation.
      *
      */
    def asResult: Result[T] =
      TryOps.asResult(value)

    /**
      * !!! USE WITH CARE !!!
      *
      * Will throw exceptions in your face if the underlying effect is failed
      */
    def unsafeGet(): T =
      TryOps.unsafeGet(value)

    /**
      * Used to transform both the "pure" part of the effect, and the "fail" part. Hence the name
      * "bi" map, because it also allows you to change both branches of the effect, not just the
      * happy path.
      */
    def bimap[R](good: T => R, bad: Throwable => Anomaly): Try[R] =
      TryOps.bimap(value, good, bad)

    /**
      * Similar to the overload, but the [[Correct]] branch of the result is used to change the "pure" branch of this
      * effect, and [[Incorrect]] branch is used to change the "fail" branch of the effect.
      *
      * The overload that uses [[Throwable]] instead of [[Anomaly]]
      */
    def bimapThr[R](good: T => R, bad: Throwable => Throwable): Try[R] =
      TryOps.bimapThr(value, good, bad)

    /**
      *
      * Given the basic two-pronged nature of this effect.
      * the ``good`` function transforms the underlying "pure" (correct, successful, etc) if that's the case.
      * the ``bad`` function transforms the underlying "failure" part of the effect into a "pure" part.
      *
      * Therefore, by using ``morph`` you are defining the rules by which to make the effect into a successful one
      * that does not short-circuit monadic flatMap chains.
      *
      * e.g:
      * {{{
      *   val f: Future[Int] = Future.fail(InvalidInputFailure)
      *   Future.morph(f, (i: Int) => i *2, (t: Throwable) => 42)
      * }}}
      *
      * Undefined behavior if you throw exceptions in the method. DO NOT do that!
      */
    def morph[R](good: T => R, bad: Throwable => R): Try[R] =
      TryOps.morph(value, good, bad)

    /**
      *
      * Explicitely discard the contents of this effect, and return [[Unit]] instead.
      *
      * N.B. thecomputation captured within this effect are still executed,
      * it's just the final value that is discarded
      *
      */
    def discardContent: Try[Unit] =
      TryOps.discardContent(value)
  }

  /**
    *
    *
    */
  final class NestedOptionOps[T](val nopt: Try[Option[T]]) extends AnyVal {

    /**
      * Sequences the given [[Anomaly]] if Option is [[None]] into this effect
      *
      * The failure of this effect takes precedence over the given failure
      */
    def unpack(ifNone: => Anomaly): Try[T] =
      TryOps.unpackOption(nopt, ifNone)

    /**
      * Sequences the given [[Throwable]] if Option is [[None]] into this effect
      *
      * The failure of this effect takes precedence over the given failure
      */
    def unpackThr(ifNone: => Throwable): Try[T] =
      TryOps.unpackOptionThr(nopt, ifNone)
  }

  /**
    *
    */
  final class NestedResultOps[T](val result: Try[Result[T]]) extends AnyVal {

    /**
      * Sequences the failure of the [[Incorrect]] [[Result]] into this effect.
      *
      * The failure of this effect takes precedence over the failure of the [[Incorrect]] value.
      */
    def unpack: Try[T] =
      TryOps.unpackResult(result)
  }

  /**
    *
    *
    */
  final class BooleanOps(val test: Boolean) extends AnyVal {

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      */
    def condTry[T](good: => T, bad: => Anomaly): Try[T] =
      TryOps.cond(test, good, bad)

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      */
    def condTryThr[T](good: => T, bad: => Throwable): Try[T] =
      TryOps.condThr(test, good, bad)

    /**
      * @return
      *   effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      */
    def condWithTry[T](good: => Try[T], bad: => Anomaly): Try[T] =
      TryOps.condWith(test, good, bad)

    /**
      * @return
      *   effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      */
    def condWithTryThr[T](good: => Try[T], bad: => Throwable): Try[T] =
      TryOps.condWithThr(test, good, bad)

    /**
      * @return
      *   Failed effect, if the boolean is true
      */
    def failOnTrueTry(bad: => Anomaly): Try[Unit] =
      TryOps.failOnTrue(test, bad)

    /**
      * @return
      *   Failed effect, if the boolean is true
      */
    def failOnTrueTryThr(bad: => Throwable): Try[Unit] =
      TryOps.failOnTrueThr(test, bad)

    /**
      * @return
      *   Failed effect, if the boolean is false
      */
    def failOnFalseTry(bad: => Anomaly): Try[Unit] =
      TryOps.failOnFalse(test, bad)

    /**
      * @return
      *   Failed effect, if the boolean is false
      */
    def failOnFalseTryThr(bad: => Throwable): Try[Unit] =
      TryOps.failOnFalseThr(test, bad)

  }

  /**
    *
    *
    */
  final class NestedBooleanOps(val test: Try[Boolean]) extends AnyVal {

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def cond[T](good: => T, bad: => Anomaly): Try[T] =
      TryOps.flatCond(test, good, bad)

    /**
      * @return
      *   effect resulted from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def condThr[T](good: => T, bad: => Throwable): Try[T] =
      TryOps.flatCondThr(test, good, bad)

    /**
      * @return
      *   effect resulted from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Anomaly]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def condWith[T](good: => Try[T], bad: => Anomaly): Try[T] =
      TryOps.flatCondWith(test, good, bad)

    /**
      * @return
      *   pure effect from ``good`` if the boolean is true
      *   failed effect with ``bad`` [[Throwable]] if boolean is false
      *   failed effect if the effect wrapping the boolean is already failed
      */
    def condWithThr[T](good: => Try[T], bad: => Throwable): Try[T] =
      TryOps.flatCondWithThr(test, good, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is true, or if the original effect is failed
      */
    def failOnTrue(bad: => Anomaly): Try[Unit] =
      TryOps.flatFailOnTrue(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is true, or if the original effect is failed
      */
    def failOnTrueThr(bad: => Throwable): Try[Unit] =
      TryOps.flatFailOnTrueThr(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is false, or if the original effect is failed
      */
    def failOnFalse(bad: => Anomaly): Try[Unit] =
      TryOps.flatFailOnFalse(test, bad)

    /**
      * @return
      *   Failed effect, if the boxed boolean is false, or if the original effect is failed
      */
    def failOnFalseThr(bad: => Throwable): Try[Unit] =
      TryOps.flatFailOnFalseThr(test, bad)

  }
}

/**
  *
  */
object TryOps {
  //===========================================================================
  //========================== Primary constructors ===========================
  //===========================================================================

  /**
    * N.B. pass only pure values. Otherwise use [[Try.apply]]
    */
  @scala.inline
  def pure[T](t: T): Try[T] =
    Success(t)

  /**
    * N.B. pass only pure values. Otherwise use [[Try.apply]]
    */
  @scala.inline
  def success[T](t: T): Try[T] =
    Success(t)

  /**
    * Failed effect but with an [[Anomaly]]
    */
  @scala.inline
  def fail[T](bad: Anomaly): Try[T] =
    Failure(bad.asThrowable)

  /**
    * Failed effect but with an [[Anomaly]]
    */
  @scala.inline
  def failure[T](bad: Anomaly): Try[T] =
    Failure(bad.asThrowable)

  /**
    * Failed effect but with a [[Throwable]]
    */
  @scala.inline
  def failThr[T](thr: Throwable): Try[T] =
    Failure(thr)

  /**
    * Failed effect but with a [[Throwable]]
    */
  @scala.inline
  def failureThr[T](thr: Throwable): Try[T] =
    Failure(thr)

  val unit: Try[Unit] =
    Success(())

  // —— apply delegates to Try.apply directly in syntax object

  //===========================================================================
  //==================== Try from various other effects =======================
  //===========================================================================

  /**
    * Lift this [[Option]] and transform it into a failed effect if it is [[None]]
    */
  def fromOption[T](opt: Option[T], ifNone: => Anomaly): Try[T] = opt match {
    case None        => TryOps.fail(ifNone)
    case Some(value) => TryOps.pure(value)
  }

  /**
    * Lift this [[Option]] and transform it into a failed effect if it is [[None]]
    */
  def fromOptionThr[T](opt: Option[T], ifNone: => Throwable): Try[T] = opt match {
    case None        => TryOps.failThr(ifNone)
    case Some(value) => TryOps.pure(value)
  }

  /**
    * Lift this [[Either]] and  sequence its left-hand-side [[Throwable]] within this effect
    * if it is a [[Throwable]].
    */
  def fromEitherThr[L, R](either: Either[L, R])(implicit ev: L <:< Throwable): Try[R] =
    either.toTry(ev)

  /**
    * Lift this [[Either]] and transform its left-hand side into a [[Anomaly]] and sequence it within
    * this effect, yielding a failed effect.
    */
  def fromEither[L, R](either: Either[L, R], transformLeft: L => Anomaly): Try[R] = either match {
    case Left(left)   => TryOps.fail(transformLeft(left))
    case Right(value) => TryOps.pure(value)
  }

  /**
    * Lift this [[Either]] and transform its left-hand side into a [[Throwable]] and sequence it within
    * this effect, yielding a failed effect.
    */
  def fromEitherThr[L, R](either: Either[L, R], transformLeft: L => Throwable): Try[R] = either match {
    case Left(left)   => TryOps.failThr(transformLeft(left))
    case Right(value) => TryOps.pure(value)
  }

  /**
    *
    * Lift the [[Result]] in this effect
    * [[Incorrect]] becomes a failed effect
    * [[Correct]] becomes a pure effect
    *
    */
  def fromResult[T](r: Result[T]): Try[T] = r match {
    case Correct(value)     => TryOps.pure(value)
    case Incorrect(anomaly) => TryOps.fail(anomaly)
  }

  //===========================================================================
  //======================== Try from special cased Try =======================
  //===========================================================================

  /**
    * @return
    *   pure effect from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Anomaly]] if boolean is false
    */
  def cond[T](test: Boolean, good: => T, bad: => Anomaly): Try[T] =
    if (test) TryOps.pure(good) else TryOps.fail(bad)

  /**
    * @return
    *   pure effect from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Throwable]] if boolean is false
    */
  def condThr[T](test: Boolean, good: => T, bad: => Throwable): Try[T] =
    if (test) TryOps.pure(good) else TryOps.failThr(bad)

  /**
    * @return
    *   effect from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Anomaly]] if boolean is false
    */
  def condWith[T](test: Boolean, good: => Try[T], bad: => Anomaly): Try[T] =
    if (test) good else TryOps.fail(bad)

  /**
    * @return
    *   effect from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Throwable]] if boolean is false
    */
  def condWithThr[T](test: Boolean, good: => Try[T], bad: => Throwable): Try[T] =
    if (test) good else TryOps.failThr(bad)

  /**
    * @return
    *   Failed effect, if the boolean is true
    */
  def failOnTrue(test: Boolean, bad: => Anomaly): Try[Unit] =
    if (test) TryOps.fail(bad) else TryOps.unit

  /**
    * @return
    *   Failed effect, if the boolean is true
    */
  def failOnTrueThr(test: Boolean, bad: => Throwable): Try[Unit] =
    if (test) TryOps.failThr(bad) else TryOps.unit

  /**
    * @return
    *   Failed effect, if the boolean is false
    */
  def failOnFalse(test: Boolean, bad: => Anomaly): Try[Unit] =
    if (!test) TryOps.fail(bad) else TryOps.unit

  /**
    * @return
    *   Failed effect, if the boolean is false
    */
  def failOnFalseThr(test: Boolean, bad: => Throwable): Try[Unit] =
    if (!test) TryOps.failThr(bad) else TryOps.unit

  /**
    * @return
    *   pure effect from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Anomaly]] if boolean is false
    *   failed effect if the effect wrapping the boolean is already failed
    */
  def flatCond[T](test: Try[Boolean], good: => T, bad: => Anomaly): Try[T] =
    test.flatMap(b => TryOps.cond(b, good, bad))

  /**
    * @return
    *   effect resulted from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Throwable]] if boolean is false
    *   failed effect if the effect wrapping the boolean is already failed
    */
  def flatCondThr[T](test: Try[Boolean], good: => T, bad: => Throwable): Try[T] =
    test.flatMap(b => TryOps.condThr(b, good, bad))

  /**
    * @return
    *   effect resulted from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Anomaly]] if boolean is false
    *   failed effect if the effect wrapping the boolean is already failed
    */
  def flatCondWith[T](test: Try[Boolean], good: => Try[T], bad: => Anomaly): Try[T] =
    test.flatMap(b => TryOps.condWith(b, good, bad))

  /**
    * @return
    *   pure effect from ``good`` if the boolean is true
    *   failed effect with ``bad`` [[Throwable]] if boolean is false
    *   failed effect if the effect wrapping the boolean is already failed
    */
  def flatCondWithThr[T](test: Try[Boolean], good: => Try[T], bad: => Throwable): Try[T] =
    test.flatMap(b => TryOps.condWithThr(b, good, bad))

  /**
    * @return
    *   Failed effect, if the boxed boolean is true, or if the original effect is failed
    */
  def flatFailOnTrue(test: Try[Boolean], bad: => Anomaly): Try[Unit] =
    test.flatMap(b => if (b) TryOps.fail(bad) else TryOps.unit)

  /**
    * @return
    *   Failed effect, if the boxed boolean is true, or if the original effect is failed
    */
  def flatFailOnTrueThr(test: Try[Boolean], bad: => Throwable): Try[Unit] =
    test.flatMap(b => if (b) TryOps.failThr(bad) else TryOps.unit)

  /**
    * @return
    *   Failed effect, if the boxed boolean is false, or if the original effect is failed
    */
  def flatFailOnFalse(test: Try[Boolean], bad: => Anomaly): Try[Unit] =
    test.flatMap(b => if (!b) TryOps.fail(bad) else TryOps.unit)

  /**
    * @return
    *   Failed effect, if the boxed boolean is false, or if the original effect is failed
    */
  def flatFailOnFalseThr(test: Try[Boolean], bad: => Throwable): Try[Unit] =
    test.flatMap(b => if (!b) TryOps.failThr(bad) else TryOps.unit)

  /**
    * Sequences the given [[Anomaly]] if Option is [[None]] into this effect
    *
    * The failure of this effect takes precedence over the given failure
    */
  def unpackOption[T](nopt: Try[Option[T]], ifNone: => Anomaly): Try[T] =
    nopt.flatMap(opt => TryOps.fromOption(opt, ifNone))

  /**
    * Sequences the given [[Throwable]] if Option is [[None]] into this effect
    *
    * The failure of this effect takes precedence over the given failure
    */
  def unpackOptionThr[T](nopt: Try[Option[T]], ifNone: => Throwable): Try[T] =
    nopt.flatMap(opt => TryOps.fromOptionThr(opt, ifNone))

  /**
    * Sequences the failure of the [[Incorrect]] [[Result]] into this effect.
    *
    * The failure of this effect takes precedence over the failure of the [[Incorrect]] value.
    */
  def unpackResult[T](result: Try[Result[T]]): Try[T] =
    result.flatMap(r => TryOps.fromResult(r))

  //===========================================================================
  //========================== Try to various effects =========================
  //===========================================================================

  /**
    * !!! USE WITH CARE !!!
    *
    * Throws exceptions into your face
    *
    */
  @scala.inline
  def asOptionUnsafe[T](value: Try[T]): Option[T] = value match {
    case Failure(exception) => throw exception
    case Success(value)     => Option(value)
  }

  /**
    * !!! USE WITH CARE !!!
    *
    * Throws exceptions into your face
    *
    */
  @scala.inline
  def asListUnsafe[T](value: Try[T]): List[T] = value match {
    case Failure(exception) => throw exception
    case Success(value)     => List(value)
  }

  // —— asEither —— is aliased to Try#toEither directly in syntax classes

  /**
    *
    * Tranforms this Try into a [[Result]]. If the [[Throwable]] of
    * the [[scala.util.Failure]] case is also an [[Anomaly]] then
    * its type is preserved, and simply mapped to a [[Incorrect]].
    *
    * If it is not an [[Anomaly]] then it is wrapped in a
    * [[busymachines.core.CatastrophicError]]. So be careful with
    * this transformation.
    *
    */
  @scala.inline
  def asResult[T](value: Try[T]): Result[T] = Result.fromTry(value)

  /**
    * !!! USE WITH CARE !!!
    *
    * Will throw exceptions in your face if the underlying effect is failed
    */
  @scala.inline
  def unsafeGet[T](value: Try[T]): T =
    value.get

  //===========================================================================
  //============================== Transformers ===============================
  //===========================================================================

  /**
    * Used to transform both the "pure" part of the effect, and the "fail" part. Hence the name
    * "bi" map, because it also allows you to change both branches of the effect, not just the
    * happy path.
    */
  def bimap[T, R](value: Try[T], good: T => R, bad: Throwable => Anomaly): Try[R] = value match {
    case Failure(t) => TryOps.fail(bad(t))
    case Success(v) => TryOps.pure(good(v))
  }

  /**
    * Similar to the overload, but the [[Correct]] branch of the result is used to change the "pure" branch of this
    * effect, and [[Incorrect]] branch is used to change the "fail" branch of the effect.
    *
    * The overload that uses [[Throwable]] instead of [[Anomaly]]
    */
  def bimapThr[T, R](value: Try[T], good: T => R, bad: Throwable => Throwable): Try[R] = value match {
    case Failure(t) => TryOps.failThr(bad(t))
    case Success(v) => TryOps.pure(good(v))
  }

  /**
    *
    * Given the basic two-pronged nature of this effect.
    * the ``good`` function transforms the underlying "pure" (correct, successful, etc) if that's the case.
    * the ``bad`` function transforms the underlying "failure" part of the effect into a "pure" part.
    *
    * Therefore, by using ``morph`` you are defining the rules by which to make the effect into a successful one
    * that does not short-circuit monadic flatMap chains.
    *
    * e.g:
    * {{{
    *   val t: Try[Int] = Try.fail(InvalidInputFailure)
    *   Try.morph(t, (i: Int) => i *2, (t: Throwable) => 42)
    * }}}
    *
    * Undefined behavior if you throw exceptions in the method. DO NOT do that!
    */
  def morph[T, R](value: Try[T], good: T => R, bad: Throwable => R): Try[R] = value match {
    case Failure(value) => TryOps.pure(bad(value))
    case Success(value) => TryOps.pure(good(value))
  }

  /**
    *
    * Explicitely discard the contents of this effect, and return [[Unit]] instead.
    *
    * N.B. thecomputation captured within this effect are still executed,
    * it's just the final value that is discarded
    *
    */
  def discardContent[T](value: Try[T]): Try[Unit] =
    value.map(UnitFunction)

  //=========================================================================
  //=============================== Traversals ==============================
  //=========================================================================

  /**
    * see:
    * https://typelevel.org/cats/api/cats/Traverse.html
    *
    * {{{
    *   def indexToFilename(i: Int): Try[String] = ???
    *
    *   val fileIndex: List[Int] = List(0,1,2,3,4)
    *   val fileNames: Try[List[String]] = Try.traverse(fileIndex){ i =>
    *     indexToFilename(i)
    *   }
    * }}}
    */
  def traverse[A, B, C[X] <: TraversableOnce[X]](col: C[A])(fn: A => Try[B])(
    implicit
    cbf: CanBuildFrom[C[A], B, C[B]]
  ): Try[C[B]] = {
    import scala.collection.mutable
    if (col.isEmpty) {
      TryOps.pure(cbf.apply().result())
    }
    else {
      val seq  = col.toSeq
      val head = seq.head
      val tail = seq.tail
      val builder: mutable.Builder[B, C[B]] = cbf.apply()
      val firstBuilder = fn(head) map { z =>
        builder.+=(z)
      }
      val eventualBuilder: Try[mutable.Builder[B, C[B]]] = tail.foldLeft(firstBuilder) {
        (serializedBuilder: Try[mutable.Builder[B, C[B]]], element: A) =>
          serializedBuilder flatMap [mutable.Builder[B, C[B]]] { (result: mutable.Builder[B, C[B]]) =>
            val f: Try[mutable.Builder[B, C[B]]] = fn(element) map { newElement =>
              result.+=(newElement)
            }
            f
          }
      }
      eventualBuilder map { b =>
        b.result()
      }
    }
  }

  /**
    * see:
    * https://typelevel.org/cats/api/cats/Traverse.html
    *
    * Specialized case of [[traverse]]
    *
    * {{{
    *   def indexToFilename(i: Int): Try[String] = ???
    *
    *   val fileNamesTry: List[Try[String]] = List(0,1,2,3,4).map(indexToFileName)
    *   val fileNames:    Try[List[String]] = Try.sequence(fileNamesTry)
    * }}}
    */
  def sequence[A, M[X] <: TraversableOnce[X]](in: M[Try[A]])(
    implicit
    cbf: CanBuildFrom[M[Try[A]], A, M[A]]
  ): Try[M[A]] = TryOps.traverse(in)(identity)

  //=========================================================================
  //=============================== Constants ===============================
  //=========================================================================

  private val UnitFunction: Any => Unit = _ => ()
}

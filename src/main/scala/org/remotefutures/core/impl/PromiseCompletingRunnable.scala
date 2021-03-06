/*
 * Copyright (c) 2014 Martin Senne, Marvin Hansen.
 */
package org.remotefutures.core.impl

import org.remotefutures.util.Debug._
import scala.concurrent.Promise
import scala.util.{Failure, Success}
import scala.util.control.NonFatal


/**
 * A runnable wrapping the execution of fnc, whose result is put the a promise.
 *
 * Shamelessly copied from scala.concurrent.impl.Future and modified.
 *
 * @param body the fnc to execute
 * @param promise is the promise to put the result of type T into
 * @tparam T return type of this distributed future.
 */
class PromiseCompletingRunnable[T](body: () => T, val promise: Promise[T]) extends PromiseBackedRunnable[T] {

  implicit val DEBUG = false

  override def run() = {
    promise complete {
      try {
        printDbg("Before execution of fnc on " + this)
        val result: T = body() // execute fnc
        printDbg("After execution of fnc on " + this)
        Success(result)

      } catch {
        case NonFatal(e) => Failure(e)
      }
    }
  }
}

/**
 * A runnable which holds a promise, which is completed / written to from within run().
 * @tparam T is the type of the promise.
 */
abstract class PromiseBackedRunnable[T] extends Runnable {

  /**
   * The promise that is written to in order to return back a value
   */
  val promise : Promise[T]
}




package org.remotefutures.core.impl.executor

import org.remotefutures.core.RemoteExecutor
import scala.concurrent.{ExecutionContext, Promise}

/**
 * An remote executor which runs the function fnc in a Runnable on the local ExecutionContext
 */
protected[core] class LocalRunningRemoteExecutor extends RemoteExecutor {

  override def execute[C, T](fnc: () => T, fncContext: C, promise: Promise[T]): Unit = {
    println("This is execute of LocalRunnableRemoteExecutor.")

    import scala.concurrent.ExecutionContext.Implicits.global

    implicit val executor: ExecutionContext = global

    val runnable = new PromiseCompletingRunnable(fnc, promise)
    executor.prepare().execute(runnable)
  }

}

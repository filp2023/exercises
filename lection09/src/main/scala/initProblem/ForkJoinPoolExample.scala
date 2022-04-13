package initProblem

import java.util.concurrent.RecursiveTask

object ForkJoinPoolExample extends App {

  case class Node[A](value: A, children: List[Node[A]] = Nil)

  case class TreeComputing(tree: Node[Int]) extends RecursiveTask[Int] {
    override def compute(): Int = {
      println(Thread.currentThread().getName)
      println(forkJoinPool.getPoolSize)
      tree.value +
        tree
          .children
          .map(node => TreeComputing(node).fork())
          .foldLeft(0)((acc, task) => acc + task.join())
    }
  }


  val tree = Node(1, List(Node(2),Node(2),Node(2), Node(2),Node(2),Node(2),Node(2)))

  import java.util.concurrent.ForkJoinPool

  val forkJoinPool = ForkJoinPool.commonPool

  val sum = forkJoinPool.invoke(TreeComputing(tree))

  println(sum)
}

package com.iamsmkr

import scala.beans.BeanProperty

package object servicediscovery {

  val zookeeperAddress = "localhost:2181"
  val ServiceDiscoveryAtomicPath = "/flightServers"

  case class ServiceDetail(@BeanProperty var partitionId: Int) {
    def this() = this(0)
  }

}

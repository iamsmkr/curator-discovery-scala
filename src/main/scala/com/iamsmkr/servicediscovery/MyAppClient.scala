package com.iamsmkr.servicediscovery

import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder

object MyAppClient extends App {

  val zkClient =
    CuratorFrameworkFactory
      .builder()
      .connectString(zookeeperAddress)
      .retryPolicy(new ExponentialBackoffRetry(1000, 3))
      .build()
  zkClient.start()
  zkClient.blockUntilConnected()

  val serviceDiscovery =
    ServiceDiscoveryBuilder
      .builder(classOf[ServiceDetail])
      .client(zkClient)
      .basePath(ServiceDiscoveryAtomicPath)
      .build()

  serviceDiscovery.start()

  import scala.jdk.CollectionConverters._

  val services = serviceDiscovery.queryForInstances("flightServer").asScala
  services.foreach(i => {
    println(s"${i.getAddress},${i.getPort},${i.getPayload.partitionId}")
  })

  serviceDiscovery.close()
  zkClient.close()
}

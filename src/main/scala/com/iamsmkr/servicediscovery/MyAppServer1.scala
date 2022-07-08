package com.iamsmkr.servicediscovery

import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder
import org.apache.curator.x.discovery.ServiceInstance
import org.apache.curator.x.discovery.details.JsonInstanceSerializer

import java.net.InetAddress

object MyAppServer1 extends App {
  val partitionId = 1

  val zkClient =
    CuratorFrameworkFactory
      .builder()
      .connectString(zookeeperAddress)
      .retryPolicy(new ExponentialBackoffRetry(1000, 3))
      .build()

  zkClient.start()
  zkClient.blockUntilConnected()

  val instance =
    ServiceInstance
      .builder()
      .address(InetAddress.getLocalHost.getHostAddress)
      .port(8866)
      .name("flightServer")
      .payload(ServiceDetail(partitionId))
      .build()

  val serviceDiscovery =
    ServiceDiscoveryBuilder
      .builder(classOf[ServiceDetail])
      .client(zkClient)
      .serializer(new JsonInstanceSerializer[ServiceDetail](classOf[ServiceDetail]))
      .basePath(ServiceDiscoveryAtomicPath)
      .build()

  serviceDiscovery.registerService(instance)
  serviceDiscovery.start()

  Thread.sleep(60000)

  serviceDiscovery.close()
  zkClient.close()
}

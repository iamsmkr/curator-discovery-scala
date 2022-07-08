package com.iamsmkr.servicediscovery

import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder
import org.apache.curator.x.discovery.ServiceInstance
import org.apache.curator.x.discovery.details.JsonInstanceSerializer

import java.net.InetAddress

object MyAppServer0 extends App {
  val partitionId = 0

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
      .serializer(new JsonInstanceSerializer[ServiceDetail](classOf[ServiceDetail]))
      .basePath(ServiceDiscoveryAtomicPath)
      .build()

  val instance =
    ServiceInstance.builder()
      .address(InetAddress.getLocalHost.getHostAddress)
      .port(8855)
      .name("flightServer")
      .payload(ServiceDetail(partitionId)).build()

  val instance2 =
    ServiceInstance.builder()
      .address(InetAddress.getLocalHost.getHostAddress)
      .port(8877)
      .name("flightServer")
      .payload(ServiceDetail(partitionId)).build()

  serviceDiscovery.registerService(instance)
  serviceDiscovery.registerService(instance2)
  serviceDiscovery.start()

  Thread.sleep(60000)

  serviceDiscovery.close()
  zkClient.close()

}

Peergreen MBeans Commands
=========================

Ease MBeanServer (platform's default one) manipulation:

* Browse MBeans
* List domains
* Display MBean's structure (MBeanInfo)
* Display MBeans's attribute's values

Installation
------------------
Install with the following command:

    start mvn:com.peergreen.shelbie/mbeans-commands/1.0.0-SNAPSHOT


Commands
------------------

### mbeans:list-objectnames
List registered ObjectNames.

#### No arguments
All ObjectNames are returned

    guillaume@peergreen-platform$ mbeans:list-objectnames
      java.lang:name=PS Eden Space,type=MemoryPool
      java.lang:type=Memory
      org.eclipse.equinox.region.domain:frameworkUUID=80edf5e9-bd8d-0012-1d07-a5591531df55,name=org.eclipse.equinox.region.kernel,type=Region
      java.lang:name=PS Survivor Space,type=MemoryPool
      org.eclipse.equinox.region.domain:frameworkUUID=80edf5e9-bd8d-0012-1d07-a5591531df55,type=RegionDigraph
      java.lang:name=Code Cache,type=MemoryPool
      java.lang:name=PS MarkSweep,type=GarbageCollector
      java.lang:type=Runtime
      java.nio:name=direct,type=BufferPool
      java.lang:type=ClassLoading
      java.lang:type=Threading
      java.nio:name=mapped,type=BufferPool
      java.util.logging:type=Logging
      java.lang:type=Compilation
      com.sun.management:type=HotSpotDiagnostic
      java.lang:name=PS Perm Gen,type=MemoryPool
      java.lang:type=OperatingSystem
      java.lang:name=PS Scavenge,type=GarbageCollector
      java.lang:name=PS Old Gen,type=MemoryPool
      JMImplementation:type=MBeanServerDelegate
      java.lang:name=CodeCacheManager,type=MemoryManager

#### With ObjectName pattern as argument
Only ObjectNames matching the given pattern are returned.

    guillaume@peergreen-platform$ mbeans:list-objectnames java.lang:*
      java.lang:type=Compilation
      java.lang:type=Memory
      java.lang:name=PS Eden Space,type=MemoryPool
      java.lang:name=PS Survivor Space,type=MemoryPool
      java.lang:name=Code Cache,type=MemoryPool
      java.lang:name=PS MarkSweep,type=GarbageCollector
      java.lang:type=Runtime
      java.lang:name=PS Perm Gen,type=MemoryPool
      java.lang:type=ClassLoading
      java.lang:name=PS Scavenge,type=GarbageCollector
      java.lang:type=OperatingSystem
      java.lang:type=Threading
      java.lang:name=PS Old Gen,type=MemoryPool
      java.lang:name=CodeCacheManager,type=MemoryManager

### mbeans:list-domains
List MBean's domain.

    guillaume@peergreen-platform$ mbeans:list-domains
      java.nio (2 MBeans)
      JMImplementation (1 MBeans)
      com.sun.management (1 MBeans)
      java.lang (14 MBeans)
      java.util.logging (1 MBeans)
      org.eclipse.equinox.region.domain (2 MBeans)

### mbeans:mbean-info
Display informations about a given MBean (`ObjectName` provided as argument).

    guillaume@peergreen-platform$ mbeans:mbean-info java.lang:type=Memory
    ----------------------------------------------------
    java.lang:type=Memory (sun.management.MemoryImpl)
      Information on the management interface of the MBean
    ----------------------------------------------------
    Attributes:
      * ObjectPendingFinalizationCount [R-] int                                      ObjectPendingFinalizationCount
      * HeapMemoryUsage                [R-] javax.management.openmbean.CompositeData HeapMemoryUsage
      * NonHeapMemoryUsage             [R-] javax.management.openmbean.CompositeData NonHeapMemoryUsage
      * Verbose                        [RW] boolean                                  Verbose
      * ObjectName                     [R-] javax.management.ObjectName              ObjectName
    Operations:
      * gc():void gc
    Notifications:
      * javax.management.Notification Memory Notification
        + java.management.memory.threshold.exceeded
        + java.management.memory.collection.threshold.exceeded

### mbeans:get-attributes
Display attribute's value of a given MBean (`ObjectName` provided as argument).

    guillaume@peergreen-platform$ mbeans:get-attributes java.lang:type=Memory
      ObjectPendingFinalizationCount 0
      HeapMemoryUsage Composite{committed=149356544, init=134069376, max=1907032064, used=36066056}
      NonHeapMemoryUsage Composite{committed=25493504, init=24313856, max=136314880, used=25323952}
      Verbose false
      ObjectName java.lang:type=Memory

Notice that `CompositeDate` and simple value types are supported (no support yet for `TabularData` for example).


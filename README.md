# kafka-consumer
thread per consumer model demo. 
<br/>
<br/>

kafka cli
===
demo command

topic
---
create topic

    /usr/bin/kafka-topics --bootstrap-server localhost:9092 --create --partitions 4 --topic test_topic
   >__if you change the topic name or number of partitions, you must also change the yml property__  
   
<br/>

delete topic  


    /usr/bin/kafka-topics --bootstrap-server localhost:9092 --delete --topic test_topic

producer
---
    /usr/bin/kafka-console-producer --bootstrap-server localhost:9092 --topic test_topic

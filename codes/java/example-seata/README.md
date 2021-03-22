

├── example-seata-api
8800
dubbo protocol port=20880
rest protocol port=8880
dubbo.application.qos.port=22220

├── example-seata-account
8801
dubbo protocol port=20881
rest protocol port=8881
dubbo.application.qos.port=22221

├── example-seata-order
8802
dubbo protocol port=20882
rest protocol port=8882
dubbo.application.qos.port=22222

├── example-seata-storage
8803
dubbo protocol port=20883
rest protocol port=8883
dubbo.application.qos.port=22223

account -> storage -> order -> api


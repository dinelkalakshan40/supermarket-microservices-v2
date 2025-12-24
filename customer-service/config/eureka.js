const {Eureka} =require('eureka-js-client');

const eurekaClient=new Eureka({
    instance: {
        app: 'CUSTOMER-SERVICE',
        instanceId: 'customer-service-3000',
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        statusPageUrl: 'http://localhost:3000/info',
        healthCheckUrl: 'http://localhost:3000/health',
        port: {'$': 3000, '@enabled': true},
        vipAddress: 'CUSTOMER-SERVICE',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn'
        }
    },
    eureka: {
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/'
    }
});

module.exports=eurekaClient;
package com.example.demo.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;



@Component
@Slf4j
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MockQueue mockQueue;

//    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent){
        new Thread(()->{
            while (true) {
                if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {

                    String orderNO=mockQueue.getCompleteOrder();
                    log.info("返回订单处理结果：" + orderNO);
                    deferredResultHolder.getMap().get(orderNO).setResult("place order success");
                    mockQueue.setCompleteOrder(null);
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}

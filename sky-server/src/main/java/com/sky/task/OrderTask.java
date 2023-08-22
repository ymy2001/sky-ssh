package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /*
    * 处理超时订单*/
    @Scheduled(cron = "0 * * * * ? ")//每分钟触发一次
    public void processTimeoutOrder(){
        log.info("定时处理任务{}", LocalDateTime.now());
        List<Orders> orderList = orderMapper.getByStatusAndByOrderTime(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        if (orderList!=null&&orderList.size()>0){
            for (Orders orders : orderList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }

    }
    @Scheduled(cron = "0 0 1 * * ? ")//每天凌晨一点处理
    public void processDliver(){
        log.info("定时处理订单{}",LocalDateTime.now());
        List<Orders> orderList = orderMapper.getByStatusAndByOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusHours(-1));
        if (orderList!=null&&orderList.size()>0){
            for (Orders orders : orderList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
    @Component
    public class WebSocketTask {
        @Autowired
        private WebSocketServer webSocketServer;

        /**
         * 通过WebSocket每隔5秒向客户端发送消息
         */
        @Scheduled(cron = "0/5 * * * * ?")
        public void sendMessageToClient() {
            webSocketServer.sendToAllClient("这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
        }
    }
}

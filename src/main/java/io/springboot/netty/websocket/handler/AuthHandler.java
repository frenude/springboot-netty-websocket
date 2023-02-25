package io.springboot.netty.websocket.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * FullHttpRequest
 */

@ChannelHandler.Sharable
@Component
public class AuthHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandler.class);
//
//    @Value("${netty.websocket.authKey}")
//    private String authKey;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof DefaultHttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            HttpHeaders headers = request.headers();
            if (headers.size() < 1) {
                ctx.channel().close();
                return;
            }
            long num = headers.entries().stream().filter(s -> s.getKey().equals("token")).count();

            if (num>0){
                String token = headers.entries().stream().filter(s -> s.getKey().equals("token")).findFirst().get().getValue();
                LOGGER.info("token：{}",token);
                // 判断token值
                if (token.equals("11111")){
                    ctx.pipeline().remove(this);
                    // 对事件进行传播，知道完成WebSocket连接。
                    ctx.fireChannelRead(msg);
                }else {
                    ctx.channel().close();
                }
            }else {
                ctx.channel().close();
            }
        } else {
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}

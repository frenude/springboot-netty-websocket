package io.springboot.netty.websocket.handler;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class RouteHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            String uri = ((FullHttpRequest) msg).getUri();
            ctx.pipeline().remove(this);

            if ("/ws".equalsIgnoreCase(uri)) {
                ctx.pipeline().addAfter("HttpObjectAggregator", WebSocketServerProtocolHandler.class.getName(), new WebSocketServerProtocolHandler(uri));
                ctx.pipeline().addAfter(WebSocketServerProtocolHandler.class.getName(), WebsocketMessageHandler.class.getName(), new WebsocketMessageHandler());
            } else if ("/ws1".equalsIgnoreCase(uri)) {
                ctx.pipeline().addAfter("HttpObjectAggregator", WebSocketServerProtocolHandler.class.getName(), new WebSocketServerProtocolHandler(uri));
                ctx.pipeline().addAfter(WebSocketServerProtocolHandler.class.getName(), WebsocketMessageHandler_1.class.getName(), new WebsocketMessageHandler_1());
           }
           else {
                ctx.channel().close();
                return;
            }

            ctx.pipeline().fireChannelRead(msg);
        }
    }
}
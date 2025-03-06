package com.rpc.example.codec;

import com.rpc.example.constant.ReqType;
import com.rpc.example.constant.RpcConstant;
import com.rpc.example.core.Header;
import com.rpc.example.core.RpcProtocol;
import com.rpc.example.core.RpcRequest;
import com.rpc.example.core.RpcResponse;
import com.rpc.example.serial.ISerializer;
import com.rpc.example.serial.SerializerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author 周贵龙
 * @version 1.0.0
 * @ClassName RpcDecoder.java
 * @Description TODO
 * @createTime 2022年04月23日 20:46:00
 */
public class RpcDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("========begin RpcDecoder==========");

        if (in.readableBytes() < RpcConstant.HEAD_TOTAL_LEN) {
            return;
        }
        in.markReaderIndex(); //标记读取开始索引
        short maci = in.readShort(); //读取2个字节的magic
        if (maci != RpcConstant.MAGIC) {
            throw new IllegalArgumentException("Illegal request parameter 'magic'," + maci);
        }

        byte serialType = in.readByte(); //读取一个字节的序列化类型
        byte reqType = in.readByte(); //读取一个字节的消息类型
        long requestId = in.readLong(); //读取请求id
        int dataLength = in.readInt(); //读取数据报文长度

        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex(); // 还原数据
            return;
        }
        //读取消息体的内容
        byte[] content = new byte[dataLength];
        in.readBytes(content);

        Header header = new Header(maci, serialType, reqType, requestId, dataLength);
        ISerializer serializer = SerializerManager.getSerializer(serialType);//获得序列化类型
        ReqType rt = ReqType.findByCode(reqType);//获得请求类型
        switch (rt) {
            case REQUEST:
                // 将内容反序列化
                RpcRequest request = serializer.deserializer(content, RpcRequest.class);
                // 最好的返回体
                RpcProtocol<RpcRequest> reqProtocol = new RpcProtocol<>();
                reqProtocol.setHeader(header);
                reqProtocol.setContent(request);
                // 传递
                out.add(reqProtocol);
                break;
            case RESPONSE:
                RpcResponse response = serializer.deserializer(content, RpcResponse.class);
                RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
                resProtocol.setHeader(header);
                resProtocol.setContent(response);
                out.add(resProtocol);
                break;
            case HEARTBEAT:
                //TODO
                break;
            default:
                break;
        }

    }
}

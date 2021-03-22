/**
 * <pre>
 * 
 * 既有codec实现类:
 * 
 * ├── AlternativeByteToIntegerDecoder
 * ├── ByteToIntegerDecoder
 * ├── CombinedByteCharCodec
 * ├── IntegerToStringDecoder
 * ├── IntegerToStringEncoder
 * ├── SafeByteToMessageDecoder
 * ├── ShortToByteEncoder
 * ├── WebSocketFrameCodec
 * 
 * 
 * └── provided                                         -- 既有实现类
 *     ├── file                                         - 文件传输/读写
 *     │   ├── ChunkedWriteHandlerInitializer
 *     │   ├── FileTransferHandler
 *     ├── protocol                                     - 协议
 *     │   ├── CmdWithDelimiterHandlerInitializer
 *     │   ├── LengthBasedInitializer
 *     │   ├── LineBasedHandlerInitializer
 *     │   ├── SslChannelInitializer
 *     │   ├── http
 *     │   │   ├── HttpCompressionInitializer
 *     │   │   ├── HttpMessageAggregatorInitializer
 *     │   │   ├── HttpPipelineInitializer
 *     │   │   ├── HttpsInitializer
 *     │   │   ├── WebSocketInitializer
 *     ├── serialization                              - 序列化
 *     │   ├── JBossMarshallingInitializer
 *     │   ├── JDKSerializationInitializer
 *     │   ├── ProtoBufInitializer
 *     └── timeout                                    - 超时
 *         ├── IdleStateHandlerInitializer
 * </pre>
 * 
 * @author zhoujiagen
 */
package com.spike.codesnippet.netty.example.codec;
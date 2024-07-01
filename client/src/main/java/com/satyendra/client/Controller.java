package com.satyendra.client;

import java.util.HashMap;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import net.devh.boot.grpc.client.inject.GrpcClient;

import io.grpc.stub.*;

import com.satyendra.client.protomodels.*;

import java.util.Iterator;

@RestController
public class Controller {

    private final RestClient restClient;
    private final getSquareGrpc.getSquareBlockingStub grpcClient;
    // private final getSquareGrpc.getSquareStub grpcStreamClient;

    public Controller(
        RestClient.Builder builder,
		@GrpcClient("getSquare") getSquareGrpc.getSquareBlockingStub grpcClient
        // @GrpcClient("getSquare") getSquareGrpc.getSquareStub grpcStreamClient
        ) {
			this.restClient =  builder.requestFactory(new JdkClientHttpRequestFactory()).build();
			this.grpcClient = grpcClient;
            // this.grpcStreamClient = grpcStreamClient;
		}
    
    @GetMapping(path="/rest/{num}")
    public HashMap<Object, Object> getRest(@PathVariable int num){
        var result = new HashMap<>();

        for(int i=1;i<=num;i++){
            var response = restClient.get()
            .uri("http://localhost:8082/".concat(String.valueOf(i)))
            .retrieve()
            .body(new ParameterizedTypeReference<>() {});
            result.put(i,response);
        }

        return result;

    }

    @GetMapping(path = "/grpc/{num}")
    public HashMap<Object,Object> getGrpc(@PathVariable int num){
        var result = new HashMap<>();

        for(int i=1;i<=num;i++){
        var response = grpcClient.get(requestMsg.newBuilder().setNum(i).build());
        result.put(i,response.getResult());
        }

        return result;
    }

    @GetMapping(path = "/grpc/stream/{num}")
    public HashMap<Object,Object> getGrpcStream(@PathVariable int num){
        var result = new HashMap<>();
        var request = requestMsg.newBuilder().setNum(num).build();
        Iterator<responseMsg> data;      
        try{
            data = grpcClient.getStream(request);

            for(int i = 1;data.hasNext();i++){
                var x = data.next();
                result.put(i,x.getResult());
            }

        }catch(Exception e){
            System.out.println("ERROR OCCURED");
            throw e;
        }
        return result;
    }
}

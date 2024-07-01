package com.satyendra.client;

import java.util.HashMap;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import net.devh.boot.grpc.client.inject.GrpcClient;

import com.satyendra.client.protomodels.*;

@RestController
public class Controller {

    private final RestClient restClient;
    private final getSquareGrpc.getSquareBlockingStub grpcClient;

    public Controller(RestClient.Builder builder,
								   @GrpcClient("getSquare") getSquareGrpc.getSquareBlockingStub grpcClient) {
			this.restClient =  builder.requestFactory(new JdkClientHttpRequestFactory()).build();
			this.grpcClient = grpcClient;

		}
    
    @GetMapping(path="/rest/{num}")
    public HashMap<Object, Object> getRest(@PathVariable int num){
        // System.out.println("rest");
        // System.out.println(num);

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
        // System.out.println("grpc");
        // System.out.println(num);

        var result = new HashMap<>();

        for(int i=1;i<=num;i++){
        var response = grpcClient.get(requestMsg.newBuilder().setNum(i).build());
        result.put(i,response.getResult());
        }

        return result;
    }
}

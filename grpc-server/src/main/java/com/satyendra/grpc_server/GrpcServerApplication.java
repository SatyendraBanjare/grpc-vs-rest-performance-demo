package com.satyendra.grpc_server;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.stub.*;
import net.devh.boot.grpc.server.service.GrpcService;


import com.satyendra.grpc_server.protomodels.*;

@SpringBootApplication
public class GrpcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcServerApplication.class, args);
	}

	@GrpcService
	public static class getsquareImpl extends getSquareGrpc.getSquareImplBase {

		@Override
		public void get(requestMsg request, StreamObserver<responseMsg> responseObserver) {
			var num = (int)request.getNum();
			responseObserver.onNext(responseMsg.newBuilder()
					.setResult(num*num)
					.build());

			responseObserver.onCompleted();
		}
	}

}

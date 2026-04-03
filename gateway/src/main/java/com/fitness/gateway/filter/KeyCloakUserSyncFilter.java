package com.fitness.gateway.filter;

import com.fitness.gateway.dto.RegisterRequest;
import com.fitness.gateway.service.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeyCloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        RegisterRequest registerRequest = getUserDetails(token);

        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");

        if(userId == null){
            userId = registerRequest.getKeyCloakId();
        }

        if(userId != null && token != null){
            String finaluserId = userId;
            return userService.validateUser(userId)
                    .flatMap(exist ->{
                        if(!exist){
                            //register user

                            if(registerRequest!=null){
                                return userService.registerUser(registerRequest)
                                        .then(Mono.empty());
                            }else {
                                return Mono.empty();
                            }
                        }else {
                            log.info("user already exist,skipping sync");
                            return Mono.empty(); // "Mono.empty()" simply means im finished in step, move to next
                        }
                    })
                    .then(Mono.defer( ()-> {   // " Mono.defer() "  means run this part only after finishing above part
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID",finaluserId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return chain.filter(exchange);

    }

    private RegisterRequest getUserDetails(String token) {
        try{
            String tokenWithoutBearer = token.replace("Bearer ","").trim();
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            RegisterRequest registerRequest = new RegisterRequest();

            registerRequest.setEmail(claimsSet.getStringClaim("email"));
            registerRequest.setKeyCloakId(claimsSet.getStringClaim("sub"));
            registerRequest.setPassword("dummy@123123");
            registerRequest.setFirstName(claimsSet.getStringClaim("given_name"));
            registerRequest.setLastName(claimsSet.getStringClaim("family_name"));

            return registerRequest;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}


//### 🔹 API Gateway Filter Flow (Keycloak + WebFlux) — Complete Understanding
//
//#### 1. What this filter is
//
//* This class implements `WebFilter`, which means:
//
//        * It is one step in Spring WebFlux request processing pipeline.
//        * Every incoming request passes through this filter.
//
//---
//
//        #### 2. What is `ServerWebExchange` (exchange)?
//
//        * It represents the **entire HTTP interaction**:
//
//        * Request (headers, body, URL)
//  * Response
//* In this filter, we mainly use it to:
//
//        * Read headers (Authorization, X-User-ID)
//  * Modify request (add new header)
//
//---
//
//        #### 3. What is `WebFilterChain` (chain)?
//
//        * It represents the **next step in processing**.
//        * The request flows like:
//Request → Filter1 → Filter2 → This Filter → Next Filter → Controller
//* Calling:
//        `chain.filter(exchange)`
//means:
//        → “I’m done, pass request to next filter/service”
//
//        ---
//
//        #### 4. High-level flow of this filter
//
//1. Request comes to Gateway
//2. Extract JWT token from `Authorization` header
//3. Parse JWT → get user details:
//
//        * Keycloak ID (`sub`)
//   * email, name, etc.
//4. Try to get `X-User-ID` from header
//
//   * If not present → use Keycloak ID
//5. Validate user in User Service:
//
//        * If user does not exist → register user
//   * If exists → skip
//6. Add `X-User-ID` header to request
//7. Forward request to next service using `chain.filter(...)`
//
//        ---
//
//        #### 5. What is `Mono<Void>`?
//
//        * It does NOT return data
//* It represents:
//        → “A process that will complete later”
//        * In this filter:
//
//        * It means request processing will complete after forwarding
//
//---
//
//        #### 6. What is `Mono.empty()`?
//
//        * It means:
//        → “Do nothing, just continue”
//        * Used when:
//
//        * User already exists
//  * No further action needed
//
//---
//
//        #### 7. What is `flatMap()` doing?
//
//        * Used to handle async operation:
//
//        * `validateUser(userId)`
//        * Based on result:
//
//        * If user not found → register
//  * Else → skip
//
//---
//
//        #### 8. What is `.then(...)` doing?
//
//        * It ensures order:
//        → “First finish validation/registration, then forward request”
//
//        ---
//
//        #### 9. What is `Mono.defer()`?
//
//        * It ensures:
//        → “Execute forwarding only after previous steps complete”
//        * Prevents early execution
//
//---
//
//        #### 10. What is request mutation?
//
//        ```java
//exchange.getRequest().mutate().header("X-User-ID", userId).build();
//```
//
//        * Creates a **new modified request**
//        * Adds or replaces header:
//        `X-User-ID`
//        * Important:
//
//        * Original request is immutable (cannot be changed directly)
//
//---
//
//        #### 11. Why set `X-User-ID` header?
//
//        * Microservices do NOT know your local variables
//* They only read request headers
//* So we attach user identity to request
//* Currently:
//
//        * You are using Keycloak ID as `X-User-ID`
//
//        ---
//
//        #### 12. Current design decision
//
//* Gateway:
//        ✔ Validates JWT
//  ✔ Extracts Keycloak ID
//  ✔ Syncs user with DB
//  ✔ Adds header
//
//* Microservices:
//        ✔ Trust Gateway
//  ✔ Use `X-User-ID`
//
//        ---
//
//        #### 13. Important limitation (current stage)
//
//* Microservices are still publicly accessible (dev mode)
//* So:
//        → Direct calls can bypass Gateway (not secure yet)
//* This will be solved later using:
//
//        * Private network / firewall
//
//---
//
//        #### 14. Final request flow
//
//Client → Gateway
//→ This Filter executes
//→ User validated/registered
//→ Header added
//→ Request forwarded
//→ Microservice processes request
//→ Response returned
//
//---
//
//        #### 15. One-line summary
//
//“This filter ensures every request has a valid user (synced with DB) and forwards it with user identity attached.”
//
//        ---
//

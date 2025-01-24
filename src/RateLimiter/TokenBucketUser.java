package RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketUser {
    private final int maxTokens;  // Max tokens the bucket can hold
    private final int refillRate; // Tokens added per seconds

    private final ConcurrentHashMap<String, TokenDetail> userMap;

    private final ScheduledExecutorService scheduler;

    private static class TokenDetail {
        private int currentToken;
        public TokenDetail(int currentToken){
            this.currentToken = currentToken;
        }
        public int getCurrentToken(){
            return currentToken;
        }
        public void setCurrentToken(int token){
            this.currentToken = token;
        }
    }

    public TokenBucketUser(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.userMap = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::refillAllUsersToken,0, 1, TimeUnit.SECONDS);
    }

    public boolean isAllowed(String userId){
        // Naush -> TD(3), t1 = 2  and t2 = 2
        TokenDetail remainingToken = userMap.computeIfAbsent(userId, id -> new TokenDetail(maxTokens));
        synchronized (remainingToken){
            if(remainingToken.getCurrentToken() > 0){
                // t1= 2, t2= 2
                remainingToken.setCurrentToken(remainingToken.getCurrentToken()-1);
                return true;
            }
        }
        return false;
    }

    // m = 10, r = 5, c=10
    public synchronized void refillToken(String userId){
        TokenDetail remainingToken = userMap.computeIfAbsent(userId, id -> new TokenDetail(maxTokens));
        synchronized (remainingToken){
            if(remainingToken.getCurrentToken() < maxTokens){
                remainingToken.setCurrentToken(Math.min(maxTokens, remainingToken.getCurrentToken() + refillRate));
                System.out.println("Refilled for user - " + userId);
            }
        }

    }

    private void refillAllUsersToken(){
        for(String userId: userMap.keySet()){
            refillToken(userId);
        }
    }

    public void stop(){
        scheduler.shutdown();
    }


    public static void main(String[] args) {
        TokenBucket tokenBucket = new TokenBucket(5, 5);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3); // creating 3 threads
        executorService.scheduleAtFixedRate(tokenBucket::isAllowed,0,200, TimeUnit.MILLISECONDS);   // per 200 milliseconds
        executorService.scheduleAtFixedRate(tokenBucket::isAllowed,0,300, TimeUnit.MILLISECONDS);    // per 300 milliseconds

        try{
            Thread.sleep(5000);
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }

        //shutdown
        executorService.shutdown();
        tokenBucket.stop();
    }

}

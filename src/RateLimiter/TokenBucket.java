package RateLimiter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenBucket {
    private final int maxTokens;  // Max tokens the bucket can hold
    private final int refillRate; // Tokens added per seconds

    private int currentTokens; // Current number of token
    private final ScheduledExecutorService scheduler;

    public TokenBucket(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.currentTokens = maxTokens;
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::refillToken,0, 1, TimeUnit.SECONDS);
    }

    public synchronized boolean isAllowed(){
        if(currentTokens > 0){
            currentTokens--;
            System.out.println("Request allowed");
            return true;
        } else {
            System.out.println("Request denied, No token available");
            return false;
        }
    }

    // m = 10, r = 5, c=10
    public synchronized void refillToken(){
        if(currentTokens < maxTokens) {
            currentTokens = Math.min(maxTokens, currentTokens + refillRate);
            System.out.println("Tokens refilled");
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

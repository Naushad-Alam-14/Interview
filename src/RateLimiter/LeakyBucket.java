package RateLimiter;

import java.util.concurrent.locks.ReentrantLock;

public class LeakyBucket {
    private final int capacity; // max capacity of the bucket
    private final int leakRate;  // Request processed per second

    private int currentLevel = 0; // current number of requests in the bucket
    private long lastLeakTime;
    private final ReentrantLock lock;

    public LeakyBucket(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        lastLeakTime = System.currentTimeMillis();
        this.lock = new ReentrantLock();
    }

    // 10:00 -> 6 request   => 10:00:10
    // 10:00 -> 6 request => 10:00:10 -> 10:00:20 -> 10:00:30 -> 10:00:40 -> 10:00:50 -> 10:01 -> 10:01:10 -> 10:01:20
    public boolean allowRequest(int i){
        lock.lock();
        try{
            // How much time has passed since the last leak
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastLeakTime;
            System.out.println(i + " elapsed time - " + elapsedTime);
            // Calculate how many requests can be leaked during this time
            int leakedRequests = (int) (elapsedTime * leakRate/1000);
            System.out.println(i + "leaked request - " + leakedRequests);

            if(leakedRequests > 0){
                currentLevel = Math.max(0, currentLevel - leakedRequests);
                System.out.println(i + " current level update - " + currentLevel);
                lastLeakTime = System.currentTimeMillis();
            }

            // check if there is space in the bucket for the new request
            if(currentLevel < capacity){
                System.out.println(i + " current request update - " + currentLevel);
                currentLevel++;
                return true;
            }else {
                return false;
            }
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LeakyBucket leakyBucket = new LeakyBucket(5,5);
        for (int i=1; i<=20; i++){
            if(leakyBucket.allowRequest(i)){
                System.out.println("Request " + i + " allowed");
            }else {
                System.out.println("Request " + i + " denied");
            }
            Thread.sleep(100); // per second send 2 request -> per second 5 request process
        }
    }
}

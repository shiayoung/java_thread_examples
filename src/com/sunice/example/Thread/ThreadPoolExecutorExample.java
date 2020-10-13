package com.sunice.example.Thread;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorExample {
    public static void main(String[] args) throws InterruptedException, IOException {
        int corePoolSize = 2;
        int maximumPoolSize = 4;
        long keepAliveTime = 10;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);

        ThreadFactory threadFactory = new NamedThreadFactory();
        RejectedExecutionHandler handler = new MyIngorePolicy();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workQueue, threadFactory, handler);

        executor.prestartAllCoreThreads();
        for(int i = 1; i <= 10; i++){
            MyTask task = new MyTask(String.valueOf(i));
            executor.execute(task);
        }

        System.in.read();
    }

    static class NamedThreadFactory implements ThreadFactory {
        final AtomicInteger mThread = new AtomicInteger(1) ;
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThread.getAndIncrement());
            System.out.println(t.getName() + " thread has been created.");
            return t;
        }
    }

    static class MyIngorePolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            doLog(r, executor);
        }

        private void doLog(Runnable r, ThreadPoolExecutor e) {
            System.err.println(r.toString() + " rejected.");
        }
    }

    static class MyTask implements Runnable {
        private String name;
        public MyTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                System.out.println(this.toString());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return "MyTask [name=" + this.getName() + "]";
        }
    }
}
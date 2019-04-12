package com.isoftstone.common.networm;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class test {

	public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            priorityQueue.offer(rand.nextInt(i + 10));
        }
       // QueueDemo.printQ(priorityQueue);
        List<Integer>ints= Arrays.asList(25,22,20,18,14,9,3,1,1,2,3,9,14,18,21,23,25);
        priorityQueue=new PriorityQueue<Integer>(ints);
       // QueueDemo.printQ(priorityQueue);
    }

}

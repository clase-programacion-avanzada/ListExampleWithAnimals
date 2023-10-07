package org.study.services;

import java.util.ArrayDeque;
import java.util.Deque;
import org.study.model.Animal;

public class AttentionQueueService {

    // Reference: https://www.baeldung.com/java-array-deque
    // Reference: https://docs.oracle.com/javase/7/docs/api/java/util/Deque.html
    // Reference: https://www.geeksforgeeks.org/deque-interface-java-example/
    private Deque<Animal> animalsToAttend;

    public AttentionQueueService() {
        this.animalsToAttend = new ArrayDeque<>();
    }

    public boolean addAnimalToAttend(Animal animal) {
        //Inserts the specified element into the queue represented by this deque
        return animalsToAttend.add(animal);
    }

    public Animal attendAnimal() {
        //Retrieves and removes the head of the queue, returns null if the queue is empty
        return animalsToAttend.poll();
    }

    public Animal checkNextAnimalToAttend() {
        //Retrieves but does not remove the head of the queue, returns null if the queue is empty
        return animalsToAttend.peek();
    }


    public int getNumberOfAnimalsToAttend() {
        return animalsToAttend.size();
    }

    public boolean isThereAnyAnimalToAttend() {
        return !animalsToAttend.isEmpty();
    }

    public boolean isAnimalInQueue(Animal animal) {
        return animalsToAttend.contains(animal);
    }





}

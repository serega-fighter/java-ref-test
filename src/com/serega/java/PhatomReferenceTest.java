package com.serega.java;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

public class PhatomReferenceTest {

    public static class TestResource {

        private final int id;

        public TestResource(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "TestResource{" +
                    "id=" + id +
                    '}';
        }
    }

    public static class Ref extends PhantomReference<TestResource> {

        /**
         * Creates a new phantom reference that refers to the given object and
         * is registered with the given queue.
         *
         * <p> It is possible to create a phantom reference with a {@code null}
         * queue, but such a reference is completely useless: Its {@code get}
         * method will always return {@code null} and, since it does not have a queue,
         * it will never be enqueued.
         *
         * @param referent the object the new phantom reference will refer to
         * @param q        the queue with which the reference is to be registered,
         *                 or {@code null} if registration is not required
         */
        public Ref(TestResource referent, ReferenceQueue<? super TestResource> q) {
            super(referent, q);
        }


    }

    public static void main(String[] args) {
        ReferenceQueue<TestResource> referenceQueue = new ReferenceQueue<>();

        Thread thread = new Thread(() -> {

            while (!Thread.currentThread().isInterrupted()) {
                Reference<? extends TestResource> polled = referenceQueue.poll();
                if (polled == null) {
                    continue;
                }

                System.out.println("At ref q: " + polled);
            }
        });
        thread.start();

        List<TestResource> tsArray = new ArrayList<>();
        TestResource ts = new TestResource(5);
        tsArray.add(ts);

        Ref ref = new Ref(ts, referenceQueue);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tsArray.clear();
        ts = null;

        System.gc();
        System.gc();
        System.gc();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

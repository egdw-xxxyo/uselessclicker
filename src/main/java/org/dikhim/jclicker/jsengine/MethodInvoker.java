package org.dikhim.jclicker.jsengine;

import org.dikhim.jclicker.util.Out;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodInvoker {
    Invocable invocable;
    private Map<String, Lock> invocableMethods = new HashMap<>();
    private List<Thread> threads = new ArrayList<>();

    MethodInvoker(ScriptEngine scriptEngine) {
        invocable = (Invocable) scriptEngine;
    }

    void registerMethod(String name, int numberOfThreads) {
        if (invocableMethods.containsKey(name)) {
            Lock lock = invocableMethods.get(name);
            lock.setMaxNumber(numberOfThreads);
        } else {
            invocableMethods.put(name, new Lock(numberOfThreads));
        }
    }

    private void registerMethod(String name) {
        invocableMethods.put(name, new Lock(1));
    }

    void invokeMethod(String name, Object... args) {
        if (!invocableMethods.containsKey(name)) {
            registerMethod(name);
        }

        Lock lock = invocableMethods.get(name);
        if (lock.lock()) {
            Thread thread = new Thread(() -> {
                try {
                    invocable.invokeFunction(name, args);
                } catch (ScriptException | NoSuchMethodException e) {
                    Out.println(e.getMessage());
                } finally {
                    lock.unlock();
                }
            });
            threads.add(thread);
            thread.start();
        }
    }

    public void stop() {
        threads.removeIf(t -> {
            try {
                t.stop();
            } catch (ThreadDeath ignored) {
            }
            return true;
        });
    }


    private class Lock {
        private volatile int currentNumber;
        private volatile int maxNumber;

        Lock(int maxNumber) {
            if (maxNumber <= 0) {
                this.maxNumber = 1;
            } else {
                this.maxNumber = maxNumber;
            }
            currentNumber = 0;
        }

        synchronized boolean isLocked() {
            return currentNumber >= maxNumber;
        }

        synchronized boolean lock() {
            if (isLocked()) return false;
            currentNumber++;
            return true;
        }

        synchronized void unlock() {
            currentNumber--;
        }

        synchronized void setMaxNumber(int maxNumber) {
            if (maxNumber <= 0) {
                this.maxNumber = 1;
            } else {
                this.maxNumber = maxNumber;
            }
        }
    }
}

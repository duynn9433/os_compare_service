package com.viettel.vtnet.os_compare_service.service.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class BufferedDbWriter<T> {
    private final CrudRepository<T, ?> repository;
    private final int batchSize;
    private final long timeoutMillis;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ReentrantLock lock = new ReentrantLock();

    private List<T> buffer = new ArrayList<>();
    private long lastFlushTime;

    public BufferedDbWriter(CrudRepository<T, ?> repository) {
        this(repository, 1000, 2000);
    }

    public BufferedDbWriter(CrudRepository<T, ?> repository, int batchSize, long timeoutMillis) {
        this.repository = repository;
        this.batchSize = batchSize;
        this.timeoutMillis = timeoutMillis;
        this.lastFlushTime = System.currentTimeMillis();
        startTimeoutWatcher();
    }

    public void cleanDatabase() {
        try {
            repository.deleteAll();
            log.info("Database cleaned for {}", repository.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Error while cleaning database", e);
        }
    }

    public void add(T entity) {
        lock.lock();
        try {
            buffer.add(entity);
            if (buffer.size() >= batchSize) {
                flush();
            }
        } finally {
            lock.unlock();
        }
    }

    public void addAll(List<T> entities) {
        lock.lock();
        try {
            buffer.addAll(entities);
            if (buffer.size() >= batchSize) {
                flush();
            }
        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        lock.lock();
        try {
            if (!buffer.isEmpty()) {
                int fromIndex = 0;
                while (fromIndex < buffer.size()) {
                    int toIndex = Math.min(fromIndex + batchSize, buffer.size());
                    List<T> batch = new ArrayList<>(buffer.subList(fromIndex, toIndex));
                    repository.saveAll(batch);
                    log.info("Flushed {} records to DB", batch.size());
                    fromIndex = toIndex;
                }
                buffer.clear();
                lastFlushTime = System.currentTimeMillis();
            }
        } catch (Exception e) {
            log.error("Error during flush", e);
        } finally {
            lock.unlock();
        }
    }

    private void startTimeoutWatcher() {
        scheduler.scheduleAtFixedRate(() -> {
            lock.lock();
            try {
                if (!buffer.isEmpty() && (System.currentTimeMillis() - lastFlushTime >= timeoutMillis)) {
                    flush();
                }
            } finally {
                lock.unlock();
            }
        }, timeoutMillis, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
        flush(); // final flush
    }

    public long countDatabase() {
        try {
            return repository.count();
        } catch (Exception e) {
            log.error("Error counting database", e);
            return -1;
        }
    }
}
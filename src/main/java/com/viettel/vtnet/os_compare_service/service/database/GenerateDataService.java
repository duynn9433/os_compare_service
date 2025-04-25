package com.viettel.vtnet.os_compare_service.service.database;

import com.viettel.vtnet.os_compare_service.repo.postgres.Entity1Repo;
import com.viettel.vtnet.os_compare_service.service.AppConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@Log4j2
public class GenerateDataService {
    private final AppConfig appConfig;
    private final Entity1Repo entity1Repo;
    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    public GenerateDataService(AppConfig appConfig, Entity1Repo entity1Repo) {
        this.appConfig = appConfig;
        this.entity1Repo = entity1Repo;
    }

    /**
     * Generates data in parallel for database repositories.
     * Works with both JPA and MongoDB repositories.
     *
     * @param entityClass The entity class to generate
     * @param repo The repository to save entities (JPA or Mongo)
     * @param deleteRepo The repository containing delete methods
     * @param expectedCount The expected count of records
     * @param maxThreads Maximum number of concurrent threads
     * @param <T> Entity type
     */
    @Transactional
    public <T> void generateWhenStartParallel(Class<T> entityClass,
                                              CrudRepository<T, ?> repo,
                                              Object deleteRepo,
                                              long expectedCount,
                                              int maxThreads) {
        long currentCount = repo.count();
        long needed = expectedCount - currentCount;
        if (needed == 0) {
            log.info("No need to insert for " + entityClass.getSimpleName());
            return;
        }
        if (needed < 0) {
            // Try to find and invoke the appropriate delete method
            try {
                Method deleteMethod = findDeleteMethod(deleteRepo, expectedCount);
                if (deleteMethod != null) {
                    deleteMethod.setAccessible(true);
                    deleteMethod.invoke(deleteRepo, expectedCount);
                    log.info("Deleted records with id > " + expectedCount + " using " + deleteMethod.getName());
                } else {
                    log.warn("No suitable delete method found for " + entityClass.getSimpleName());
                }
            } catch (Exception e) {
                log.error("Error invoking delete method for " + entityClass.getSimpleName(), e);
            }

            return;
        }
        try {
            Method generateRandom = entityClass.getMethod("generateRandom");
            Method setId = entityClass.getMethod("setIdInit", long.class);

            Semaphore semaphore = new Semaphore(maxThreads); // limit concurrent threads

            List<T> currentBatch = new ArrayList<>(appConfig.getBatchSize() + 10);
            List<Future<T>> futures = new ArrayList<>();

            for (int i = 0; i < needed; i++) {
                final long id = currentCount + i + 1;

                semaphore.acquire(); // limit threads
                Future<T> future = executor.submit(() -> {
                    try {
                        @SuppressWarnings("unchecked")
                        T entity = (T) generateRandom.invoke(null);
                        setId.invoke(entity, id);
                        return entity;
                    } finally {
                        semaphore.release();
                    }
                });

                futures.add(future);

                if (futures.size() >= appConfig.getBatchSize()) {
                    currentBatch = waitAndCollect(futures);
                    repo.saveAll(currentBatch);
                    log.info("Inserted batch of " + currentBatch.size() + " records.");
                    futures.clear();
                }
            }

            // Write remaining entities
            if (!futures.isEmpty()) {
                currentBatch = waitAndCollect(futures);
                repo.saveAll(currentBatch);
                log.info("Inserted final batch of " + currentBatch.size() + " records.");
            }

            log.info("Inserted total of " + needed + " entries for " + entityClass.getSimpleName());

        } catch (Exception e) {
            log.error("Error in parallel generateWhenStart for " + entityClass.getSimpleName(), e);
        }
    }

    /**
     * Finds an appropriate delete method in the repository.
     * Tries different possible method names for both JPA and MongoDB repositories.
     *
     * @param repo The repository object
     * @param maxId The maximum ID value
     * @return The found method or null if none found
     */
    private Method findDeleteMethod(Object repo, long maxId) {
        // List of potential method names and their parameter types
        String[][] methodCandidates = {
                {"deleteByIdGreaterThan", "long"},
                {"deleteByMaxId", "long"},
                {"deleteByIdGreaterThan", "Long"},
                {"deleteByMaxId", "Long"},
                {"deleteByIdGreaterThanEqual", "long"},
                {"deleteByIdGreaterThanEqual", "Long"},
                {"deleteByIdAfter", "long"},
                {"deleteByIdAfter", "Long"},
                // MongoDB specific methods
                {"deleteAllByIdGreaterThan", "long"},
                {"deleteAllByIdGreaterThan", "Long"},
                {"deleteAllByIdGreaterThanEqual", "long"},
                {"deleteAllByIdGreaterThanEqual", "Long"}
        };

        // Try to find the method in the repository class and its interfaces
        Class<?> repoClass = repo.getClass();
        for (String[] candidate : methodCandidates) {
            String methodName = candidate[0];
            Class<?> paramType = "long".equals(candidate[1]) ? long.class : Long.class;

            // Try in the class itself
            try {
                Method method = repoClass.getMethod(methodName, paramType);
                log.info("Found delete method: " + methodName);
                return method;
            } catch (NoSuchMethodException e) {
                // Continue to check interfaces
            }

            // Check in interfaces
            for (Class<?> iface : repoClass.getInterfaces()) {
                try {
                    Method method = iface.getMethod(methodName, paramType);
                    log.info("Found delete method in interface: " + methodName);
                    return method;
                } catch (NoSuchMethodException e) {
                    // Continue to next interface
                }
            }
        }

        return null;
    }

    private <T> List<T> waitAndCollect(List<Future<T>> futures) throws ExecutionException, InterruptedException {
        List<T> result = new ArrayList<>(futures.size());
        for (Future<T> future : futures) {
            result.add(future.get());
        }
        return result;
    }
}
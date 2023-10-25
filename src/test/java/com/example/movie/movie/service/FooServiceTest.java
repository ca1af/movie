package com.example.movie.movie.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
@Transactional
@Rollback(value = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FooServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private Long id;

    private StopWatch stopWatch;

    @BeforeEach
    void init(){
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    @AfterEach
    void afterEach(){
        stopWatch.stop();
        System.out.println("============수행시간은 :: " + stopWatch.getTotalTimeMillis() + "ms");
    }

    @Test
    void addCountDirtyCheck() throws InterruptedException {
        id = testRestTemplate.postForObject("http://localhost:" + port + "/foo", null, Long.class);
        CountDownLatch latch = new CountDownLatch(1000);

        executeHttpRequests(() -> {
            testRestTemplate.postForEntity("http://localhost:" + port + "/foo/dirty?id=" + id, null, Void.class);
        }, 1000, latch);

        Thread.sleep(10);

        Long updatedCount = testRestTemplate.getForObject("http://localhost:" + port + "/foo/" + id, Long.class);
        assertThat(updatedCount).isEqualTo(1000);
    }

    @Test
    void addCountQuery() throws InterruptedException {
        id = testRestTemplate.postForObject("http://localhost:" + port + "/foo", null, Long.class);
        CountDownLatch latch = new CountDownLatch(1000);

        executeHttpRequests(() -> {
            testRestTemplate.postForEntity("http://localhost:" + port + "/foo/query?id=" + id, null, Void.class);
        }, 1000, latch);

        Thread.sleep(100);

        Long updatedCount = testRestTemplate.getForObject("http://localhost:" + port + "/foo/" + id, Long.class);
        assertThat(updatedCount).isEqualTo(1000);
    }

    @Test
    @DisplayName("배타락을 명시적으로, native query로.")
    void addCountQuery_FOR_UPDATE() {

        id = testRestTemplate.postForObject("http://localhost:" + port + "/foo", null, Long.class);
        CountDownLatch latch = new CountDownLatch(1000);

        executeHttpRequests(() -> {
            testRestTemplate.postForEntity("http://localhost:" + port + "/foo/exclusive?id=" + id, null, Void.class);
        }, 1000, latch);


        Long updatedCount = testRestTemplate.getForObject("http://localhost:" + port + "/foo/" + id, Long.class);
        assertThat(updatedCount).isEqualTo(1000);
    }

    private void executeHttpRequests(Runnable httpRequestTask, int requestSize, CountDownLatch latch) {
        ExecutorService executorService = Executors.newFixedThreadPool(requestSize);

        for (int i = 0; i < requestSize; i++) {
            executorService.submit(() -> {
                httpRequestTask.run();
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executorService.shutdown();
    }
}

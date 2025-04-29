import { Reader } from "k6/x/kafka";
import { Trend } from "k6/metrics";

export const options = {
    vus: 100, // 100 concurrent consumers
    // iterations: 10000,
    duration: '90s',
    summaryTrendStats: ['min', 'avg', 'med', 'p(90)', 'p(99)', 'max'], // Thêm p99 vào summary
};

const brokers = (__ENV.BROKERS || "localhost:9092").split(",");
const topic = "test";


const reader = new Reader({
    brokers: brokers,
    topic: topic,
});

const consumeTime = new Trend('kafka_consume_time');

export default function () {
    const start = Date.now();

    const messages = reader.consume({ limit: 1 });

    const duration = Date.now() - start;
    consumeTime.add(duration);

    // (Tùy chọn) Hiển thị message nếu debug:
    // for (const message of messages) {
    //     console.log(`Received: ${message.value}`);
    // }
}
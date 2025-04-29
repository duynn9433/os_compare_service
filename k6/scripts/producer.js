import { Writer } from "k6/x/kafka";
import { Trend, Counter } from "k6/metrics";

// Cấu hình K6
export const options = {
    vus: 100, // 100 concurrent threads
    // iterations: 10000, // Tổng số messages
    duration : '90s', // Thời gian chạy
    summaryTrendStats: ['min', 'avg', 'med', 'p(90)', 'p(99)', 'max'],
    summaryTimeUnit: 'ms',
};


const successCount = new Counter('kafka_success');
const failCount = new Counter('kafka_fail');

const brokers = (__ENV.BROKERS || "localhost:9092").split(",");
const topic = "test";

const writer = new Writer({
    brokers: brokers,
    topic: topic,
});

// Metric đo thời gian gửi message
const produceTime = new Trend('kafka_produce_time');

export default function () {
    const payload = "x".repeat(256); // 256 ký tự

    const start = Date.now();
    try {
        writer.produce({
            messages: [{ value: payload }],
        });
        successCount.add(1);
    } catch (err) {
        failCount.add(1);
        console.error(`❌ Lỗi gửi message: ${err}`);
    }
    const duration = Date.now() - start;

    produceTime.add(duration); // ghi lại thời gian gửi
}
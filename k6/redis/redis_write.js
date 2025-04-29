import redis from 'k6/experimental/redis';
import { check } from 'k6';

const client = new redis.Client('redis://localhost:6379');

// Tạo chuỗi ngẫu nhiên 256 byte
function randomValue(length = 256) {
    let result = '';
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

export let options = {
    vus: 100,
    duration: '90s',
    summaryTrendStats: ['min', 'avg', 'med', 'p(90)', 'p(99)', 'max'],
    summaryTimeUnit: 'ms',
};

export default async function () {
    const key = `key:${__VU}-${__ITER}`;
    const value = randomValue();
    const result = await client.set(key, value);
    console.log(`SET ${key} =>`, result); // In kết quả
    check(result, {
        'write succeeded': (r) => r === 'OK',
    });
}
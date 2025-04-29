import redis from 'k6/experimental/redis';
import { check, sleep } from 'k6';

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
    vus: 1,
    duration: '2s',
    summaryTrendStats: ['min', 'avg', 'med', 'p(90)', 'p(99)', 'max'],
    summaryTimeUnit: 'ms',
};

export default async function () {
    const key = `key:${__VU}-${__ITER}`; // đảm bảo key duy nhất
    const value = randomValue(); // 256-bit random string dễ debug

    const setResult = await client.set(key, value);
    const getResult = await client.get(key);

    check(setResult, {
        'set returned something': (r) => r !== null,
    });

    check(getResult, {
        'get returned value': (r) => r !== null,
        'value matches': (r) => r === value,
    });

    console.log(`SET ${key} => ${value}`);
    console.log(`GET ${key} => ${getResult}`);
}
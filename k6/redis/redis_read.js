// import redis from 'k6/x/redis';
import redis from 'k6/experimental/redis';
import { check } from 'k6';
const client = new redis.Client('redis://localhost:6379');

export let options = {
    vus: 100,
    duration: '90s',
    summaryTrendStats: ['min', 'avg', 'med', 'p(90)', 'p(99)', 'max'],
    summaryTimeUnit: 'ms',
};

export default function () {
    const key = `key:${Math.floor(Math.random() * 100000)}`;
    const value = client.get(key);

    check(value, {
        'value is not null': (v) => v !== null,
    });
}
const fs = require('fs');
const readline = require('readline');

const filename = process.argv[2] || 'sample.csv';
const lines = [];

const start = process.hrtime();

const rl = readline.createInterface({
    input: fs.createReadStream(filename),
    crlfDelay: Infinity
});

rl.on('line', line => {
    lines.push(line);
});

rl.on('close', () => {
    const [s, ns] = process.hrtime(start);
    const time = s + ns / 1e9;
    console.log(`✅ NodeJS read ${lines.length} rows in ${time.toFixed(4)} seconds`);
});
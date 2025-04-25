const fs = require('fs');
const lines = parseInt(process.argv[2]) || 10000;
const file = process.argv[3] || 'output.csv';

const start = Date.now();
const stream = fs.createWriteStream(file);
for (let i = 1; i <= lines; i++) {
    stream.write(`Line ${i},Some data here\n`);
}
stream.end(() => {
    const end = Date.now();
    console.log(`[NodeJS] Wrote ${lines} lines to ${file} in ${(end - start)/1000} seconds.`);
});
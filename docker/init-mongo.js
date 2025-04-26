db = db.getSiblingDB('os_compare'); // Switch to os_compare database
db.createCollection('init_collection'); // Create an initial collection
db.init_collection.insertOne({ initialized: true }); // Insert a dummy document
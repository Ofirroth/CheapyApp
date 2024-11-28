const { spawn } = require('child_process');
const userService = require('../services/user');
const Item = require('../models/item');

// Function to get recommended items for a given username
const getRecommended = async (username) => {
    try {
        // Fetch user details based on the username
        const user = await userService.getUserByName(username);
        const userId = user._id.toString();  // Convert ObjectId to string

        return new Promise((resolve, reject) => {
            // Spawn a Python process to run the recommendation algorithm with userId as a parameter
            const pythonProcess = spawn('python', ['./recommendationSystem/KNNAlgo.py', userId]);
            let output = '';
            // Capture stdout data from the Python process
            pythonProcess.stdout.on('data', (data) => {
                output += data.toString();
            });
            // Capture stderr errors from the Python process
            pythonProcess.stderr.on('data', (data) => {
                console.log('stderr:', data.toString());
            });
            // Handle Python process close event
            pythonProcess.on('close', async (code) => {
                //Error
                if (code !== 0) {
                    console.log(`Python process exited with code ${code}`);
                    return reject('Error generating recommendations');
                }
                try {
                    const resultJson = JSON.parse(output); // Parse the returned JSON string
                    if (resultJson.error) {
                        console.log(resultJson.error);
                        return reject(resultJson.error);
                    }
                    // Get recommended item names
                    const recommendedItemNames = resultJson.recommendedItems;
                    // find recommended items by name
                    const items = await Item.find({ name: { $in: recommendedItemNames } });
                    // Remove duplicates based on the name field
                    const uniqueItems = [...new Map(items.map(item => [item.name, item])).values()];
                    // Resolve the promise with the unique recommended items
                    resolve(uniqueItems);
                } catch (error) {
                    console.log('JSON parsing error:', error);
                    reject('Error processing recommendations');
                }
            });
        });
    } catch (err) {
        console.log('Error finding user:', err);
        throw new Error('Error finding user');
    }
};

module.exports = { getRecommended };

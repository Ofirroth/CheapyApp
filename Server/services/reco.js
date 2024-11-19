const { spawn } = require('child_process');
const userService = require('../services/user');
const Item = require('../models/item');

const getRecommended = async (username) => {
    try {
        const user = await userService.getUserByName(username);
        const userId = user._id.toString();  // Convert ObjectId to string

        return new Promise((resolve, reject) => {
            const pythonProcess = spawn('python', ['./recommendationSystem/KNNAlgo.py', userId]);
            let output = '';
            pythonProcess.stdout.on('data', (data) => {
                output += data.toString();
            });

            pythonProcess.stderr.on('data', (data) => {
                console.log('stderr:', data.toString());
            });

            pythonProcess.on('close', async (code) => {
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
                    const recommendedItemNames = resultJson.recommendedItems;

const items = await Item.find({ name: { $in: recommendedItemNames } });
// Remove duplicates based on the name field
const uniqueItems = [...new Map(items.map(item => [item.name, item])).values()];
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

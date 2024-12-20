const fs = require('fs');
const csvWriter = require('csv-write-stream');
const cartService = require('../services/cart');
const itemService = require('../services/item');
const Cart = require('../models/cart');

// Function to generate a user-item matrix where each user and their purchased items are tracked
async function generateUserItemDict() {
    // get all list carts
    const carts = await cartService.fetchCartData();
   // If no carts are found or an error occurred while fetching, log and return an empty object
    if (!carts || carts.length === 0) {
        console.log("No carts found or failed to fetch carts.");
        return {};  // Return an empty object
    }
    const userItemDict= {};
    // Loop through each cart
    carts.forEach(cart => {
        const userId = cart.userId;
        // Loop through each item in the cart
        cart.items.forEach(item => {
            if (!userItemDict[userId]) {
                userItemDict[userId] = {};
            }
            // Assign the quantity of the item purchased by the user
            if (!userItemDict[userId][item.name]) {
                userItemDict[userId][item.name] = 0;
            }
            userItemDict[userId][item.name] += item.quantity;
        });
    });
  return userItemDict;
}

// Function to create the final user-item matrix in the required format (rows with userId and item quantities)
async function createFinalMatrix() {
  // Generate the user-item matrix by calling the function above
  const userItemdict = await generateUserItemDict();
  console.log(userItemdict);
  // Fetch all available item names from the item service
  const allItems = await itemService.getAllItemsNames();

  const matrix = [];

  // Loop through each user in the user-item matrix
  for (const [userId, items] of Object.entries(userItemdict)) {
    const row = { userId };

    // Loop through each unique item and check if the user bought it
    allItems.forEach(item => {
      // If the user bought the item, add the quantity to the row, otherwise assign 0
      if (items[item]) {
        row[item] = 1
      }
      else {
        row[item] = 0;
      }
    });
    matrix.push(row);
  }
  return matrix;
}

// Function to save the matrix into a CSV file
async function saveMatrixToCSV() {
  // Create the final user-item matrix
  const matrix = await createFinalMatrix();

  const writer = csvWriter();
   // Open a writable stream to the file 'user_item_matrix.csv'
  writer.pipe(fs.createWriteStream('user_item_matrix.csv'));
  // Write each row of the matrix to the CSV file
  matrix.forEach(row => {
    writer.write(row);
  });

  writer.end();
  console.log('Matrix saved to user_item_matrix.csv');
}

// Export the function so it can be used in other parts of the application
module.exports = saveMatrixToCSV;

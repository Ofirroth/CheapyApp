const fs = require('fs');
const csvWriter = require('csv-write-stream');
const _ = require('lodash');
const cartService = require('../services/cart');
const itemService = require('../services/item');
const Cart = require('../models/cart');


async function generateUserItemMatrix() {
  const carts = await cartService.fetchCartData();
      if (!carts || carts.length === 0) {
            console.log("No carts found or failed to fetch carts.");
            return {};  // Return an empty object to avoid further errors
         }
  const userItemMatrix = {};
  // Loop through each cart
  carts.forEach(cart => {
    const userId = cart.userId;
    // Loop through each item in the cart
    cart.items.forEach(item => {
      if (!userItemMatrix[userId]) {
        userItemMatrix[userId] = {};
      }
      // Assign the quantity of the item purchased by the user
      userItemMatrix[userId][item.name] = item.quantity;
    });
  });
  return userItemMatrix;
}

// Function to create a user-item matrix in the final format
async function createFinalMatrix() {
  const userItemMatrix = await generateUserItemMatrix();
  const allItems = await itemService.getAllItemsNames();

  const matrix = [];

  // Loop through each user in the user-item matrix
  for (const [userId, items] of Object.entries(userItemMatrix)) {
    const row = { userId };

    // Loop through each unique item and check if the user bought it
    allItems.forEach(item => {
      row[item] = items[item] || 0;  // Use 0 if the user didn't buy the item
    });

    matrix.push(row);
  }

  return matrix;
}

// Function to save the matrix into a CSV file
async function saveMatrixToCSV() {
  const matrix = await createFinalMatrix();

  const writer = csvWriter();
  writer.pipe(fs.createWriteStream('user_item_matrix.csv'));

  matrix.forEach(row => {
    writer.write(row);
  });

  writer.end();
  console.log('Matrix saved to user_item_matrix.csv');
}


module.exports = saveMatrixToCSV;
